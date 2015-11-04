package br.com.ablebit.clj;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import br.com.ablebit.clj.ui.CLJDashboardFrame;

public class Transmissor {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Transmissor.class);

	/**
	 * Configurações Gerais.
	 */
	private static Configuration configuration;
	
	/**
	 * Executor de processos.
	 */
	private static ExecutorService servicesExecutorService;
	
	/**
	 * Executor de carga dos sockets
	 */
	private static ScheduledExecutorService scheduledSocketExecutorService;
	
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

					if(scheduledSocketExecutorService!=null)
						scheduledSocketExecutorService.shutdownNow();

					if(socketExecutorService!=null)
						socketExecutorService.shutdownNow();
					
					if(servicesExecutorService!=null)
						servicesExecutorService.shutdownNow();
					
				} catch(Exception e) {
					LOG.error("Erro na finalização do Transmissor.", e);
				}
				
				LOG.info("Transmissor Finalizado!");
				
			}
		});

		try {
			configuration = ConfigurationLoader.loadConfiguration();
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento de configurações.", e);
			System.exit(-1);
		}
		
		servicesExecutorService = Executors.newFixedThreadPool(2, new NamedThreadFactory("transmissor-services"));
		
		transmissorSocketProcessors = new CopyOnWriteArrayList<>();
		
		CLJDashboardFrame dashboardFrame = new CLJDashboardFrame(transmissorSocketProcessors);
		dashboardFrame.setVisible(true);

		Repository<Packet> repository = loadRepository();
		
		createPacketTransmissionInit(repository);
		
		try {
			loadRepositoryWriter(configuration, repository);
		} catch (Exception e) {
			LOG.fatal("Erro no carregamento do escritor de repositorio.", e);
			System.exit(-1);
		}
		
		socketExecutorService = Executors.newCachedThreadPool(new NamedThreadFactory("transmissor-socket"));
		
		scheduledSocketExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("transmissor-socket-scheduled"));
		
		scheduledSocketExecutorService.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {

				List<InetAddress> addresses = null;
				try {
					addresses = loadLocalInterfaces();
				} catch (Exception e) {
					LOG.fatal("Erro no carregamento das interfaces locais.", e);
					System.exit(-1);
				}
				
				loadSockets(configuration, repository, addresses);

			}
			
		}, 0, 10, TimeUnit.SECONDS);
		
	}

	private static void createPacketTransmissionInit(Repository<Packet> repository) {
		
		LOG.info("Criando pacote de inicializacao...");
		
		Packet packet = new Packet(Packet.TYPE_INIT, 0, "".getBytes());
		repository.put(packet);
		
		LOG.info("Pacote de inicializacao criado com sucesso.");
		
	}

	private static void loadSockets(Configuration configuration, Repository<Packet> repository, List<InetAddress> addresses) {
		
		LOG.info("Carregando sockets para transmissao...");
		
		String remoteReceptorIp = configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_REMOTE_RECEPTOR_IP);
		int remoteReceptorPort = Integer.parseInt(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_REMOTE_RECEPTOR_PORT));

		int localPort = remoteReceptorPort+1;
		
		for(InetAddress address : addresses) {
			
			TransmissorSocketProcessor transmissorSocketProcessor = null;
			
			Optional<TransmissorSocketProcessor> optional = transmissorSocketProcessors.stream().filter(i -> i.getInetAddress().equals(address)).findFirst();
			
			if (optional.isPresent()) {
				
				transmissorSocketProcessor = optional.get();
				
				if (transmissorSocketProcessor.getConnected().get()) {
					LOG.debug(String.format("Socket[%s] já conectado, continuando...", transmissorSocketProcessor));
					continue;
				} else {
					//TODO: Verificar como cancelar a execução e se é necessária.
					//transmissorSocketProcessor.disconnect();
					transmissorSocketProcessors.remove(transmissorSocketProcessor);
				}
				
			} 
			
			transmissorSocketProcessor = new TransmissorSocketProcessor(repository, address, localPort++, remoteReceptorIp, remoteReceptorPort);
			
			transmissorSocketProcessors.add(transmissorSocketProcessor);
			
			socketExecutorService.submit(transmissorSocketProcessor);
			
			LOG.info(String.format("Interface carregada e operando em modo de transmissão[%s]. ", transmissorSocketProcessor));

		}
		
		LOG.debug("Verificando enderecos que foram removidos...");
		
		for(TransmissorSocketProcessor transmissorSocketProcessor : new ArrayList<>(transmissorSocketProcessors.stream().filter(i -> !addresses.contains(i.getInetAddress())).collect(Collectors.toList()))) {
			try {
				transmissorSocketProcessor.disconnect();
			} catch (IOException e) {
				LOG.error(String.format("Erro na desconexao de interfaces nao existentes [%s].", transmissorSocketProcessor));
			}
			transmissorSocketProcessors.remove(transmissorSocketProcessor);
			LOG.info(String.format("Interface removida da transmissão[%s]. ", transmissorSocketProcessor));
		}
		
		LOG.info(String.format("Sockets[total:%d] para transmissao carregados com sucesso.", transmissorSocketProcessors.size()));
		
	}

	private static List<InetAddress> loadLocalInterfaces() throws Exception {
		
		LOG.info("Carregando interfaces locais...");
		
		List<InetAddress> addresses = NetworkUtil.listInetAddress();

		LOG.info("Interfaces locais carregadas com sucesso.");
		
		return addresses;
	}

	private static void loadRepositoryWriter(Configuration configuration, Repository<Packet> repository) throws Exception {
		
		LOG.info("Carregando escritor de repositorio...");
		
		double minVolumeRMS = Double.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_MIN_VOLUME_RMS));
		float sampleRate = Float.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLERATE));
		int sampleSize = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SAMPLESIZE));
		int channels = Integer.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_CHANELS));
		boolean signed = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_SIGNED));
		boolean bigEndian = Boolean.valueOf(configuration.getConfiguration(ConfigurationProperty.TRANSMISSOR_AUDIO_BIGENDIAN));
		
		servicesExecutorService.execute(new RepositoryAudioWriter(repository, minVolumeRMS, sampleRate, sampleSize, channels, signed, bigEndian));
		
		LOG.info(String.format("Escritor de Repositorio carregado com sucesso RepositoryAudioWriter[sampleRate:%f,sampleSize:%d,chanels:%d,signed:%b,bigEndian:%b].", sampleRate, sampleSize, channels, signed, bigEndian));

	}

	private static Repository<Packet> loadRepository() {
		
		LOG.info("Carregando repositorio para transmissao...");
		
		Repository<Packet> repository = new PriorityRepository<>();
		
		LOG.info("Repositorio para transmissao carregado com sucesso.");
		
		return repository;
	}

}
