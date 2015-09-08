package br.com.ablebit.clj.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;

/**
 * Classe que envia pacotes TCP.
 * 
 * @author lfranchi
 *
 */
public class TransmissorSocketProcessor implements Callable<Boolean> {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(TransmissorSocketProcessor.class);

	/**
	 * Repositorio dos dados.
	 */
	private Repository<Packet> repository;
	
	/**
	 * Porta Local.
	 */
	private int localPort;
	
	/**
	 * Ip remoto.
	 */
	private String remoteReceptorIp;
	
	/**
	 * Porta remota.
	 */
	private int remoteReceptorPort;
	
	/**
	 * Interface de Rede Fisica.
	 * 
	 * @return
	 */
	private NetworkInterface networkInterface;

	/**
	 * Socket.
	 */
	private Socket socket;
	
	/**
	 * Socket out.
	 */
	private ObjectOutputStream socketOut;
	
	/**
	 *  Construtor Padrao.
	 */
	public TransmissorSocketProcessor(Repository<Packet> repository, NetworkInterface networkInterface, int localPort, String remoteReceptorIp, int remoteReceptorPort) {
		this.repository = repository;
		this.networkInterface = networkInterface;
		this.localPort = localPort;
		this.remoteReceptorIp = remoteReceptorIp;
		this.remoteReceptorPort = remoteReceptorPort;
	}

	@Override
	public Boolean call() throws Exception {
		
		connect();
		
		while (!Thread.currentThread().isInterrupted()) {
			
			Packet packet = repository.take();
			
			LOG.info("ENVIANDO: " + packet.getCounter() + " - " + packet.getContent().length);
			this.socketOut.writeObject(packet);
			
		}
		
		return null;
	}
	
	/**
	 * Efetua Conexão do Socket.
	 * 
	 * @throws Exception
	 */
	public void connect() throws Exception {
		
		setSocket(new Socket());
		
		try {

			getSocket().setTcpNoDelay(true);
			getSocket().setSoTimeout(5000);
			getSocket().setReuseAddress(true);
			getSocket().setKeepAlive(true);

			InetAddress inetAddress = (getRemoteReceptorIp().equals("127.0.0.1") ? InetAddress.getLocalHost() : getNetworkInterface().getInetAddresses().nextElement());
			getSocket().bind(new InetSocketAddress(inetAddress, getLocalPort()));
	
			getSocket().connect(new InetSocketAddress(getRemoteReceptorIp(), getRemoteReceptorPort()), getLocalPort());
			
			this.socketOut = new ObjectOutputStream(getSocket().getOutputStream());
			
		} catch(Exception e) {
			LOG.error("Erro conectando socket.", e);
		}
		
	}	
	
	/**
	 * Disconecta Soket. TODO: Chamar no shutdown do Executor.
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		
		this.socketOut.flush();
		this.socketOut.close();
		this.socketOut = null;
	
		this.getSocket().close();
		this.setSocket(null);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNetworkInterface().getName() + ":" + getLocalPort() + " -> " + remoteReceptorIp + ":" + remoteReceptorPort;
	}


	public String getRemoteReceptorIp() {
		return remoteReceptorIp;
	}

	public void setRemoteReceptorIp(String remoteReceptorIp) {
		this.remoteReceptorIp = remoteReceptorIp;
	}

	public int getRemoteReceptorPort() {
		return remoteReceptorPort;
	}

	public void setRemoteReceptorPort(int remoteReceptorPort) {
		this.remoteReceptorPort = remoteReceptorPort;
	}

	public NetworkInterface getNetworkInterface() {
		return networkInterface;
	}

	public void setNetworkInterface(NetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getSocketOut() {
		return socketOut;
	}

	public void setSocketOut(ObjectOutputStream socketOut) {
		this.socketOut = socketOut;
	}

	public Repository<Packet> getRepository() {
		return repository;
	}

	public void setRepository(Repository<Packet> repository) {
		this.repository = repository;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

}
