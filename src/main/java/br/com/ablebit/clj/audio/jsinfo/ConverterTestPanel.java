/*
 *	ConverterTestPanel.java
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

import java.io.ByteArrayInputStream;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;



public class ConverterTestPanel
extends JPanel
implements ActionListener
{
	private AudioFormatPanel	m_sourceFormatPanel;
	private AudioFormatPanel	m_targetFormatPanel;
	private JTextArea		m_textArea;


	public ConverterTestPanel()
	{
		setLayout(new BorderLayout());
		JPanel	topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 0)/*new BoxLayout(topPanel, BoxLayout.X_AXIS)*/);
		m_sourceFormatPanel = new AudioFormatPanel();
		m_sourceFormatPanel.setBorder(new TitledBorder(new EtchedBorder(), "Source Format"));
		topPanel.add(m_sourceFormatPanel);
		m_targetFormatPanel = new AudioFormatPanel();
		m_targetFormatPanel.setBorder(new TitledBorder(new EtchedBorder(), "Target Format"));
		topPanel.add(m_targetFormatPanel);

		JPanel		buttonPanel = new JPanel(new GridLayout(0, 1));
		JButton		testPossibleButton;
		JToolTip	toolTip;
		JPanel	topButtonPanel = new JPanel(new GridLayout(0, 1));
		topButtonPanel.setBorder(new TitledBorder(new EtchedBorder(), "specifying Encoding target"));

		testPossibleButton = new JButton("Check if supported");
		testPossibleButton.setToolTipText("Uses AudioSystem.isConversionSupported(AudioFormat.Encoding, AudioInputStream)");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("issupported/encoding");
		topButtonPanel.add(testPossibleButton);

		testPossibleButton = new JButton("Try to get stream");
		testPossibleButton.setToolTipText("Uses AudioSystem.getAudioInputStream(AudioFormat.Encoding, AudioInputStream)");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("getstream/encoding");
		topButtonPanel.add(testPossibleButton);

		buttonPanel.add(topButtonPanel);
		JPanel	bottomButtonPanel = new JPanel(new GridLayout(0, 1));
		bottomButtonPanel.setBorder(new TitledBorder(new EtchedBorder(), "specifying AudioFormat target"));

		testPossibleButton = new JButton("Check if supported");
		testPossibleButton.setToolTipText("Uses AudioSystem.isConversionSupported(AudioFormat, AudioInputStream)");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("issupported/audioformat");
		bottomButtonPanel.add(testPossibleButton);

		testPossibleButton = new JButton("Try to get stream");
		testPossibleButton.setToolTipText("Uses AudioSystem.getAudioInputStream(AudioFormat, AudioInputStream)");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("getstream/audioformat");
		bottomButtonPanel.add(testPossibleButton);

		buttonPanel.add(bottomButtonPanel);
		buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Tests"));
		topPanel.add(buttonPanel);

		add(topPanel, BorderLayout.NORTH);

		m_textArea = new JTextArea();
		JScrollPane	scrollPane = new JScrollPane(m_textArea);
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Results"));
		add(scrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		out("Test: " + strActionCommand);
		if (strActionCommand.equals("issupported/audioformat"))
		{
			AudioFormat	sourceFormat = m_sourceFormatPanel.getAudioFormat();
			AudioFormat	targetFormat = m_targetFormatPanel.getAudioFormat();
			out("source format: " + sourceFormat);
			out("target format: " + targetFormat);
			outPossible(AudioSystem.isConversionSupported(targetFormat, sourceFormat));
		}
		else if (strActionCommand.equals("issupported/encoding"))
		{
			AudioFormat	sourceFormat = m_sourceFormatPanel.getAudioFormat();
			AudioFormat.Encoding	targetEncoding = m_targetFormatPanel.getAudioFormat().getEncoding();
			out("source format: " + sourceFormat);
			out("target encoding: " + targetEncoding);
			outPossible(AudioSystem.isConversionSupported(targetEncoding, sourceFormat));
		}
		else if (strActionCommand.equals("getstream/audioformat"))
		{
			AudioFormat	sourceFormat = m_sourceFormatPanel.getAudioFormat();
			AudioFormat	targetFormat = m_targetFormatPanel.getAudioFormat();
			out("source format: " + sourceFormat);
			out("target format: " + targetFormat);
			AudioInputStream	ais = new AudioInputStream(
				new ByteArrayInputStream(new byte[0]),
				sourceFormat,
				AudioSystem.NOT_SPECIFIED);
			try
			{
				AudioSystem.getAudioInputStream(targetFormat, ais);
				outPossible(true);
			}
			catch (IllegalArgumentException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				outPossible(false);
			}
		}
		else if (strActionCommand.equals("getstream/encoding"))
		{
			AudioFormat	sourceFormat = m_sourceFormatPanel.getAudioFormat();
			AudioFormat.Encoding	targetEncoding = m_targetFormatPanel.getAudioFormat().getEncoding();
			out("source format: " + sourceFormat);
			out("target encoding: " + targetEncoding);
			AudioInputStream	ais = new AudioInputStream(
				new ByteArrayInputStream(new byte[0]),
				sourceFormat,
				AudioSystem.NOT_SPECIFIED);
			try
			{
				AudioSystem.getAudioInputStream(targetEncoding, ais);
				outPossible(true);
			}
			catch (IllegalArgumentException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				outPossible(false);
			}
		}
	}



	private void outPossible(boolean bPossible)
	{
		if (bPossible)
		{
			out("-> possible");
		}
		else
		{
			out("-> not possible");
		}
	}



	private void out(String strMessage)
	{
		m_textArea.append(strMessage + "\n");
	}

} 



/*** ConverterTestPanel.java ***/
