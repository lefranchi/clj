/*
 *	MidiDevicePanel.java
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

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;



public class MidiDevicePanel
extends JPanel
{
	private JTable		m_sequencerTable;
	private JTable		m_synthesizerTable;
	private JTable		m_midiDeviceTable;



	public MidiDevicePanel()
	{
		setLayout(new GridLayout(0, 1));
		JPanel		tablePanel = null;
		JScrollPane	scrollPane = null;
		String		strLabel = null;

		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		String	strSequencerName = SequencerTableModel.getDefaultSequencerName();
		strLabel = "Sequencers (default: " + strSequencerName + ")";
		tablePanel.add(new JLabel(strLabel), BorderLayout.NORTH);
		m_sequencerTable = new JTable(new SequencerTableModel());
		scrollPane = new JScrollPane(m_sequencerTable);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		add(tablePanel);

		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		String	strSynthesizerName = SynthesizerTableModel.getDefaultSynthesizerName();
		strLabel = "Synthesizers (default: " + strSynthesizerName + ")";
		tablePanel.add(new JLabel(strLabel), BorderLayout.NORTH);
		m_synthesizerTable = new JTable(new SynthesizerTableModel());
		scrollPane = new JScrollPane(m_synthesizerTable);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		add(tablePanel);

		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(new JLabel("Other MIDI Devices"), BorderLayout.NORTH);
		m_midiDeviceTable = new JTable(new MidiDeviceTableModel());
		scrollPane = new JScrollPane(m_midiDeviceTable);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		add(tablePanel);
	}



} 



/*** MidiDevicePanel.java ***/
