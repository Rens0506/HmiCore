/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package hmi.util;

/**
 * Singleton for synchronization between animation and render threads
 * @author welberge
 */
public final class AnimationSync implements Sync
{
    private AnimationSync(){}
    private static volatile Object sync = null;
    public static synchronized Object getSync()
    {
        if(sync==null)
        {
            sync = new Object();
        }
        return sync;
    }
	public synchronized Object getStaticSync()
	{
		return getSync();
	}
}