/*
 *	ServiceProviderPanel.java
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

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Mixer;



public class ServiceProviderPanel
extends JPanel
implements ItemListener
{
	private static final String[]	m_astrCategories =
	{
		"javax.sound.midi.spi.MidiDeviceProvider",
		"javax.sound.midi.spi.MidiFileReader",
		"javax.sound.midi.spi.MidiFileWriter",
		"javax.sound.midi.spi.SoundbankReader",
		"javax.sound.sampled.spi.AudioFileReader",
		"javax.sound.sampled.spi.AudioFileWriter",
		"javax.sound.sampled.spi.FormatConversionProvider",
		"javax.sound.sampled.spi.MixerProvider",
	};


	private JComboBox			m_categoryComboBox;
	private ServiceProviderTableModel	m_serviceProviderTableModel;
	private JTable				m_serviceProviderTable;
	private ConfigurationFilesTableModel	m_configurationFilesTableModel;
	private JTable				m_configurationFilesTable;



	public ServiceProviderPanel()
	{
		this.setLayout(new BorderLayout());
		JPanel	controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		controlPanel.add(new JLabel("Category:"));
		m_categoryComboBox = new JComboBox(m_astrCategories);
		m_categoryComboBox.addItemListener(this);
		controlPanel.add(m_categoryComboBox);

		this.add(controlPanel, BorderLayout.NORTH);

		JPanel	mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.add(mainPanel, BorderLayout.CENTER);

		JPanel	providersPanel = new JPanel();
		providersPanel.setLayout(new BorderLayout());
		mainPanel.add(providersPanel);

		providersPanel.add(new JLabel("Available Providers:"), BorderLayout.NORTH);

		m_serviceProviderTableModel = new ServiceProviderTableModel();
		m_serviceProviderTable = new JTable(m_serviceProviderTableModel);
		JScrollPane	serviceProviderScrollPane = new JScrollPane(m_serviceProviderTable);
		providersPanel.add(serviceProviderScrollPane, BorderLayout.CENTER);

		JPanel	configurationFilesPanel = new JPanel();
		configurationFilesPanel.setLayout(new BorderLayout());
		mainPanel.add(configurationFilesPanel);

		configurationFilesPanel.add(new JLabel("Configuration Files:"), BorderLayout.NORTH);

		m_configurationFilesTableModel = new ConfigurationFilesTableModel();
		m_configurationFilesTable = new JTable(m_configurationFilesTableModel);
		JScrollPane	configurationFilesScrollPane = new JScrollPane(m_configurationFilesTable);
		configurationFilesPanel.add(configurationFilesScrollPane, BorderLayout.CENTER);

		setCategory(0);
	}



	/*
	 *	For the category ComboBox
	 */
	public void itemStateChanged(ItemEvent event)
	{
		int	nIndex = m_categoryComboBox.getSelectedIndex();
/*
		if (JSInfoDebug.getTraceServiceProviderPanel())
		{
			JSInfoDebug.out("ServiceProviderPanel.itemStateChanged(): index: " + nIndex);
		}
*/
		setCategory(nIndex);
	}



	private void setCategory(int nCategory)
	{
		m_serviceProviderTableModel.setName(m_astrCategories[nCategory]);
		m_configurationFilesTableModel.setName(m_astrCategories[nCategory]);
	}


} 



/*** ServiceProviderPanel.java ***/
