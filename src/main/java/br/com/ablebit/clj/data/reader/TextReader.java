package br.com.ablebit.clj.data.reader;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.net.Packet;

/**
 * Le pacotes do Buffer Central e imprime texto na console.
 * 
 * @author leandro.franchi
 *
 */
public class TextReader implements Runnable {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(TextReader.class);

	
	/**
	 * Repositorio de onde serao lidos os pacotes recebidos e enviados a saida na console.
	 */
	private Repository<Packet> repository;

	/**
	 * Construtor Padrao.
	 * 
	 * @param repository
	 * 
	 */
	public TextReader(Repository<Packet> repository) {
		this.setRepository(repository);
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
				
				LOG.info("LENDO==" + packet.getCounter() + " - Conteudo: " + packet.getContent() + " - Tamanho: " + packet.getContent().length );
				
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
