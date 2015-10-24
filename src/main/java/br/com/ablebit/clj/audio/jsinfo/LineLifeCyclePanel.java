/*
 *	LineLifeCyclePanel.java
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;



public class LineLifeCyclePanel
extends JPanel
implements ActionListener,
	LineListener
{
	private AudioFormatPanel	m_audioFormatPanel;
	private JTextArea		m_textArea;
	private DataLine		m_line;
	private PushThread		m_thread;


	public LineLifeCyclePanel()
	{
		setLayout(new BorderLayout());

		JPanel	topPanel = new JPanel();

		JPanel	generalPanel = new JPanel();
		generalPanel.setBorder(new TitledBorder(new EtchedBorder(), "General"));
		m_audioFormatPanel = new AudioFormatPanel();
		generalPanel.add(m_audioFormatPanel);
		topPanel.add(generalPanel);

		JPanel	activityPanel = new JPanel();
		activityPanel.setBorder(new TitledBorder(new EtchedBorder(), "Data"));
		JButton	startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setActionCommand("startThread");
		activityPanel.add(startButton);
		startButton = new JButton("Stop");
		startButton.addActionListener(this);
		startButton.setActionCommand("stopThread");
		activityPanel.add(startButton);

		topPanel.add(activityPanel);
		JPanel	linePanel = new JPanel(new GridLayout(0, 1));
		linePanel.setBorder(new TitledBorder(new EtchedBorder(), "Line"));

		JButton	testPossibleButton = new JButton("Create");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("create");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Open");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("open");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Start");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("start");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Stop");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("stop");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Drain");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("drain");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Flush");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("flush");
		linePanel.add(testPossibleButton);

		testPossibleButton = new JButton("Close");
		testPossibleButton.addActionListener(this);
		testPossibleButton.setActionCommand("close");
		linePanel.add(testPossibleButton);

		topPanel.add(linePanel);

		add(topPanel, BorderLayout.NORTH);

		m_textArea = new JTextArea();
		m_textArea.setEditable(false);
		m_textArea.setLineWrap(true);
		m_textArea.setWrapStyleWord(true);
		JScrollPane	scrollPane = new JScrollPane(m_textArea);
		add(scrollPane, BorderLayout.CENTER);

	}



	private void addMessage(String strMessage)
	{
		m_textArea.append(strMessage);
	}



	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("create"))
		{
			addMessage("creating line\n");
			AudioFormat	format = m_audioFormatPanel.getAudioFormat();
			DataLine.Info	info = new DataLine.Info(SourceDataLine.class,
								 format);
			try
			{
				m_line = (DataLine) AudioSystem.getLine(info);
			}
			catch (LineUnavailableException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				outputException(e);
			}
			catch (IllegalArgumentException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				outputException(e);
			}
			m_line.addLineListener(this);
			m_thread = new PushThread(new LineLifeCycleAudioInputStream(format), (SourceDataLine) m_line);
			m_thread.start();
		}
		if (strActionCommand.equals("open"))
		{
			addMessage("opening line\n");
			AudioFormat	format = m_audioFormatPanel.getAudioFormat();
			try
			{
				if (m_line instanceof SourceDataLine)
				{
					((SourceDataLine) m_line).open(format);
				}
				else if (m_line instanceof TargetDataLine)
				{
					((TargetDataLine) m_line).open(format);
				}
				else
				{
					// ??
				}
			}
			catch (LineUnavailableException e)
			{
				if (JSInfoDebug.getTraceAllExceptions())
				{
					JSInfoDebug.out(e);
				}
				outputException(e);
			}
			int	nBufferSize = m_line.getBufferSize();
			addMessage("Buffer Size: " + nBufferSize + "\n");
		}
		else if (strActionCommand.equals("start"))
		{
			addMessage("starting line\n");
			m_line.start();
		}
		else if (strActionCommand.equals("stop"))
		{
			addMessage("stopping line\n");
			m_line.stop();
		}
		else if (strActionCommand.equals("flush"))
		{
			addMessage("flushing line\n");
			m_line.flush();
		}
		else if (strActionCommand.equals("drain"))
		{
			addMessage("draining line\n");
			m_line.drain();
		}
		else if (strActionCommand.equals("close"))
		{
			addMessage("closing line\n");
			m_line.close();
		}
		else if (strActionCommand.equals("startThread"))
		{
			addMessage("starting thread\n");
			m_thread.setRunning(true);
		}
		else if (strActionCommand.equals("stopThread"))
		{
			addMessage("stopping thread\n");
			m_thread.setRunning(false);
		}
	}



	public void update(LineEvent event)
	{
		addMessage("Received " + event.getType() + " event\n");
		addMessage("  at frame position: " + event.getFramePosition() + "\n");
	}



	/**	Outputs a Throwable's stacktrace to the text area.
	 */
	private void outputException(Throwable t)
	{
		ByteArrayOutputStream	baos = new ByteArrayOutputStream();
		PrintStream		ps = new PrintStream(baos);
		t.printStackTrace(ps);
		addMessage(baos.toString());
	}

	public class PushThread
extends Thread
	{
		private AudioInputStream	m_audioInputStream;
		private SourceDataLine		m_line;
		private boolean			m_bRunning;


		public PushThread(AudioInputStream audioInputStream,
				  SourceDataLine line)
		{
			m_audioInputStream = audioInputStream;
			m_line = line;
		}


		public void run()
		{
			byte[]	abBuffer = new byte[8192];
			m_bRunning = false;
			while (true)
			{
				while (m_bRunning)
				{
					try
					{
						int	nRead = m_audioInputStream.read(abBuffer);
						addMessage("Trying to write " + nRead + " bytes\n");
						int	nWritten = m_line.write(abBuffer, 0, nRead);
						addMessage("Written " + nRead + " bytes\n");
					}
					catch (IOException e)
					{
						if (JSInfoDebug.getTraceAllExceptions())
						{
							JSInfoDebug.out(e);
						}
					}
				}
				while (! m_bRunning)
				{
					synchronized (this)
					{
						try
						{
							this.wait();
						}
						catch (InterruptedException e)
						{
							if (JSInfoDebug.getTraceAllExceptions())
							{
								JSInfoDebug.out(e);
							}
						}
					}
				}
			}
		}

		public void setRunning(boolean bRunning)
		{
			m_bRunning = bRunning;
			synchronized (this)
			{
				this.notifyAll();
			}
		}


	}


} 



/*** LineLifeCyclePanel.java ***/
