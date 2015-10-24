/*
 *	LineTestPanel.java
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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;



public class LineTestPanel
extends JPanel
implements ActionListener
{
	private ButtonGroup		m_typeButtonGroup;
	private JRadioButton		m_sdlRadioButton;
	private JRadioButton		m_tdlRadioButton;
	private JRadioButton		m_clipRadioButton;
	private ButtonGroup		m_bufferSizeButtonGroup;
	private JRadioButton		m_defaultRadioButton;
	private JRadioButton		m_notspecifiedRadioButton;
	private JRadioButton		m_customRadioButton;
	private JTextField		m_bufferSizeTextField;
	private AudioFormatPanel	m_sourceFormatPanel;



	public LineTestPanel()
	{
		JPanel	centerPanel = new JPanel();
		setLayout(new BorderLayout());

		JPanel	typePanel = new JPanel();
		typePanel.setLayout(new GridLayout(0, 1));
		typePanel.setBorder(new TitledBorder(new EtchedBorder(), "Line Type"));
		m_typeButtonGroup = new ButtonGroup();
		m_sdlRadioButton = new JRadioButton("SourceDataLine");
		typePanel.add(m_sdlRadioButton);
		m_typeButtonGroup.add(m_sdlRadioButton);
		m_tdlRadioButton = new JRadioButton("TargetDataLine");
		typePanel.add(m_tdlRadioButton);
		m_typeButtonGroup.add(m_tdlRadioButton);
		m_clipRadioButton = new JRadioButton("Clip");
		typePanel.add(m_clipRadioButton);
		m_typeButtonGroup.add(m_clipRadioButton);
		centerPanel.add(typePanel);

		JPanel	bufferSizePanel = new JPanel();
		bufferSizePanel.setLayout(new GridLayout(0, 1));
		bufferSizePanel.setBorder(new TitledBorder(new EtchedBorder(), "Buffer Size"));
		m_bufferSizeButtonGroup = new ButtonGroup();
		m_defaultRadioButton = new JRadioButton("Default (no argument)");
		bufferSizePanel.add(m_defaultRadioButton);
		m_bufferSizeButtonGroup.add(m_defaultRadioButton);
		m_notspecifiedRadioButton = new JRadioButton("AudioSystem.NOT_SPECIFIED");
		bufferSizePanel.add(m_notspecifiedRadioButton);
		m_bufferSizeButtonGroup.add(m_notspecifiedRadioButton);
		m_customRadioButton = new JRadioButton("Custom:");
		bufferSizePanel.add(m_customRadioButton);
		m_bufferSizeButtonGroup.add(m_customRadioButton);
		m_bufferSizeTextField = new JTextField(6);
		bufferSizePanel.add(m_bufferSizeTextField);
		centerPanel.add(bufferSizePanel);

		m_sourceFormatPanel = new AudioFormatPanel();
		m_sourceFormatPanel.setBorder(new TitledBorder(new EtchedBorder(), "Format"));
		centerPanel.add(m_sourceFormatPanel);

		add(centerPanel, BorderLayout.CENTER);

		JPanel	buttonPanel = new JPanel(new GridLayout(0, 1));

		JButton	testButton = new JButton("Test: isLineSupported()");
		testButton.addActionListener(this);
		testButton.setActionCommand("test1");
		buttonPanel.add(testButton);

		testButton = new JButton("Test: getLine()");
		testButton.addActionListener(this);
		testButton.setActionCommand("test2");
		buttonPanel.add(testButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}



	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("test1"))
		{
			Line.Info	lineInfo = constructLineInfo();
			JSInfoDebug.out("line info: " + lineInfo);
			JSInfoDebug.out("possible: " + AudioSystem.isLineSupported(lineInfo));
			
			try {
				((SourceDataLine) AudioSystem.getLine(lineInfo)).open();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if (strActionCommand.equals("test2"))
		{
			Line.Info	lineInfo = constructLineInfo();
			JSInfoDebug.out("line info: " + lineInfo);
			try
			{
				Line	line = AudioSystem.getLine(lineInfo);
				JSInfoDebug.out("possible: " + true);
				line.close();
			}
			catch (IllegalArgumentException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				JSInfoDebug.out("possible: " + false);
			}
			catch (LineUnavailableException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				JSInfoDebug.out("possible: " + true + " (but currently unavailable)");
			}
		}
	}



	private Line.Info constructLineInfo()
	{
		AudioFormat	format = m_sourceFormatPanel.getAudioFormat();
		if (isDefaultBufferSizeDesired())
		{
			return new DataLine.Info(getDataLineType(), format);
		}
		else
		{
			return new DataLine.Info(getDataLineType(), format,
						 getDesiredBufferSize());
		}
	}



	private Class getDataLineType()
	{
		if (m_sdlRadioButton.isSelected())
		{
			return SourceDataLine.class;
		}
		else if (m_tdlRadioButton.isSelected())
		{
			return TargetDataLine.class;
		}
		else if (m_clipRadioButton.isSelected())
		{
			return Clip.class;
		}
		else
		{
			return null;
		}
	}



	private boolean isDefaultBufferSizeDesired()
	{
		return m_defaultRadioButton.isSelected();
	}



	private int getDesiredBufferSize()
	{
		if (m_defaultRadioButton.isSelected())
		{
			// some sort of error indication; this case shouldn't happen
			return -100;
		}
		else if (m_notspecifiedRadioButton.isSelected())
		{
			return AudioSystem.NOT_SPECIFIED;
		}
		else if (m_customRadioButton.isSelected())
		{
			String	strBufferSize = m_bufferSizeTextField.getText();
			int	nBufferSize = Integer.parseInt(strBufferSize);
			return nBufferSize;
		}
		else
		{
			// still another sort of error
			return -200;
		}
	}
} 



/*** LineTestPanel.java ***/
