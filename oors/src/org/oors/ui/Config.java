// Copyright (c) 2011, OORS contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// * Neither the name of the OORS Project nor the
// names of its contributors may be used to endorse or promote products
// derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL CONTRIBUTORS OF THE OORS PROJECT BE LIABLE FOR
// ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.oors.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Config
{
	private final static Logger	LOG			= Logger.getLogger(Config.class
													.getName());

	private static Config		instance	= null;

	public static Config getInstance()
	{
		if (instance == null) instance = new Config();
		return instance;
	}

	private String						helpIndex;

	private HashMap<String, ImageIcon>	icons;
	private LinkedList<String>			startupScriptFilenames;
	private ImageIcon					notFound;
	private HashMap<String, JPopupMenu>	menus;
	private int							splashWidth;
	private String						splashImage;
	private int							splashHeight;
	private int							splashMessageX;
	private int							splashMessageY;
	private String						splashMessageFont;
	private String						splashMessageColor;
	private int							splashMessageLength;

	public String getHelpIndex()
	{
		return helpIndex;
	}

	public int getSplashWidth()
	{
		return splashWidth;
	}

	public String getSplashImage()
	{
		return splashImage;
	}

	public int getSplashHeight()
	{
		return splashHeight;
	}

	public int getSplashMessageX()
	{
		return splashMessageX;
	}

	public int getSplashMessageY()
	{
		return splashMessageY;
	}

	public String getSplashMessageFont()
	{
		return splashMessageFont;
	}

	public String getSplashMessageColor()
	{
		return splashMessageColor;
	}

	public int getSplashMessageLength()
	{
		return splashMessageLength;
	}

	public List<String> getStartupScriptFilenames()
	{
		return this.startupScriptFilenames;
	}

	public JPopupMenu getMenu(String ui_oors_selection)
	{
		return menus.get(ui_oors_selection);
	}

	public ImageIcon getIcon(String name)
	{
		ImageIcon ii = icons.get(name);
		if (ii == null) return notFound;
		return ii;
	}

	private Document	document;
	private XPath		xpath;

	private Config()
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			docFactory.setValidating(false);
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			document = builder.parse("config.xml");

			XPathFactory xFactory = XPathFactory.newInstance();
			xpath = xFactory.newXPath();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void readConfiguration()
	{
		NodeList nodes;

		try
		{
			// splash //////////////////////////////////////

			splashImage = (String) xpath.evaluate("//splash/image", document,
					XPathConstants.STRING);
			splashWidth = Integer.parseInt((String) xpath.evaluate(
					"//splash/width", document, XPathConstants.STRING));
			splashHeight = Integer.parseInt((String) xpath.evaluate(
					"//splash/height", document, XPathConstants.STRING));
			splashMessageX = Integer.parseInt((String) xpath.evaluate(
					"//splash/message/x", document, XPathConstants.STRING));
			splashMessageY = Integer.parseInt((String) xpath.evaluate(
					"//splash/message/y", document, XPathConstants.STRING));
			splashMessageFont = (String) xpath.evaluate(
					"//splash/message/font", document, XPathConstants.STRING);
			splashMessageColor = (String) xpath.evaluate(
					"//splash/message/color", document, XPathConstants.STRING);
			splashMessageLength = Integer.parseInt((String) xpath.evaluate(
					"//splash/message/max-length", document,
					XPathConstants.STRING));

			// scripts //////////////////////////////////////

			startupScriptFilenames = new LinkedList<String>();

			nodes = (NodeList) xpath.evaluate("//startup/script", document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++)
			{
				try
				{
					startupScriptFilenames.add(nodes.item(i).getTextContent());
				}
				catch (Exception ex)
				{
					LOG.info("Failed to find ImageIcon file: "
							+ nodes.item(i).getTextContent());
				}
			}
			// icons //////////////////////////////////////

			icons = new HashMap<String, ImageIcon>();

			nodes = (NodeList) xpath.evaluate(
					"//icons/icon/name | //icons/icon/filename", document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++)
			{
				try
				{
					icons.put(nodes.item(i++).getTextContent(), new ImageIcon(
							nodes.item(i).getTextContent()));
				}
				catch (Exception ex)
				{
					LOG.info("Failed to find ImageIcon file: "
							+ nodes.item(i).getTextContent());
				}
			}

			try
			{
				notFound = new ImageIcon((String) xpath.evaluate(
						"//icons/@default", document, XPathConstants.STRING));
			}
			catch (Exception ex)
			{
				LOG.info("Failed to find default image");
			}

			// actions ////////////////////////////////////

			HashMap<String, OorsAction> actions = new HashMap<String, OorsAction>();

			nodes = (NodeList) xpath.evaluate("oors-ui/actions/action",
					document, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); ++i)
			{

				String name = (String) xpath.evaluate("name", nodes.item(i),
						XPathConstants.STRING);
				String script_filename = (String) xpath.evaluate("script",
						nodes.item(i), XPathConstants.STRING);

				OorsAction oa = new OorsAction(script_filename);
				oa.putValue(OorsAction.SMALL_ICON, Config.getInstance()
						.getIcon(name));
				oa.putValue(OorsAction.NAME, name);
				oa.putValue(OorsAction.SHORT_DESCRIPTION, name);
				actions.put(name, oa);

				// try
				// {
				// Class<?> c = Class.forName(class_name);
				//
				// if (c.getSuperclass() == OorsAction.class)
				// {
				// OorsAction oa;
				// try
				// {
				// Constructor<?> cons = c.getConstructor();
				// oa = (OorsAction) cons.newInstance();
				// oa.putValue(OorsAction.SMALL_ICON, Config.getInstance()
				// .getIcon(name));
				// oa.putValue(OorsAction.NAME, name);
				// oa.putValue(OorsAction.SHORT_DESCRIPTION, name);
				// actions.put(name, oa);
				// }
				// catch (Exception e)
				// {
				// LOG.info("failed to find "+class_name+" "+e.getMessage());
				// }
				// }
				// else
				// {
				// LOG.info(class_name + " not a sub class");
				// }
				// }
				// catch (Exception e)
				// {
				// LOG.info(class_name+" - " + e.getMessage());
				// }
			}

			// menus //////////////////////////////////

			menus = new HashMap<String, JPopupMenu>();

			nodes = (NodeList) xpath.evaluate("//menus/menu", document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++)
			{
				Node n = nodes.item(i);

				String ui = (String) xpath.evaluate("ui-element", n,
						XPathConstants.STRING);
				String selection = (String) xpath.evaluate("selection", n,
						XPathConstants.STRING);

				JPopupMenu pm = new JPopupMenu();
				menus.put(ui + "-" + selection, pm);

				NodeList nl_actions = (NodeList) xpath.evaluate("action", n,
						XPathConstants.NODESET);
				for (int j = 0; j < nl_actions.getLength(); j++)
				{
					OorsAction a = actions.get(nl_actions.item(j)
							.getTextContent());
					pm.add(a);
				}
			}

			// help //////////////////////////////////////

			helpIndex = ((String) xpath.evaluate("oors-ui/help", document,
					XPathConstants.STRING));

			// cleanup //////////////////////////////////////

			document = null;
			xpath = null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
