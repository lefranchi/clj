package br.com.ablebit.clj.net;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;

/**
 * Classe receptora de pacotes TCP - NonBlocking IO.
 * 
 * @author lfranchi
 *
 */
public class ReceptorSocketProcessor implements Callable<Boolean> {

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
	public Boolean call() throws Exception {
		
		LOG.info("Rebendo mensagem de " + socket.getRemoteSocketAddress());

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
		
		return true;
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
