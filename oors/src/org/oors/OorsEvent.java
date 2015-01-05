package org.oors;

public class OorsEvent
{
	private OorsEventGenerator affected;
	
	public OorsEventGenerator getAffected()
	{
		return affected;
	}
	
	public OorsEvent( OorsEventGenerator b )
	{
		this.affected = b;
	}
}
