package br.com.ablebit.clj.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.data.type.AtomicFloat;

/**
 * Classe que envia pacotes TCP.
 * 
 * @author lfranchi
 *
 */
public class TransmissorSocketProcessor implements Runnable {

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
	private InetAddress inetAddress;

	/**
	 * Socket.
	 */
	private Socket socket;
	
	/**
	 * Socket out.
	 */
	private ObjectOutputStream socketOut;
	
	/**
	 * Controle de desconexão.
	 */
	private transient AtomicBoolean disconnect;
	
	/**
	 * Total de Pacotes Enviados.
	 */
	private AtomicLong totalPacketSent = new AtomicLong(0);
	
	/**
	 * Total de Mb Enviados.
	 */
	private AtomicFloat totalPacketSentMb = new AtomicFloat(0);
	
	/**
	 * Largura da banda. Mbits/s.
	 */
	private double bandwidth;
	
	/**
	 *  Construtor Padrao.
	 */
	public TransmissorSocketProcessor(Repository<Packet> repository, InetAddress inetAddress, int localPort, String remoteReceptorIp, int remoteReceptorPort) {
		this.repository = repository;
		this.inetAddress = inetAddress;
		this.localPort = localPort;
		this.remoteReceptorIp = remoteReceptorIp;
		this.remoteReceptorPort = remoteReceptorPort;
		
		disconnect = new AtomicBoolean(false);
		
	}

	@Override
	public void run() {
		
		long lBegin = 0;
        long lEnd = 0;
        
		try {
		
			connect();
			
			while (!Thread.currentThread().isInterrupted() && !disconnect.get()) {
				
				Packet packet = repository.take();
				
				LOG.debug("ENVIANDO: " + packet.getCounter() + " - " + packet.getContent().length);
				lBegin = System.nanoTime();
				this.socketOut.writeObject(packet);
				lEnd = System.nanoTime();
				
				updateStatistics(packet, (lEnd - lBegin));
				
			}

		} catch(Exception e) {
			LOG.error(String.format("Falha no socket transmissor[%s].", this), e);
		}
		
	}

	/**
	 * Atualiza informacoes estatisticas.
	 * 
	 * @param packet
	 */
	private void updateStatistics(Packet packet, long elapsedTime) {
		
		double seconds = (double)elapsedTime / 1000000000.0;
		double mbperseconds = (packet.getContent().length/1024) / seconds; 
		setBandwidth(mbperseconds);
		
		getTotalPacketSent().set(packet.getCounter());
		
		getTotalPacketSentMb().set((getTotalPacketSent().get()*packet.getContent().length)/1024);
		
		LOG.debug(String.format("[%d:pacotes][%2fMb:dados][%.2f:Mbits/s] -> enviados pela interface %s.", getTotalPacketSent().longValue(), getTotalPacketSentMb().floatValue(), getBandwidth(), getInetAddress().getAddress().toString()));
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
			
			getSocket().bind(new InetSocketAddress(getInetAddress(), getLocalPort()));
	
			getSocket().connect(new InetSocketAddress(getRemoteReceptorIp(), getRemoteReceptorPort()), 5000);
			
			this.socketOut = new ObjectOutputStream(getSocket().getOutputStream());
			
		} catch(Exception e) {
			LOG.error("Erro conectando socket.", e);
		}
		
	}	
	
	/**
	 * Disconecta Soket. 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		
		if(disconnect!=null)
			disconnect.set(true);
		
		if(this.getSocket()!=null) {
			this.getSocket().shutdownInput();
			this.getSocket().shutdownOutput();
			this.getSocket().close();
			this.setSocket(null);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getInetAddress() + ":" + getLocalPort() + " -> " + remoteReceptorIp + ":" + remoteReceptorPort;
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

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	public AtomicLong getTotalPacketSent() {
		return totalPacketSent;
	}

	public void setTotalPacketSent(AtomicLong totalPacketSent) {
		this.totalPacketSent = totalPacketSent;
	}

	public AtomicFloat getTotalPacketSentMb() {
		return totalPacketSentMb;
	}

	public void setTotalPacketSentMb(AtomicFloat totalPacketSentMb) {
		this.totalPacketSentMb = totalPacketSentMb;
	}

	public double getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

}
