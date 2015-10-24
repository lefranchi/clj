package br.com.ablebit.clj;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationLoader;
import br.com.ablebit.clj.config.ConfigurationProperty;
import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.data.impl.BufferedRepository;
import br.com.ablebit.clj.data.reader.RepositoryAudioReader;
import br.com.ablebit.clj.net.Packet;
import br.com.ablebit.clj.net.ReceptorSocketProcessor;
import br.com.ablebit.clj.thread.NamedThreadFactory;

/**
 * Servidor de Recepcao.
 * 
 * @author leandro.franchi
 *
 */
public class Receptor {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Receptor.class);

	/**
	 * Executor de processos.
	 */
	private static ExecutorService repositoryExecutorService;

	/**
	 * Server Socket.
	 */
	private static ServerSocket serverSocket;

	/**
	 * Executor para leitura de sockets (NoBlocking IO).
	 */
	private static ExecutorService socketExecutorService;

	public static void main(String[] args) {

		LOG.info("Inicializando Receptor...");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				
				LOG.info("Finalizando Receptor...");
				
				try {
				
					if(serverSocket!=null)
						serverSocket.close();
					
					if(socketExecutorService!=null)
						socketExecutorService.shutdownNow();
					
					if(repositoryExecutorService!=null)
						repositoryExecutorService.shutdownNow();
					
				} catch(Exception e) {
					LOG.error("Erro na finalização do Receptor.", e);
				}
				
				LOG.info("Receptor Finalizado!");
				
			}
			
		});

		Configuration configuration = null;
		try {
			configuration = ConfigurationLoader.loadConfiguration();
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento de configurações.", e);
			System.exit(-1);
		}

		Repository<Packet> repository = loadRepository(configuration);

		try {
			loadRepositoryReader(configuration, repository);
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento do leitor de repositorio.", e);
			System.exit(-1);
		}

		try {
			loadSocket(configuration);
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento do socket.", e);
			System.exit(-1);
		}

		socketExecutorService = Executors.newCachedThreadPool(new NamedThreadFactory("cl-receptor-socket"));

		LOG.info("Escutando...");

		while (!Thread.currentThread().isInterrupted()) {
			
			try {
				Socket socket = serverSocket.accept();
				socketExecutorService.submit(new ReceptorSocketProcessor(socket, repository));
			} catch (SocketException e) {
				if(e.getMessage().contains("Socket closed"))
					break;
			} catch (IOException e) {
				LOG.error("Erro em accept packet.", e);
			}
		}

	}

	private static void loadSocket(Configuration configuration) throws Exception {
		
		LOG.info("Carregando socket para recepcao...");
		
		String receptorIp = configuration.getConfiguration(ConfigurationProperty.RECEPTOR_IP);
		int port = Integer.parseInt(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_PORT));
		serverSocket = new ServerSocket(port, 0, InetAddress.getByName(receptorIp));
		
		LOG.info(String.format("Socket[%s:%d] para recepcao carregado com sucesso.", receptorIp, port));
			
	}

	private static void loadRepositoryReader(Configuration configuration, Repository<Packet> repository) throws Exception {
		
		LOG.info("Carregando leitor de repositorio...");
		
		repositoryExecutorService = Executors.newSingleThreadExecutor();

		float sampleRate = Float.valueOf(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_AUDIO_SAMPLERATE));
		int sampleSize = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_AUDIO_SAMPLESIZE));
		int channels = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_AUDIO_CHANELS));
		boolean signed = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_AUDIO_SIGNED));
		boolean bigEndian = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_AUDIO_BIGENDIAN));
		
		repositoryExecutorService.execute(new RepositoryAudioReader(repository, sampleRate, sampleSize, channels, signed, bigEndian));
		
		LOG.info(String.format("Leitor de Repositorio carregado com sucesso RepositoryAudioReader[sampleRate:%f,sampleSize:%d,chanels:%d,signed:%b,bigEndian:%b].", sampleRate, sampleSize, channels, signed, bigEndian));
		
	}

	private static Repository<Packet> loadRepository(Configuration configuration) {
		
		LOG.info("Carregando repositorio para recepcao...");
		
		Integer bufferSize = new Integer(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_REPOSITORY_BUFFER_SIZE));
		Integer repositoryDelay = new Integer(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_REPOSITORY_DELAY));
		Repository<Packet> repository = new BufferedRepository<>(bufferSize, repositoryDelay);
		
		LOG.info(String.format("Repositorio para recepcao[bufferSize:%d, repositoryDelay:%d] carregado com sucesso.", bufferSize, repositoryDelay) );
		
		return repository;
	}


}
