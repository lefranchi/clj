/*
 *	InfoPanel.java
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
import javax.swing.JTabbedPane;
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



public class InfoPanel
extends JPanel
{
	private JTabbedPane		m_tabbedPane;


	public InfoPanel()
	{
		this.setLayout(new BorderLayout());
		JPanel	topPanel = new JPanel();
		// TODO: fetch version from somewhere else
		topPanel.add(new JLabel("JSInfo 0.x.0"));
		this.add(topPanel, BorderLayout.NORTH);

		m_tabbedPane = new JTabbedPane();
		this.add(m_tabbedPane, BorderLayout.CENTER);

		JPanel	aboutPanel = new JPanel();
		aboutPanel.add(new JLabel("Copyright (c) 2000 - 2002 Matthias Pfisterer"));
		m_tabbedPane.add(aboutPanel, "About");

		JPanel	authorsPanel = new JPanel();
		authorsPanel.add(new JLabel("Copyright (c) 2000 - 2002 Matthias Pfisterer"));
		m_tabbedPane.add(authorsPanel, "Authors");

		JPanel	licensePanel = new JPanel();
		licensePanel.add(new JLabel("This software is distributed under the terms of the GNU General Public License"));
		m_tabbedPane.add(licensePanel, "License agreemant");
	}
} 



/*** InfoPanel.java ***/
