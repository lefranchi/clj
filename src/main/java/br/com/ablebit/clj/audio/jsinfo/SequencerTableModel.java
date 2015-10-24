/*
 *	SequencerTableModel.java
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import  javax.sound.midi.Sequencer;
import  javax.sound.midi.Synthesizer;
import  javax.sound.midi.MidiDevice;
import  javax.sound.midi.MidiSystem;
import  javax.sound.midi.MidiDevice;
import  javax.sound.midi.MidiUnavailableException;



public class SequencerTableModel
  extends MidiDeviceTableModel
{
	private static final String[] sm_astrColumnNames = {"Name", "Vendor", "Description", "Version", "Class", "max.Receivers", "max. Transmitters", "Position (ms)", "MasterSyncModes", "SlaveSyncModes"};


  
	public SequencerTableModel()
	{
		super();
	}



	protected boolean useIt(MidiDevice device)
	{
		return  (device instanceof Sequencer);
	}



	protected String[] getColumnNames()
	{
		return sm_astrColumnNames;
	}



	public Object getValueAt(int nRow, int nColumn)
	{
		if (nColumn < 8)
		{
			return super.getValueAt(nRow, nColumn);
		}
		Sequencer	device = (Sequencer) m_devices.get(nRow);
		Sequencer.SyncMode[]	modes = null;
		switch (nColumn)
		{
		case 8:
			modes = device.getMasterSyncModes();
			return "" + getSyncModes(modes);

		case 9:
			modes = device.getSlaveSyncModes();
			return "" + getSyncModes(modes);

		default:
			return null;
		}
	}



	private static String getSyncModes(Sequencer.SyncMode[] modes)
	{
		String	s = "";
		for (int i = 0; i < modes.length; i++)
		{
			s += modes[i] + " ";
		}
		return s;
	}



	public static String getDefaultSequencerName()
	{
		String	strSequencerName = null;
		try
		{
			Sequencer   defaultSequencer = MidiSystem.getSequencer();
			strSequencerName = defaultSequencer.getDeviceInfo().getName();
		}
		catch (MidiUnavailableException e)
		{
				if (JSInfoDebug.getTraceAllExceptions()) { JSInfoDebug.out(e); }
		}
		return strSequencerName;
	}
}



/*** SequencerTableModel.java ***/
