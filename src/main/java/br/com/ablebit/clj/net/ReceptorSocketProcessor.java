package br.com.ablebit.clj.net;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;

/**
 * Classe receptora de pacotes TCP - NonBlocking IO.
 * 
 * @author lfranchi
 *
 */
public class ReceptorSocketProcessor implements Runnable {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(ReceptorSocketProcessor.class);

	/**
	 * Socket receptor da mensagem.
	 */
	private Socket socket;
	
	/**
	 * Repositorio de dados.
	 */
	private Repository<Packet> repository;
			
	/**
	 * Contrutor padrao.
	 * 
	 * @param socket Socket receptor da mensagem.
	 */
	public ReceptorSocketProcessor(Socket socket, Repository<Packet> repository) { 
		super();
		this.socket = socket;
		this.repository = repository;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public void run() {
		
		LOG.info("Rebendo mensagem de " + socket.getRemoteSocketAddress());

		try {
		
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			//TODO: Testar necessidade deste while!
			while(!Thread.interrupted()) {
	
				Packet packet = (Packet) objectInputStream.readObject();
				packet.setSource(socket.getRemoteSocketAddress().toString());
				
				getRepository().put(packet);
				
			}
		
			//objectInputStream.close();
			//inputStream.close();
			//socket.close();
			
		} catch(Exception e) {
			LOG.error(e);
		}
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Repository<Packet> getRepository() {
		return repository;
	}

	public void setRepository(Repository<Packet> repository) {
		this.repository = repository;
	}

	
}
