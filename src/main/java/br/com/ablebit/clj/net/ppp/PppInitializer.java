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

import br.com.ablebit.clj.config.Configuration;

/**
 * Inicializa configurações que devem ser executadas somente uma vez. 
 * 
 * 	TODO: Criar script para executar este programa na inicializacao do SO.
 * 
 * @author lfranchi
 *
 */
public class PppInitializer {
	
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(PppInitializer.class);

	/**
	 * Configuracoes das operadoras (Cache).
	 */
	public static Map<String, Properties> CACHE_CARRIER_PROPERTIES = new HashMap<>();

	public static void main(String[] args) {
		
		LOG.info("Inicializando Inicializador PPP...");
		
		loadCarrierProperties();

		try {
			generateSecretFiles();
		} catch(IOException e) {
			LOG.fatal("Impossivel criar arquivos de senhas!", e);
			System.exit(-1);
		}

		LOG.info("Inicializador PPP inicializado com sucesso.");
		
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


}
