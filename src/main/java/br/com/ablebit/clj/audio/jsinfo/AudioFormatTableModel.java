/*
 *	AudioFormatTableModel.java
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

import  javax.sound.sampled.AudioFormat;
import  javax.sound.sampled.AudioSystem;
import  javax.sound.sampled.DataLine;



public class AudioFormatTableModel
extends AbstractTableModel
{
	private static final String[]	COLUMN_NAMES =
	{
		"Encoding",
		"Sample Rate",
		"Sample Size (bits)",
		"Channels",
		"Frame Size (bytes)",
		"Frame Rate",
		"Endianess",
	};
	private static final AudioFormat[]	EMPTY_AUDIOFORMAT_ARRAY = new AudioFormat[0];

	private DataLine.Info		m_info;



	public AudioFormatTableModel()
	{
	}



	public void setInfo(DataLine.Info info)
	{
		m_info = info;
		fireTableDataChanged();
	}



	private AudioFormat[] getAudioFormats()
	{
		if (m_info != null)
		{
			return m_info.getFormats();
		}
		else
		{
			return EMPTY_AUDIOFORMAT_ARRAY;
		}
	}



	public int getRowCount()
	{
		return getAudioFormats().length;
	}



	private String[] getColumnArray()
	{
		return COLUMN_NAMES;
	}



	public int getColumnCount()
	{
		return getColumnArray().length;
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
		AudioFormat	format = getAudioFormats()[nRow];
		switch (nColumn)
		{
		case 0:		// encoding
			return format.getEncoding().toString();

		case 1:		// sample rate
			float	fSampleRate = format.getSampleRate();
			return (fSampleRate == AudioSystem.NOT_SPECIFIED) ? "any" : Float.toString(fSampleRate);

		case 2:		// sample size
			int	nSampleSize = format.getSampleSizeInBits();
			return (nSampleSize == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nSampleSize);

		case 3:		// channels
			int	nChannels = format.getChannels();
			return (nChannels == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nChannels);

		case 4:		// frame size
			int	nFrameSize = format.getFrameSize();
			return (nFrameSize == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nFrameSize);

		case 5:		// frame rate
			float	fFrameRate = format.getFrameRate();
			return (fFrameRate == AudioSystem.NOT_SPECIFIED) ? "any" : Float.toString(fFrameRate);

		case 6:		// endianess
			return format.isBigEndian() ? "big" : "little";

		default:
			return null;
		}
	}
}



/*** AudioFormatTableModel.java ***/
