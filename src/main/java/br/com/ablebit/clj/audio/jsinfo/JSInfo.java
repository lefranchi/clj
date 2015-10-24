/*
 *	JSInfo.java
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
import javax.swing.JComponent;
import javax.swing.JFrame;



public class JSInfo
{
	boolean	m_bPackFrame = false;



	public JSInfo()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Java Sound Info");
		frame.setSize(new Dimension(800, 500));
		WindowAdapter	windowAdapter = new WindowAdapter()
			{
				public void windowClosing(WindowEvent we)
				{
					System.exit(0);
				}
			};
		frame.addWindowListener(windowAdapter);

		JComponent tabbedPane = new JSInfoPane();
		frame.getContentPane().add(tabbedPane);

		//Validate frames that have preset sizes
		//Pack frames that have useful preferred size info, e.g. from their layout
		if (m_bPackFrame)
		{
			frame.pack();
		}
		else
		{
			frame.validate();
		}

		//Center the window
		Dimension	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension	frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}



	public static void main(String[] args)
	{
		try {
			new JSInfo();
		} catch (Throwable t) {
			System.err.println("Exception occurred in main():");
			t.printStackTrace();
			System.exit(1);
		}
	}
}


/*** JSInfo.java ***/
