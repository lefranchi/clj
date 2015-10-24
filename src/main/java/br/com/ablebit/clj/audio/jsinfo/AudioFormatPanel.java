/*
 *	AudioFormatPanel.java
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import javax.sound.sampled.AudioFormat;



public class AudioFormatPanel
extends JPanel
implements ActionListener
{
	private static final boolean		RATE_MAGIC_DEFAULT = true;
	private static final boolean		FRAME_SIZE_MAGIC_DEFAULT = true;

	public static final AudioFormat.Encoding[]	m_aEncodings =
	{
		AudioFormat.Encoding.PCM_SIGNED,
		AudioFormat.Encoding.PCM_UNSIGNED,
		AudioFormat.Encoding.ALAW,
		AudioFormat.Encoding.ULAW,
	};


	private static final AudioFormat.Encoding[]	m_aMagicSupportingEncodings =
	{
		AudioFormat.Encoding.PCM_SIGNED,
		AudioFormat.Encoding.PCM_UNSIGNED,
		AudioFormat.Encoding.ALAW,
		AudioFormat.Encoding.ULAW,
	};


	public static final String[]		sm_aDefaultRates =
	{
		"96000.0",
		"88200.0",
		"48000.0",
		"44100.0",
		"32000.0",
		"24000.0",
		"22050.0",
		"16000.0",
		"12000.0",
		"11025.0",
		"8000.0",
		"-1"
	};


	public static final String[]		sm_aDefaultSampleSizes =
	{
		"8",
		"16",
		"24",
		"32",
		"-1"
	};


	public static final String[]		sm_aDefaultChannels =
	{
		"1",
		"2",
		"-1"
	};


	public static final String[]		sm_aDefaultFrameSizes =
	{
		"1",
		"2",
		"3",
		"4",
		"6",
		"8",
		"-1"
	};


	private JComboBox		m_encodingComboBox;
	private JComboBox		m_sampleRateComboBox;
	private JComboBox		m_sampleSizeComboBox;
	private JComboBox		m_channelsComboBox;
	private JComboBox		m_frameSizeComboBox;
	private JComboBox		m_frameRateComboBox;
	private JToggleButton		m_bigEndianToggleButton;
	private JToggleButton		m_rateMagicToggleButton;
	private JToggleButton		m_frameSizeMagicToggleButton;



	public AudioFormatPanel()
	{
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Encoding"));
		String[]	astrEncodingNames = new String[m_aEncodings.length];
		for (int i = 0; i < m_aEncodings.length; i++)
		{
			astrEncodingNames[i] = m_aEncodings[i].toString();
		}
		m_encodingComboBox = new JComboBox(astrEncodingNames);
		add(m_encodingComboBox);
		add(new JLabel("Sample Rate"));
		m_sampleRateComboBox = new JComboBox(sm_aDefaultRates);
		m_sampleRateComboBox.setEditable(true);
		m_sampleRateComboBox.addActionListener(this);
		add(m_sampleRateComboBox);
		add(new JLabel("Sample Size"));
		m_sampleSizeComboBox = new JComboBox(sm_aDefaultSampleSizes);
		m_sampleSizeComboBox.setEditable(true);
		m_sampleSizeComboBox.addActionListener(this);
		add(m_sampleSizeComboBox);
		add(new JLabel("Channels"));
		m_channelsComboBox = new JComboBox(sm_aDefaultChannels);
		m_channelsComboBox.setEditable(true);
		m_channelsComboBox.addActionListener(this);
		add(m_channelsComboBox);
		add(new JLabel("Frame Size"));
		m_frameSizeComboBox = new JComboBox(sm_aDefaultFrameSizes);
		m_frameSizeComboBox.setEditable(true);
		add(m_frameSizeComboBox);
		add(new JLabel("Frame Rate"));
		m_frameRateComboBox = new JComboBox(sm_aDefaultRates);
		m_frameRateComboBox.setEditable(true);
		m_frameRateComboBox.addActionListener(this);
		add(m_frameRateComboBox);
		add(new JLabel("Endianess"));
		m_bigEndianToggleButton = new JToggleButton("Big Endian");
		add(m_bigEndianToggleButton);
		m_rateMagicToggleButton = new JCheckBox("Rate Magic", RATE_MAGIC_DEFAULT);
		add(m_rateMagicToggleButton);
		m_frameSizeMagicToggleButton = new JCheckBox("Frame Size Magic", FRAME_SIZE_MAGIC_DEFAULT);
		add(m_frameSizeMagicToggleButton);
	}



	public AudioFormat getAudioFormat()
	{
		return new AudioFormat(
			m_aEncodings[m_encodingComboBox.getSelectedIndex()],
			parseFloat((String) m_sampleRateComboBox.getSelectedItem()),
			parseInt((String) m_sampleSizeComboBox.getSelectedItem()),
			parseInt((String) m_channelsComboBox.getSelectedItem()),
			parseInt((String) m_frameSizeComboBox.getSelectedItem()),
			parseFloat((String) m_frameRateComboBox.getSelectedItem()),
			m_bigEndianToggleButton.isSelected());
	}



	private static float parseFloat(String strText)
	{
		try
		{
			return Float.parseFloat(strText);
		}
		catch (NumberFormatException e)
		{
			if (JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
			return 0.0F;
		}
	}



	private static int parseInt(String strText)
	{
		try
		{
			return Integer.parseInt(strText);
		}
		catch (NumberFormatException e)
		{
			if (JSInfoDebug.getTraceAllExceptions())
			{
				JSInfoDebug.out(e);
			}
			return 0;
		}
	}



	private boolean getRateMagic()
	{
		return m_rateMagicToggleButton.isSelected();
	}



	private boolean getFrameSizeMagic()
	{
		return m_frameSizeMagicToggleButton.isSelected();
	}



	public void actionPerformed(ActionEvent ae)
	{
		Object	source = ae.getSource();
		if (getRateMagic())
		{
			if (source == m_sampleRateComboBox)
			{
				Object	obj = m_sampleRateComboBox.getSelectedItem();
				m_frameRateComboBox.setSelectedItem(obj);
			}
			else if (source == m_frameRateComboBox)
			{
				Object	obj = m_frameRateComboBox.getSelectedItem();
				m_sampleRateComboBox.setSelectedItem(obj);
			}
		}
		if (getFrameSizeMagic())
		{
			if (source == m_sampleSizeComboBox
			    || source == m_channelsComboBox)
			{
				calculateAndSetFrameSize();
			}
		}
	}


	private void calculateAndSetFrameSize()
	{
		int	nSampleSize = parseInt((String) m_sampleSizeComboBox.getSelectedItem());
		int	nChannels = parseInt((String) m_channelsComboBox.getSelectedItem());
		int	nFrameSize = ((nSampleSize + 7) / 8) * nChannels;
		m_frameSizeComboBox.setSelectedItem("" + nFrameSize);
	}

} 



/*** AudioFormatPanel.java ***/
