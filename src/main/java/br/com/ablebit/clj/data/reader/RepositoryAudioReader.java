package br.com.ablebit.clj.data.reader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.net.Packet;


/**
 * Le pacotes do Buffer Central e faz PlayBack.
 * 
 * @author leandro.franchi
 *
 */
public class RepositoryAudioReader implements Runnable {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(RepositoryAudioReader.class);
	
	/**
	 * Source DataLine.
	 */
	private SourceDataLine sourceDataLine;
	
	/**
	 * Audio Format.
	 */
	private AudioFormat audioFormat;
	
	/**
	 * Repositorio de onde serao lidos os pacotes recebidos e enviados a saida de audio.
	 */
	private Repository<Packet> repository;
	
	/**
	 * Controle de play. Nao pode tocar menor que o ultimo tocado;
	 */
	private long maxPacketCounter;

	/**
	 * Construtor Padrao. Ja inicializa Mixers e Lines de Audio de acordo a configuracao.
	 * 
	 * @param repository
	 * @throws Exception 
	 * 
	 */
	public RepositoryAudioReader(Repository<Packet> repository, float sampleRate, int sampleSize, int channels, boolean signed, boolean bigEndian) throws Exception {
		
		this.setRepository(repository);
		
		this.audioFormat = new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
			
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.audioFormat);
		sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(this.audioFormat, sourceDataLine.getBufferSize());
		sourceDataLine.start();
            
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while(!Thread.currentThread().isInterrupted()) {
			
			try {
				
				Packet packet = getRepository().take();
				
				if (packet.getType() == Packet.TYPE_INIT) {
				
					LOG.info("Iniciando/Reiniciando transmissao.");
					
					maxPacketCounter = 0; 
					
				} else {
				
					if (packet.getCounter() > maxPacketCounter) {
						LOG.debug("LENDO==" + packet.getCounter() + " - Tamanho: " + packet.getContent().length );
						sourceDataLine.write(packet.getContent(), 0 , packet.getContent().length); 
						maxPacketCounter = packet.getCounter();
					} else {
						 LOG.info("DESCARTANDO==" + packet.getCounter() + " - Tamanho: " + packet.getContent().length );					
					}
					
				}
				
			} catch (InterruptedException e) {
				LOG.fatal("Erro na leitura no repositorio!", e);
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
