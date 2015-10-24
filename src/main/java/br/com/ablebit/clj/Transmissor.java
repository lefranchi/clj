package br.com.ablebit.clj;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationLoader;
import br.com.ablebit.clj.config.ConfigurationProperty;
import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.data.impl.PriorityRepository;
import br.com.ablebit.clj.data.writer.RepositoryAudioWriter;
import br.com.ablebit.clj.net.NetworkUtil;
import br.com.ablebit.clj.net.Packet;
import br.com.ablebit.clj.net.TransmissorSocketProcessor;
import br.com.ablebit.clj.thread.NamedThreadFactory;

public class Transmissor {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Transmissor.class);

	/**
	 * Executor de processos.
	 */
	private static ExecutorService repositoryExecutorService;
	
	/**
	 * Executor para leitura de sockets (NoBlocking IO).
	 */
	private static ExecutorService socketExecutorService;
	
	/**
	 * Lista de transmissores.
	 */
	private static List<TransmissorSocketProcessor> transmissorSocketProcessors;

	public static void main(String[] args) {

		LOG.info("Inicializando Transmissor...");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				
				LOG.info("Finalizando Transmissor...");
				
				try {

					for (TransmissorSocketProcessor transmissorSocketProcessor : transmissorSocketProcessors) {
						transmissorSocketProcessor.disconnect();
					}

					if(socketExecutorService!=null)
						socketExecutorService.shutdownNow();
					
					if(repositoryExecutorService!=null)
						repositoryExecutorService.shutdownNow();
					
				} catch(Exception e) {
					LOG.error("Erro na finalização do Transmissor.", e);
				}
				
				LOG.info("Transmissor Finalizado!");
				
			}
		});

		Configuration configuration = null;
		try {
			configuration = ConfigurationLoader.loadConfiguration();
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento de configurações.", e);
			System.exit(-1);
		}

		Repository<Packet> repository = loadRepository();
		
		initPacketTransmission(repository);

		try {
			loadRepositoryWriter(configuration, repository);
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento do escritor de repositorio.", e);
			System.exit(-1);
		}
		
		
		List<InetAddress> addresses = null;
		try {
			addresses = loadLocalInterfaces();
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento das interfaces locais.", e);
			System.exit(-1);
		}
		
		loadSockets(configuration, repository, addresses);

	}

	private static void initPacketTransmission(Repository<Packet> repository) {
		
		LOG.info("Criando pacote de inicializacao...");
		
		Packet packet = new Packet(Packet.TYPE_INIT, 0, "".getBytes());
		repository.put(packet);
		
		LOG.info("Pacote de inicializacao criado com sucesso.");
		
	}

	private static void loadSockets(Configuration configuration, Repository<Packet> repository, List<InetAddress> addresses) {
		
		LOG.info("Carregando sockets para transmissao...");
		
		transmissorSocketProcessors = new ArrayList<>(addresses.size());
		
		socketExecutorService = Executors.newCachedThreadPool(new NamedThreadFactory("transmissor-socket"));

		String remoteReceptorIp = configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_REMOTE_RECEPTOR_IP);
		int remoteReceptorPort = Integer.parseInt(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_REMOTE_RECEPTOR_PORT));

		int localPort = remoteReceptorPort+1;
		
		for(InetAddress address : addresses) {
			
			TransmissorSocketProcessor transmissorSocketProcessor = new TransmissorSocketProcessor(repository, address, localPort++, remoteReceptorIp, remoteReceptorPort);
			
			transmissorSocketProcessors.add(transmissorSocketProcessor);
			
			socketExecutorService.submit(transmissorSocketProcessor);
		
			LOG.info(String.format("Interface carregada e operando em modo de transmissão[%s]. ", transmissorSocketProcessor));
		
		}
		
		LOG.info(String.format("Sockets[total:%d] para transmissao carregados com sucesso.", addresses.size()));
		
	}

	private static List<InetAddress> loadLocalInterfaces() throws Exception {
		
		LOG.info("Carregando interfaces locais...");
		
		List<InetAddress> addresses = NetworkUtil.listInetAddress();

		LOG.info("Interfaces locais carregadas com sucesso.");
		
		return addresses;
	}

	private static void loadRepositoryWriter(Configuration configuration, Repository<Packet> repository) throws Exception {
		
		LOG.info("Carregando escritor de repositorio...");
		
		repositoryExecutorService = Executors.newSingleThreadExecutor();

		float sampleRate = Float.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLERATE));
		int sampleSize = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLESIZE));
		int channels = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_CHANELS));
		boolean signed = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SIGNED));
		boolean bigEndian = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_BIGENDIAN));
		
		repositoryExecutorService.execute(new RepositoryAudioWriter(repository, sampleRate, sampleSize, channels, signed, bigEndian));
		
		LOG.info(String.format("Escritor de Repositorio carregado com sucesso RepositoryAudioWriter[sampleRate:%f,sampleSize:%d,chanels:%d,signed:%b,bigEndian:%b].", sampleRate, sampleSize, channels, signed, bigEndian));

	}

	private static Repository<Packet> loadRepository() {
		
		LOG.info("Carregando repositorio para transmissao...");
		
		Repository<Packet> repository = new PriorityRepository<>();
		
		LOG.info("Repositorio para transmissao carregado com sucesso.");
		
		return repository;
	}

}
