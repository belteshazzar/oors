package org.oors;

import java.util.EventListener;

public interface OorsListener extends EventListener
{
	public void createdUpdate( OorsEvent event );
	public void modifiedUpdate( OorsEvent event );
	public void deletedUpdate( OorsEvent event );
}
