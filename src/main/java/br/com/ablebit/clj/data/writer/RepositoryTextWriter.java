package br.com.ablebit.clj.data.writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.net.Packet;

/**
 * Insere pacotes no repositorio a partir de entrada na consolem - Texto.
 * 
 * @author leandro.franchi
 *
 */
public class RepositoryTextWriter implements Runnable {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(RepositoryTextWriter.class);

	/**
	 * Reporitorio que contem os pacotes a serem transmitidos.
	 */
	private Repository<Packet> repository;
	
	/**
	 * Auxiliar para contador de pacotes.
	 */
	private long packetCounter = 0;

	/**
	 * Construtor Padrao.
	 * 
	 * @param configuration
	 */
	public RepositoryTextWriter(Repository<Packet> repository) {
		setRepository(repository);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while(!Thread.currentThread().isInterrupted()) {
			
			try {
			
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        System.out.print("Enter String");
		        String s = br.readLine();
				
				Packet packet = new Packet(packetCounter++, s.getBytes());
				getRepository().put(packet);
				
			} catch(IOException e) {
				LOG.error(e.getMessage(), e);
			}
			
		}
			
	}

	public Repository<Packet> getRepository() {
		return repository;
	}

	public void setRepository(Repository<Packet> repository) {
		this.repository = repository;
	}

	public long getPacketCounter() {
		return packetCounter;
	}

	public void setPacketCounter(long packetCounter) {
		this.packetCounter = packetCounter;
	}
	
}
