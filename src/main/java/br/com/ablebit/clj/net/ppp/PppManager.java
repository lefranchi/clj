package br.com.ablebit.clj.net.ppp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import br.com.ablebit.clj.comm.CommUtils;
import br.com.ablebit.clj.comm.CommUtils.AT_COMMAND;
import br.com.ablebit.clj.comm.SerialPortCommunicator;
import br.com.ablebit.clj.config.Configuration;
import br.com.ablebit.clj.linux.LinuxUtils;
import br.com.ablebit.clj.linux.UdevCommandExecutor;

public class PppManager {
	
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(PppManager.class);

	/**
	 * Template para chatscripts.
	 */
	public static String CHATSCRIPTS_TEMPLATE = "";
	
	/**
	 * Template para peers.
	 */
	public static String PEERS_TEMPLATE = "";
	
	/**
	 * Propriedades da lista de IMSI.
	 */
	public static Properties CACHE_IMSI_DB;
	
	/**
	 * Configuracoes das operadoras (Cache).
	 */
	public static Map<String, Properties> CACHE_CARRIER_PROPERTIES = new HashMap<>();
	

	public static void main(String[] args) {

		
		LOG.info("Inicializando Conector PPP...");

		if(args.length<2) {
			LOG.info("Finalizando com erro. Numero de parametros incorreto.");
			System.exit(-1);
		}
		
		String deviceName = args[0];
		String action = args[1];
		
		LOG.info(String.format("Executando %s em %s.", action, deviceName));

		LOG.debug("Carregando templates pppd[chatscripts]...");
		try {
			loadChatScriptsTemplate();
		} catch(Exception e) {
			LOG.fatal("Impossivel carregar templates[chatscripts]! Enviando sinal de encerramento!", e);
			System.exit(-1);
		}
		LOG.info("Templates pppd[chatscripts] carregados!");


		LOG.debug("Carregando templates pppd[peers]...");
		try {
			loadPeersTemplate();
		} catch(Exception e) {
			LOG.fatal("Impossivel carregar templates[peers]! Enviando sinal de encerramento!", e);
			System.exit(-1);
		}
		LOG.info("Templates pppd[peers] carregados!");

		LOG.debug("Carregando base de dados de IMSI...");
		try {
			loadImsiDb();
		} catch (IOException e) {
			LOG.fatal("Impossivel carregar Base de Dados de IMSI! Enviando sinal de encerramento!", e);
			System.exit(-1);
		}
		LOG.info("Base de Dados de IMSI carregados!");

		LOG.debug("Carregando arquivos de propriedades das operadoras...");
		loadCarrierProperties();
		LOG.info("Arquivos de propriedades das operadoras carregados!");
		
		
		LOG.debug("Criando arquivo de senhas...");
		try {
			generateSecretFiles();
		} catch(IOException e) {
			LOG.fatal("Impossivel criar arquivos de senhas! Enviando sinal de encerramento!", e);
			System.exit(-1);
		}
		LOG.info("Arquivos de senhas criados!");
		
		connectNewDevice(deviceName);
		
	}
	
	/**
	 * Carrega o template para chatscripts em memoria.
	 * 
	 * @throws IOException
	 */
	private static void loadChatScriptsTemplate() throws IOException {
		LOG.debug("Carregando template chatscripts em memoria...");
		BufferedReader chatscriptsTemplateReader = new BufferedReader(new InputStreamReader(PppManager.class.getResourceAsStream("/templates/chatscripts")));
		String line = "";
		while ( (line = chatscriptsTemplateReader.readLine()) != null )
			CHATSCRIPTS_TEMPLATE += line + System.lineSeparator();
		LOG.debug("Template chatscripts carregado em memoria!");
	}
	
	/**
	 * Carrega o template para peers em memoria.
	 * 
	 * @throws IOException
	 */
	private static void loadPeersTemplate() throws IOException {
		LOG.debug("Carregando template peers em memoria...");
		BufferedReader chatscriptsTemplateReader = new BufferedReader(new InputStreamReader(PppManager.class.getResourceAsStream("/templates/peers")));
		String line = "";
		while ( (line = chatscriptsTemplateReader.readLine()) != null )
			PEERS_TEMPLATE += line + System.lineSeparator();
		LOG.debug("Template peers carregado em memoria!");
	}
	
	/**
	 * Carrga lista de IMSI em cache.
	 * 
	 * @throws IOException
	 */
	private static void loadImsiDb() throws IOException {
		LOG.debug("Carregando cache para IMSIDB...");
		CACHE_IMSI_DB = new Properties();
		CACHE_IMSI_DB.load(PppManager.class.getResourceAsStream("/carrierdb/imsidb"));
		LOG.debug("Cache de IMSIDB carregado!");
	}

	/**
	 * Carrega em memoria todos arquivos de propriedades das operadoras.
	 */
	@SuppressWarnings("deprecation")
	private static void loadCarrierProperties() {

		LOG.debug("Carregando arquivo de propriedade de operadoras... ");
		
		LOG.debug("Criando path para /carrierdb/carrier...");
		URL url = PppManager.class.getResource("/carrierdb/carrier");
		Path devicesPath = FileSystems.getDefault().getPath(url.getFile());
		LOG.debug("Path para /carrierdb/carrier criado!");
		
		LOG.debug("Criando stream de diretorio de propriedades de operadoras...");
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(devicesPath, "*")) {
		
			for (Path file : ds) {

				String carrier = file.toFile().getName();
				
				LOG.debug("Carregando cache de propriedades para a operadora [" + carrier + "]...");
				Properties carrierProperties = new Properties();
				carrierProperties.load(new FileInputStream(file.toFile()));
				Configuration.print(carrierProperties, Priority.DEBUG);
				CACHE_CARRIER_PROPERTIES.put(carrier, carrierProperties);
				LOG.debug("Propriedades da operadora " + file + " foram carregadas!");
				
			}
			
		} catch (IOException e) {
			LOG.fatal("Erro ao carregas arquivos de configuracao de operadoras! Enviano sinal de desligamento.");
			System.exit(-1);
		}

