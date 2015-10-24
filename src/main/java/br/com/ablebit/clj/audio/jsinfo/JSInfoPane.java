/*
 *	JSInfoPane.java
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



public class JSInfoPane
extends JTabbedPane
{
	private JPanel	m_mixerPanel = null;
	private JPanel	m_controlsPanel = null;
	private JPanel	m_lineLifeCyclePanel = null;
	private JPanel	m_lineTestPanel = null;
	private JPanel	m_converterTestPanel = null;
	private JPanel	m_midiDevicePanel = null;
	private JPanel	m_serviceProviderPanel = null;
	private JPanel	m_infoPanel = null;



	public JSInfoPane()
	{
		super();
		m_mixerPanel = new MixerPanel();
		this.add(m_mixerPanel, "Mixer/Line");
		m_controlsPanel = new ControlsPanel();
		this.add(m_controlsPanel, "Controls");
		m_lineLifeCyclePanel = new LineLifeCyclePanel();
		this.add(m_lineLifeCyclePanel, "Line Lifecycle");
		m_lineTestPanel = new LineTestPanel();
		this.add(m_lineTestPanel, "DataLine Test");
		m_converterTestPanel = new ConverterTestPanel();
		this.add(m_converterTestPanel, "Converter Test");
		m_midiDevicePanel = new MidiDevicePanel();
		this.add(m_midiDevicePanel, "MIDI Devices");
		m_serviceProviderPanel = new ServiceProviderPanel();
		this.add(m_serviceProviderPanel, "Service Providers");
		m_infoPanel = new InfoPanel();
		this.add(m_infoPanel, "Info");
	}
}



/*** JSInfoPane.java ***/
