/*
 *	MidiDeviceTableModel.java
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

import  javax.sound.midi.Sequencer;
import  javax.sound.midi.Synthesizer;
import  javax.sound.midi.MidiDevice;
import  javax.sound.midi.MidiSystem;
import  javax.sound.midi.MidiDevice;
import  javax.sound.midi.MidiUnavailableException;



public class MidiDeviceTableModel
  extends AbstractTableModel
{
	// private static final String[] sm_astrColumnNames = {"Name", "Vendor", "Description", "Version", "Class", "Channels", "Voices", "Latency (ns)"};
	private static final String[] sm_astrColumnNames = {"Name", "Vendor", "Description", "Version", "Class", "max.Receivers", "max. Transmitters", "Position (us)"};


	/**	The devices in this model.
		This list contains instances of MidiDevice.
		The list is built by init().

		@see init()
	 */
	protected List	m_devices;



	public MidiDeviceTableModel()
	{
		init();
	}



	public void init()
	{
		m_devices = new ArrayList();
		MidiDevice.Info[]  infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++)
		{
			MidiDevice	device = null;
			try
			{
				device = MidiSystem.getMidiDevice(infos[i]);
				if (useIt(device))
				{
					m_devices.add(device);
				}
			}
			catch (MidiUnavailableException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
			}
		}
	}



	/** Checks if the passed device should be displayed in this model.
	    The implementation in this class returns true if the device
	    is not a Sequencer and not a Synthesizer.
	    Subclasses should override this method to get other partitions
	    of the MidiDevice space.
	 */
	protected boolean useIt(MidiDevice device)
	{
		return ! (device instanceof Sequencer || device instanceof Synthesizer);
	}



	/**	Returns the number of devices in the list.
	 */
	public int getRowCount()
	{
		return m_devices.size();
	}



	public int getColumnCount()
	{
		return getColumnNames().length;
	}



	public Class getColumnClass(int nColumn)
	{
		return String.class;
	}



	public String getColumnName(int nColumn)
	{
		return getColumnNames()[nColumn];
	}



	protected String[] getColumnNames()
	{
		return sm_astrColumnNames;
	}



	public Object getValueAt(int nRow, int nColumn)
	{
		MidiDevice device = (MidiDevice) m_devices.get(nRow);
		MidiDevice.Info  info = device.getDeviceInfo();
		switch (nColumn)
		{
		case 0:
			return info.getName();

		case 1:
			return info.getVendor();

		case 2:
			return info.getDescription();

		case 3:
			return info.getVersion();

		case 4:
			return device.getClass().getName();

		case 5:
			int	nMaxReceivers = device.getMaxReceivers();
			return (nMaxReceivers == -1) ? "unlimited" : "" + nMaxReceivers;

		case 6:
			int	nMaxTransmitters = device.getMaxTransmitters();
			return (nMaxTransmitters == -1) ? "unlimited" : "" + nMaxTransmitters;

		case 7:
			long	lMicrosecondPosition = device.getMicrosecondPosition();
			return (lMicrosecondPosition == -1) ? "unsupported" : "" + lMicrosecondPosition;

		default:
			return null;
		}
	}
} 



/*** MidiDeviceTableModel.java ***/
