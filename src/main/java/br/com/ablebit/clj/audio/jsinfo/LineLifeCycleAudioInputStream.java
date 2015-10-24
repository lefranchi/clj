/*
 *	LineLifeCycleAudioInputStream.java
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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;



public class LineLifeCycleAudioInputStream
extends AudioInputStream
{
	private AudioFormat	m_audioFormat;
	private float[]		m_afBuffer;
	private	byte[]		m_abBuffer;
	private int		m_nBufferPointer;

	public LineLifeCycleAudioInputStream(AudioFormat audioFormat)
	{
		super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
		m_audioFormat = audioFormat;

		///////////////////////////////
		byte[]		abData;
		AudioFormat		m_format;
		int		nSignalType = 1;	// 1 = sine, 2 = square
		float		fAmplitude = 20000.0F;	// [0..32767]
		int		nSampleFrequency = (int) m_audioFormat.getFrameRate();
		int		nSignalFrequency = 1000;
		// float	fMaximumBufferLengthInSeconds = 1.0F;

		// int	nMaximumBufferLengthInFrames = (int) (fMaximumBufferLengthInSeconds * nSampleFrequency);
		// length of one period in frames
		int nPeriodLengthInFrames = nSampleFrequency / nSignalFrequency;
/*
		// make even
		if ((nPeriodLengthInFrames % 2) != 0)
		{
			nPeriodLengthInFrames++;
		}
*/
		int nNumPeriodsInBuffer = 1;
		int nNumFramesInBuffer = nNumPeriodsInBuffer * nPeriodLengthInFrames;
		int nBufferLength = nNumFramesInBuffer * 4;
		m_afBuffer = new float[nPeriodLengthInFrames];
		for (int nFrame = 0; nFrame < nPeriodLengthInFrames; nFrame++)
		{
			float	fValue = 0;
			switch (nSignalType)
			{
			case 1:	// sine
				fValue = (float) (Math.sin(((double)nFrame / (double) nPeriodLengthInFrames) * 2.0 * Math.PI) * fAmplitude);
				break;
			case 2:	// square
				if (nFrame < nPeriodLengthInFrames / 2)
				{
					fValue = fAmplitude;
				}
				else
				{
					fValue = -fAmplitude;
				}
			}
			m_afBuffer[nFrame] = fValue;
		}
		int	nByteBufferLengthInBytes = nBufferLength * m_audioFormat.getFrameSize();
		m_abBuffer = new byte[nByteBufferLengthInBytes];
		int	nBytePointer = 0;
		for (int nFrame = 0; nFrame < nPeriodLengthInFrames; nFrame++)
		{
			float	fSample = m_afBuffer[nFrame];
			int	nSample = (int) fSample;
			for (int nChannel = 0; nChannel < m_audioFormat.getChannels(); nChannel++)
			{
				if (m_audioFormat.isBigEndian())
				{
					m_abBuffer[nBytePointer] = (byte) ((nSample >> 8) & 0xFF);
					nBytePointer++;
					m_abBuffer[nBytePointer] = (byte) (nSample & 0xFF);
					nBytePointer++;
				}
				else	// little endian
				{
					m_abBuffer[nBytePointer] = (byte) (nSample & 0xFF);
					nBytePointer++;
					m_abBuffer[nBytePointer] = (byte) ((nSample >> 8) & 0xFF);
					nBytePointer++;
				}
			}
		}
		m_nBufferPointer = 0;
	}


	private void advanceBufferPointer(int nAmount)
	{
		m_nBufferPointer += nAmount;
		if (m_nBufferPointer >= m_abBuffer.length)
		{
			m_nBufferPointer = 0;
		}
	}



	public int read()
	{
		int	nResult = m_abBuffer[m_nBufferPointer];
		advanceBufferPointer(1);
		return nResult;
	}



	public int read(byte[] abData, int nOffset, int nLength)
	{
		int	nRemainingLength = nLength;
		while (nRemainingLength > 0)
		{
			int	nAvailable = m_abBuffer.length - m_nBufferPointer;
			int	nCopyLength = Math.min(nAvailable, nRemainingLength);
			System.arraycopy(m_abBuffer, m_nBufferPointer, abData, nOffset, nCopyLength);
			advanceBufferPointer(nCopyLength);
			nOffset += nCopyLength;
			nRemainingLength -= nCopyLength;
		}
		return nLength;
	}


} 



/*** LineLifeCycleAudioInputStream.java ***/
