/*
 *	MixerTableModel.java
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

import  javax.sound.sampled.Line;
import  javax.sound.sampled.Mixer;
import  javax.sound.sampled.AudioSystem;



public class MixerTableModel
extends AbstractTableModel
{
	private static final String[] sm_astrColumnNames =
	{
		"Name", "Vendor", "Description", "Version", "Class"/*, "Channels", "Voices", "Latency (ns)"*/
	};

	/** Cached value of AudioSystem.getMixer(null).
	 */
	private static String	sm_strDefaultMixerName;

	/** Cached value of AudioSystem.getMixerInfo().
	 */
	private Mixer.Info[]	m_aMixerInfos;

	/** Cached values of AudioSystem.getMixer().
	    The values here are retrieved by calling AudioSystem.getMixer()
	    with the info objects stored in m_aMixerInfos.
	 */
	private Mixer[]		m_aMixers;



	public MixerTableModel()
	{
	}


	public int getRowCount()
	{
		return getMixerInfo().length;
	}


	public int getColumnCount()
	{
		return sm_astrColumnNames.length;
	}


	public Class getColumnClass(int nColumn)
	{
		return String.class;
	}


	public String getColumnName(int nColumn)
	{
		return sm_astrColumnNames[nColumn];
	}


	public Object getValueAt(int nRow, int nColumn)
	{
		Mixer.Info	info = getMixerInfo(nRow);
		Mixer		mixer = getMixer(nRow);
		// TODO ??
		// Line.Info	lineInfo = mixer.getLineInfo();
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
			return mixer.getClass();

		default:
			return null;
		}
	}


	private Mixer.Info[] getMixerInfo()
	{
		if (m_aMixerInfos == null)
		{
			m_aMixerInfos = AudioSystem.getMixerInfo();
		}
		return m_aMixerInfos;
	}



	private Mixer.Info getMixerInfo(int nIndex)
	{
		Mixer.Info[]	aInfos = getMixerInfo();
		return aInfos[nIndex];
	}



	// TODO: make private (used in MixerTableModel)
	public Mixer getMixer(int nIndex)
	{
		if (m_aMixers == null)
		{
			m_aMixers = new Mixer[getMixerInfo().length];
		}
		if (m_aMixers[nIndex] == null)
		{
			Mixer.Info	info = getMixerInfo(nIndex);
			m_aMixers[nIndex] = AudioSystem.getMixer(info);
		}
		return m_aMixers[nIndex];
	}



	public static String getDefaultMixerName()
	{
		if (sm_strDefaultMixerName == null)
		{
			Mixer   defaultMixer = AudioSystem.getMixer(null);
			sm_strDefaultMixerName = defaultMixer.getMixerInfo().getName();
		}
		return sm_strDefaultMixerName;
	}
}



/*** MixerTableModel.java ***/
