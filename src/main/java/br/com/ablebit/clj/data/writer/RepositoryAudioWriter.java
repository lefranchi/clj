package br.com.ablebit.clj.data.writer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.net.Packet;

/**
 * Insere pacotes no repositorio a partir de um device.
 * 
 * @author leandro.franchi
 *
 */
public class RepositoryAudioWriter implements Runnable {
	
	/**
	 * Logger.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(RepositoryAudioWriter.class);
	
	/**
	 * Audio Target DataLine.
	 */
	private TargetDataLine targetDataLine;
	
	/**
	 * Audio Format.
	 */
	private AudioFormat audioFormat;
	
	/**
	 * Reporitorio que contem os pacotes a serem transmitidos.
	 */
	private Repository<Packet> repository;
	
	/**
	 * Auxiliar para contador de pacotes.
	 */
	private long packetCounter = 0;
	
	/**
	 * Construtor Padrao. Ja inicializa Mixers e Lines de Audio de acordo a configuracao.
	 * 
	 * @param configuration
	 * @throws Exception 
	 */
	public RepositoryAudioWriter(Repository<Packet> repository, float sampleRate, int sampleSize, int channels, boolean signed, boolean bigEndian) throws Exception {

		setRepository(repository);
		
		this.audioFormat = new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
		targetDataLine.open(this.audioFormat, targetDataLine.getBufferSize());
		targetDataLine.start();
			
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while(!Thread.currentThread().isInterrupted()) {
			
			int frameSizeInBytes = this.audioFormat.getFrameSize();
	        int bufferLengthInFrames = targetDataLine.getBufferSize() / 8;
	        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
	        byte[] data = new byte[bufferLengthInBytes];
	        
			if(targetDataLine.read(data, 0, bufferLengthInBytes) != -1) {

				byte packetBuffer[] = new byte[bufferLengthInBytes];
				System.arraycopy(data, 0, packetBuffer, 0, bufferLengthInBytes);

				Packet packet = new Packet(packetCounter++, packetBuffer);
				getRepository().put(packet);
				
	        }
			
		}
			
	}


	public Repository<Packet> getRepository() {
		return repository;
	}


	public void setRepository(Repository<Packet> repository) {
		this.repository = repository;
	}

}
