package br.com.ablebit.clj.audio;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationLoader;
import br.com.ablebit.clj.config.ConfigurationProperty;

public class AudioRecorder {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(AudioRecorder.class);

	static final long RECORD_TIME = 10000;

	File wavFile = new File("RecordAudio.wav");

	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	TargetDataLine targetDataLine;

	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {

		Configuration configuration = null;

		try {
			configuration = ConfigurationLoader.loadConfiguration();
		} catch (Exception e) {
			LOG.error(e);
			System.exit(-1);
		}

		float sampleRate = Float.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLERATE));
		int sampleSize = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLESIZE));
		int channels = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_CHANELS));
		boolean signed = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SIGNED));
		boolean bigEndian = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_BIGENDIAN));

		return new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
	}

	/**
	 * Captures the sound and record into a WAV file
	 */
	void start() {

		try {

			AudioFormat format = getAudioFormat();

			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(format, targetDataLine.getBufferSize());
			targetDataLine.start();

			if (!AudioSystem.isLineSupported(info)) {
				LOG.info("Line not supported");
				System.exit(0);
			}

			LOG.info("Start capturing...");

			AudioInputStream ais = new AudioInputStream(targetDataLine);

			LOG.info("Start recording...");

			AudioSystem.write(ais, fileType, wavFile);

		} catch (Exception ex) {
			LOG.error(ex);
		}
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		targetDataLine.stop();
		targetDataLine.close();
		LOG.info("Finished");
	}

	/**
	 * Entry to run the program
	 */
	public static void main(String[] args) {

		final AudioRecorder recorder = new AudioRecorder();

		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(RECORD_TIME);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				recorder.finish();
			}
		});

		stopper.start();

		recorder.start();
	}

}
