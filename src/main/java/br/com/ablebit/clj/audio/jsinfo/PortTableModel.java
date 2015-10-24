/*
 *	PortTableModel.java
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

import  javax.swing.table.AbstractTableModel;

import  javax.sound.sampled.Mixer;
import  javax.sound.sampled.AudioSystem;
import  javax.sound.sampled.Line;
import  javax.sound.sampled.DataLine;
import  javax.sound.sampled.Port;



public class PortTableModel
extends LineTableModel
{
	private static final String[] PORT_COLUMN_NAMES =
	{
		LineTableModel.DATA_LINE_COLUMN_NAMES[0],
		LineTableModel.DATA_LINE_COLUMN_NAMES[1],
		"Name",
		"Is source"
	};



	public PortTableModel()
	{
		super(Port.Info.class);
	}



	protected String[] getColumnArray()
	{
		return PORT_COLUMN_NAMES;
	}



	public Object getValueAt(int nRow, int nColumn)
	{
		if (nColumn < 2)
		{
			return super.getValueAt(nRow, nColumn);
		}
		Port.Info	info = (Port.Info) getLineInfo(nRow);
		switch (nColumn)
		{
		case 2:
			return "" + info.getName();

		case 3:
			return "" + info.isSource();

		default:
			return null;
		}
	}
}



/*** PortTableModel.java ***/
