package br.com.ablebit.clj;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.config.ConfigurationProperty;
import br.com.ablebit.clj.data.Repository;
import br.com.ablebit.clj.data.impl.BufferedRepository;
import br.com.ablebit.clj.data.reader.RepositoryTextReader;
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
	private static ExecutorService executorService;

	/**
	 * Server Socket.
	 */
	private static ServerSocket serverSocket;

	/**
	 * Executor para leitura de sockets (NoBlocking IO).
	 */
	private static ExecutorService socketExecutorService;

	/**
	 * Futures para leitores de Sockets.
	 */
	private static List<Future<Boolean>> socketFutures = new ArrayList<>();

	public static void main(String[] args) {

		LOG.info("Inicializando Receptor...");

		Configuration configuration = new Configuration("clj.properties");
		try {
			configuration.load();
			LOG.info("Configuracoes do clj carregadas!");
		} catch(IOException e) {
			LOG.fatal("Problemas ao carregar arquivo de configurações.", e);
			System.exit(-1);
		}

		Integer bufferSize = new Integer(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_REPOSITORY_BUFFER_SIZE));
		Integer repositoryDelay = new Integer(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_REPOSITORY_DELAY));
		Repository<Packet> repository = new BufferedRepository<>(bufferSize, repositoryDelay);
		LOG.info("Criado repositorio para recepcao[bufferSize:" + bufferSize + ", repositoryDelay:" + repositoryDelay);

		/* Instancia executor para processos de leitura de repositorio */
		executorService = Executors.newSingleThreadExecutor();

		executorService.execute(new RepositoryTextReader(repository));
		LOG.info("Instanciado Leitor de repositório!");

		// setServerSocket(new ServerSocket(getLocalPort(), 100,
		// getNetworkInterface().getNetworkInterface().getInetAddresses().nextElement()));
		try {
			String receptorIp = configuration.getConfiguration(ConfigurationProperty.RECEPTOR_IP);
			int port = Integer.parseInt(configuration.getConfiguration(ConfigurationProperty.RECEPTOR_PORT));
			serverSocket = new ServerSocket(port, 100, Inet4Address.getByName(receptorIp));
			LOG.info("ServerSocket Conectado!");
		} catch (Exception e) {
			LOG.fatal("Erro na inicialização do socket na porta 5000.", e);
			System.exit(-1);
		}

		socketExecutorService = Executors.newCachedThreadPool(new NamedThreadFactory("cl-receptor-socket"));

		LOG.info("Escutando...");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				
				LOG.info("Finalizando Receptor...");
				
				try {
				
					serverSocket.close();
					socketExecutorService.shutdown();
					executorService.shutdown();
					
				} catch(Exception e) {
					LOG.error("Erro na finalização do Receptor.", e);
				}
				
				LOG.info("Receptor Finalizado!");
				
				System.exit(0);
				
			}
		});

		while (!Thread.currentThread().isInterrupted()) {
			try {
				/* Recebe mensagem */
				Socket socket = serverSocket.accept();
				/* Cria novo processador para a mensagem recebida */
				socketFutures.add(socketExecutorService.submit(new ReceptorSocketProcessor(socket, repository)));
			} catch (IOException e) {
				LOG.error("Erro em accept packet.", e);
			}
		}

	}

}
