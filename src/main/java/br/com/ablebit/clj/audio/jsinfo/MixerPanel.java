/*
 *	MixerPanel.java
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

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;



public class MixerPanel
extends JPanel
implements ListSelectionListener
{
	private MixerTableModel		m_mixerTableModel;
	private JTable			m_mixerTable;
	private LineTableModel		m_dataLineTableModel;
	private JTable			m_dataLineTable;
	private AudioFormatTableModel	m_audioFormatTableModel;
	private JTable			m_audioFormatTable;
	private PortTableModel		m_portTableModel;
	private JTable			m_portTable;



	public MixerPanel()
	{
		setLayout(new GridLayout(0, 1));
		JPanel		panel = null;
		JPanel		p = null;
		JScrollPane	scrollPane = null;

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		String	strMixerName = MixerTableModel.getDefaultMixerName();
		String	strLabel = "Mixers (default: " + strMixerName + ")";
 		panel.add(new JLabel(strLabel), BorderLayout.NORTH);
		m_mixerTableModel = new MixerTableModel();
		m_mixerTable = new JTable(m_mixerTableModel);
		m_mixerTable.getSelectionModel().addListSelectionListener(this);
		scrollPane = new JScrollPane(m_mixerTable);
		panel.add(scrollPane, BorderLayout.CENTER);
		add(panel);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Available DataLines"), BorderLayout.NORTH);
		m_dataLineTableModel = new DataLineTableModel();

		m_dataLineTable = new JTable(m_dataLineTableModel);
		ListSelectionListener	listener = new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent event)
				{
					int	nIndex = m_dataLineTable.getSelectedRow();
					// JSInfoDebug.out("valueChanged(): selected row index: " + nIndex);
					setLine(nIndex);
				}
			};
		m_dataLineTable.getSelectionModel().addListSelectionListener(listener);
		scrollPane = new JScrollPane(m_dataLineTable);
		panel.add(scrollPane, BorderLayout.CENTER);
		add(panel);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("AudioFormats"), BorderLayout.NORTH);
		m_audioFormatTableModel = new AudioFormatTableModel();
		m_audioFormatTable = new JTable(m_audioFormatTableModel);
		scrollPane = new JScrollPane(m_audioFormatTable);
		panel.add(scrollPane, BorderLayout.CENTER);
		add(panel);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Available Ports"), BorderLayout.NORTH);
		m_portTableModel = new PortTableModel();

		m_portTable = new JTable(m_portTableModel);
		scrollPane = new JScrollPane(m_portTable);
		panel.add(scrollPane, BorderLayout.CENTER);
		add(panel);

	}



	/*
	 *	For the Mixer table
	 */
	public void valueChanged(ListSelectionEvent event)
	{
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.valueChanged(): begin"); }
		int	nIndex = m_mixerTable.getSelectedRow();
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.valueChanged(): selected row index: " + nIndex); }
		setMixer(nIndex);
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.valueChanged(): end"); }
	}



	private void setMixer(int nIndex)
	{
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setMixer(int): begin"); }
		Mixer	mixer = m_mixerTableModel.getMixer(nIndex);
		setMixer(mixer);
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setMixer(int): end"); }
	}



	private void setMixer(Mixer mixer)
	{
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setMixer(Mixer): begin"); }
		m_dataLineTableModel.setMixer(mixer);
		m_portTableModel.setMixer(mixer);
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setMixer(Mixer): end"); }
	}



	private void setLine(int nIndex)
	{
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setLine(): begin"); }
		DataLine.Info	info = null;
		if (nIndex >= 0)
		{
			info = (DataLine.Info) m_dataLineTableModel.getLineInfo(nIndex);
		}
		m_audioFormatTableModel.setInfo(info);
		if (JSInfoDebug.getTraceMixerPanel()) { JSInfoDebug.out("MixerPanel.setLine(): end"); }
	}
} 



/*** MixerPanel.java ***/
