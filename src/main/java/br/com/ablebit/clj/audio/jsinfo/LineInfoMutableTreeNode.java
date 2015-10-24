/*
 *	LineInfoMutableTreeNode.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package br.com.ablebit.clj.audio.jsinfo;

import javax.swing.tree.DefaultMutableTreeNode;

import javax.sound.sampled.Line;
import javax.sound.sampled.Port;



/**	MutableTreeNode for Line.Infos.
	This class overrides toString() of DefaultMutableTreeNode,
	so that the label shown in the tree view can be controlled.
*/
public class LineInfoMutableTreeNode
extends DefaultMutableTreeNode
{
	public LineInfoMutableTreeNode(Line.Info userObject)
	{
		super(userObject);
	}


	public String toString()
	{
		Line.Info	info = (Line.Info) getUserObject();
		String	str = null;
		if (info instanceof Port.Info)
		{
			Port.Info	portInfo = (Port.Info) info;
			str = "Port " + portInfo.getName();
			str += portInfo.isSource() ? " (source)" : " (target)";
		}
		else
		{
			str = info.toString();
		}
		return str;
	}

} 



/*** LineInfoMutableTreeNode.java ***/
