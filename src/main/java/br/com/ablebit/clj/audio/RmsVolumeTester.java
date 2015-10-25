package br.com.ablebit.clj.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationLoader;
import br.com.ablebit.clj.config.ConfigurationProperty;
import br.com.ablebit.clj.data.writer.RepositoryAudioWriter;

public class RmsVolumeTester {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(RepositoryAudioWriter.class);

	public static void main(String[] args) {
		
		Configuration configuration = null;
		try {
			configuration = ConfigurationLoader.loadConfiguration();
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento de configurações.", e);
			System.exit(-1);
		}

		float sampleRate = Float.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLERATE));
		int sampleSize = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLESIZE));
		int channels = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_CHANELS));
		boolean signed = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SIGNED));
		boolean bigEndian = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_BIGENDIAN));

		TargetDataLine targetDataLine = null;
		AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat, targetDataLine.getBufferSize());
			targetDataLine.start();
		} catch (LineUnavailableException e) {
			LOG.fatal("Erro no carregamento de TargetDataline.", e);
			System.exit(-1);
		}

		int frameSizeInBytes = audioFormat.getFrameSize();
        int bufferLengthInFrames = targetDataLine.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];

		while(!Thread.currentThread().isInterrupted()) {
			
			if(targetDataLine.read(data, 0, bufferLengthInBytes) != -1) {

				byte packetBuffer[] = new byte[bufferLengthInBytes];
				System.arraycopy(data, 0, packetBuffer, 0, bufferLengthInBytes);

				LOG.info(AudioUtils.volumeRMS(packetBuffer));
				
	        }
			
		}
		
	}
	
}
