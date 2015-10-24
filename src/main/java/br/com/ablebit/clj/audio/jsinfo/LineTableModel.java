/*
 *	LineTableModel.java
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

import  java.util.ArrayList;
import  java.util.List;

import  javax.swing.table.AbstractTableModel;

import  javax.sound.sampled.Mixer;
import  javax.sound.sampled.AudioSystem;
import  javax.sound.sampled.Line;
import  javax.sound.sampled.DataLine;



public abstract class LineTableModel
extends AbstractTableModel
{
	private static final Line.Info[]	EMPTY_LINE_INFO_ARRAY = new Line.Info[0];
	protected static final String[] DATA_LINE_COLUMN_NAMES =
	{
		"Class", "max. Number"
	};




	private Class		m_lineInfoClass;
	private Mixer		m_mixer;



	public LineTableModel(Class lineInfoClass)
	{
		m_lineInfoClass = lineInfoClass;
	}



	private Class getLineInfoClass()
	{
		return m_lineInfoClass;
	}



	public Mixer getMixer()
	{
		return m_mixer;
	}



	public void setMixer(Mixer mixer)
	{
		m_mixer = mixer;
		fireTableDataChanged();
	}



	protected Line.Info[] getLineInfo()
	{
		if (JSInfoDebug.getTraceLineTableModel())
		{
			JSInfoDebug.out("LineTableModel.getLineInfo(): begin");
		}
		Line.Info[]	allInfos = null;
		Mixer	mixer = getMixer();
		if (mixer != null)
		{
			Line.Info[]	sdlInfos = getMixer().getSourceLineInfo();
			if (JSInfoDebug.getTraceLineTableModel()) { JSInfoDebug.out("AllLineTableModel.getLineInfo(): source lines infos: " + sdlInfos.length); }
			Line.Info[]	tdlInfos = getMixer().getTargetLineInfo();
			if (JSInfoDebug.getTraceLineTableModel()) { JSInfoDebug.out("AllLineTableModel.getLineInfo(): target lines infos: " + tdlInfos.length); }
			allInfos = new Line.Info[sdlInfos.length + tdlInfos.length];
			System.arraycopy(sdlInfos, 0, allInfos, 0, sdlInfos.length);
			System.arraycopy(tdlInfos, 0, allInfos, sdlInfos.length, tdlInfos.length);
		}
		else
		{
			allInfos = EMPTY_LINE_INFO_ARRAY;
		}
		List	selectedInfoList = new ArrayList();
		for (int i = 0; i < allInfos.length; i++)
		{
			if (getLineInfoClass().isInstance(allInfos[i]))
			{
				selectedInfoList.add(allInfos[i]);
			}
		}
		Line.Info[]	selectedInfos = (Line.Info[]) selectedInfoList.toArray(EMPTY_LINE_INFO_ARRAY);
		if (JSInfoDebug.getTraceLineTableModel()) { JSInfoDebug.out("AllLineTableModel.getLineInfo(): end"); }
		return selectedInfos;
	}



	public int getRowCount()
	{
		return getLineInfo().length;
	}


	protected abstract String[] getColumnArray();


	public int getColumnCount()
	{
		return getColumnArray().length;
	}


	public Line.Info getLineInfo(int nRow)
	{
		return getLineInfo()[nRow];
	}


	public Class getColumnClass(int nColumn)
	{
		return String.class;
	}


	public String getColumnName(int nColumn)
	{
		return getColumnArray()[nColumn];
	}


	public Object getValueAt(int nRow, int nColumn)
	{
		Line.Info	info = getLineInfo(nRow);
		switch (nColumn)
		{
		case 0:
			return info.getLineClass().toString();

		case 1:
			int	nMaxLines = getMixer().getMaxLines(info);
			return (nMaxLines == AudioSystem.NOT_SPECIFIED) ? "unlimited" : "" + nMaxLines;

		default:
			return null;
		}
	}
}



/*** LineTableModel.java ***/
