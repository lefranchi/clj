/*
 *	JSInfoApplet.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class JSInfoApplet
extends JApplet
{
	boolean	m_bPackFrame = false;



	public JSInfoApplet()
	{
	}


	public void init()
	{
		JComponent tabbedPane = new JSInfoPane();
		getContentPane().add(tabbedPane);

		//Validate frames that have preset sizes
		//Pack frames that have useful preferred size info, e.g. from their layout
/*
		if (m_bPackFrame)
		{
			frame.pack();
		}
		else
		{
			frame.validate();
		}
*/

	}
}


 
/*** JSInfoApplet.java ***/
