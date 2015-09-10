package br.com.ablebit.clj;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationProperty;
import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.data.impl.PriorityRepository;
import br.com.ablebit.clj.data.writer.RepositoryTextWriter;
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
	private static ExecutorService executorService;
	
	/**
	 * Executor para leitura de sockets (NoBlocking IO).
	 */
	private static ExecutorService socketExecutorService;

	/**
	 * Futures para leitores de Sockets.
	 */
	private static List<Future<Boolean>> socketFutures = new ArrayList<>();

	public static void main(String[] args) {

		LOG.info("Inicializando Transmissor...");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				
				LOG.info("Finalizando Transmissor...");
				
				try {
				
					socketExecutorService.shutdown();
					executorService.shutdown();
					
				} catch(Exception e) {
					LOG.error("Erro na finalização do Transmissor.", e);
				}
				
				LOG.info("Transmissor Finalizado!");
				
				System.exit(0);
				
			}
		});

		Configuration configuration = new Configuration("clj.properties");
		try {
			configuration.load();
			LOG.info("Configuracoes do clj carregadas!");
		} catch(IOException e) {
			LOG.fatal("Problemas ao carregar arquivo de configurações.", e);
			System.exit(-1);
		}

		Repository<Packet> repository = new PriorityRepository<>();
		LOG.info("Criado repositorio para envio!");

		/* Instancia executor para processos de escrita de repositorio */
		executorService = Executors.newSingleThreadExecutor();

		/* Instancia leitor de audio para enviar pacotes para op repositorio */
		//executorService.execute(new TransmissorAudioRepositoryWriter(repository, configuration));
		executorService.execute(new RepositoryTextWriter(repository));
		LOG.info("Instanciado Leitor de repositório!");
		
		String remoteReceptorIp = configuration.getConfiguration(ConfigurationProperty.REMOTE_RECEPTOR_IP);
		int remoteReceptorPort = Integer.parseInt(configuration.getConfiguration(ConfigurationProperty.REMOTE_RECEPTOR_PORT));
		
		List<InetAddress> addresses = null;
		try {
			addresses = NetworkUtil.listInetAddress();
		} catch(Exception e) {
			LOG.fatal("Erro no carregamento das interfaces.", e);
			System.exit(-1);
		}
		LOG.info("Interfaces carregadas com sucesso!");
		
		socketExecutorService = Executors.newCachedThreadPool(new NamedThreadFactory("cl-transmissor-socket"));
		
		int localPort = remoteReceptorPort+1;
		
		for(InetAddress address : addresses) {
			
			TransmissorSocketProcessor socketProcessor = new TransmissorSocketProcessor(repository, address, localPort++, remoteReceptorIp, remoteReceptorPort);
			
			socketFutures.add(socketExecutorService.submit(socketProcessor));
		
			LOG.info("Interface carregada e operando em modo de transmissão! " + socketProcessor);
		}

	}

}