		LOG.debug("Arquivo de propriedade de operadoras carregados!");
		
	}
	
	/**
	 * Gera arquivo de senhas baseado nos arquivos de configuracoes das operadoras (carrierdb/carrier).
	 * 
	 * @throws IOException 
	 */
	public static void generateSecretFiles() throws IOException {
		
		StringBuilder sb = new StringBuilder();
		
		LOG.debug("Carregando template pap-secrets em memoria...");
		BufferedReader papSecretsTemplateReader = new BufferedReader(new InputStreamReader(PppManager.class.getResourceAsStream("/templates/pap-secrets")));
		String line = "";
		while ( (line = papSecretsTemplateReader.readLine()) != null )
			sb.append(line).append(System.lineSeparator());

		for(Properties carrierProperties : CACHE_CARRIER_PROPERTIES.values())
			sb.append(carrierProperties.getProperty("user")).append("     *       ").append(carrierProperties.getProperty("pwd")).append(System.lineSeparator());
		
		Path path = FileSystems.getDefault().getPath("/etc/ppp/", "pap-secrets");
		Files.write( path, sb.toString().getBytes(), StandardOpenOption.CREATE);
		
		LOG.debug("Template pap-secretes carregado em memoria!");

	}

	/**
	 * Efetua a criacao e conexao do novo device.
	 * @param deviceName
	 */
	private static void connectNewDevice(String deviceName) {

		try {
			
			LOG.debug("Executando comando para verificacao da origem da porta...");
			String udevCommandReturnValue = UdevCommandExecutor.executeInfo(deviceName);
			LOG.debug("Comando executado[" + udevCommandReturnValue + "]!");
			
			LOG.debug("Verificando ordem da porta[" + deviceName + "] no hardware...");
			int portOrder = LinuxUtils.extractDeviceOrder(udevCommandReturnValue);
			LOG.debug("Ordem da porta[" + deviceName + "] verificada!");
			
			LOG.debug("Somente portas 0 podem ser utilizadas, verificando...");
			if (portOrder>0) {
				LOG.warn("A porta do device [" + deviceName + "] é [" + portOrder + "]. Por isto ela nao devera ser usada! Abortando criacao para ela!");
				return;
			}
			LOG.info("A porta do device [" + deviceName + "] é [" + portOrder + "]!");
			
			LOG.debug("Criando SerialCommunicator para [" + deviceName + "]...");
			SerialPortCommunicator communicator = null;
			try {
				communicator = new SerialPortCommunicator("/dev/"+deviceName);
				communicator.connect();
				LOG.debug("SerialCommunicator para " + deviceName + " criado e conectado!");
			} catch(Exception e) {
				LOG.fatal("Erro ao criar conexao com porta serial.", e);
				System.exit(-1);
			}
			
			LOG.debug("Enviando comando Ati para " + deviceName + "...");
			communicator.sendMessage("ATi", 1000);
			LOG.debug("Comando Ati enviado para " + deviceName + "!");
			
			LOG.debug("Recendo resposta do comando ATi de " + deviceName + "...");
			String message = null;
			boolean hasMessage = false;
			while((message = communicator.getLastMessage())!=null) {
				LOG.debug(message);
				hasMessage = true;
			}
			LOG.debug("Resposta recebida de " + deviceName + "!");

			LOG.debug("Verificando resposta de " + deviceName + "...");
			if(!hasMessage) {
				LOG.debug("Resposta de " + deviceName + " esta vazia. Ignorando!");
				return;
			} else {
				LOG.debug("Resposta de " + deviceName + " esta OK!");
			}
			
			LOG.debug("Extraindo IMEI de " + deviceName + "...");
			String imei = CommUtils.extract(communicator, AT_COMMAND.IMEI);
			LOG.debug("IMEI[" + imei + "] de " + deviceName + " Extraido!");

			LOG.debug("Extraindo IMSI de " + deviceName + "...");
			String imsi = CommUtils.extract(communicator, AT_COMMAND.IMSI);
			LOG.debug("IMSI[" + imsi + "] de " + deviceName + " Extraido!");

			LOG.debug("Finalizando SerialCommunicator para " + deviceName + "...");
			communicator.shutdown();
			LOG.debug("SerialCommunicator para " + deviceName + " finalizado!");

			LOG.debug("Instanciando PppConnection para " + deviceName + " IMEI:" + imei + " IMSI:" + imsi);
			PppConnection pppConnection = new PppConnection(deviceName, imei, imsi);
			LOG.debug("PppConnection para " + deviceName + " IMEI:" + imei + " IMSI:" + imsi + " instancado!");
			
			LOG.debug("Criando arquivos de configuracao pppd[" + deviceName + "]...");
			pppConnection.executeConfiguration();
			LOG.debug("Arquivos de configuracao pppd[" + deviceName + "] criados!");
			
			LOG.debug("Efetuando conexao pppd em [" + deviceName + "]...");
			pppConnection.connect();
			LOG.info("Conexao pppd em [" + deviceName + "] efetuada!");
			
		} catch(Exception e) {
			LOG.error("Erro na comunicacao com a porta " + deviceName, e);
		}

	}
	
}
