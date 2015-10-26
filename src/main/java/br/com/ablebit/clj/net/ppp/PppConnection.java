package br.com.ablebit.clj.net.ppp;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.com.ablebit.clj.linux.PppCommandExecutor;

/**
 * Representa uma conexao a ser gerenciada pelo PPP.
 * 
 * @author leandro.franchi
 *
 */
public class PppConnection {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(PppConnection.class);

	/**
	 * Caminho do Modem (ex: /dev/ttyUSB0.)
	 */
	private String deviceName;
	
	/**
	 * Codigo do Imei.
	 */
	private String imei;
	
	/**
	 * Codigo IMSI.
	 */
	private String imsi;
	
	/**
	 * Nome da Operadora.
	 */
	private String carrier;
	
	/**
	 * Nome gerado na criacao das configuracoes.
	 */
	private String connectionName;
	
	/**
	 * Contrutor Padrao.
	 * 
	 * @param deviceName
	 * @param imsi
	 */
	public PppConnection(String deviceName, String imei, String imsi) {
		this.deviceName = deviceName;
		this.imei = imei;
		this.imsi = imsi;
	}
	
	/**
	 * Executa configuracao. Cria arquivos de configuracoes para pppd.
	 */
	@SuppressWarnings("deprecation")
	public void executeConfiguration() throws Exception {
		
		/* Verifica Operadora de acordo com o IMSI */
		LOG.debug("Identificando operadora...");
		this.carrier = extractCarrier();
		LOG.debug("Identificado oeradora [" + carrier + "] na porta [" + getDeviceName() + "]!");
		
		LOG.debug("Gerando nome de conexao...");
		this.connectionName = generateConnectionName();
		LOG.debug("Nome de conexao[" + this.connectionName + "] gerado!");
		
		LOG.debug("Gerando arquivos em chatsripts[" + this.connectionName + "]...");
		generateChatScripts(this.connectionName);
		LOG.debug("Arquivos chatscripts[" + this.connectionName + "] gerados!");
		
		LOG.debug("Gerando arquivos em peers[" + this.connectionName + "]...");
		generatePeersScripts(this.connectionName);
		LOG.debug("Arquivos em peers[" + this.connectionName + "] gerados!");
	
	}
	
	/**
	 * Efetua conexao pppd.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void connect() throws IOException, InterruptedException {
		LOG.debug("Executando comando de conexao em [" + getConnectionName() + "]");
		String commandReturn = PppCommandExecutor.pon(getConnectionName());
		LOG.debug("Retorno do comando de conexao em [" + commandReturn + "]");
	}
	
	/**
	 * Efetua descoexao pppd.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void disconnect() throws IOException, InterruptedException {
		PppCommandExecutor.poff(getConnectionName());
	}
	
	/**
	 * Verifica Operadora de acordo com o IMSI.
	 * 
	 * @return
	 * @throws IOException 
	 */
	private String extractCarrier() throws IOException {
		return PppManager.CACHE_IMSI_DB.getProperty(getImsi().substring(0, 5));
	}
	
	/**
	 * Gera arquivos em chascripts para a conexao a partir dos templates.
	 * 
	 * @param connectionName
	 * @throws IOException 
	 */
	private void generateChatScripts(String connectionName) throws IOException {
		LOG.debug("Gerando template para chatscripts para [" + getCarrier() + " em " + getDeviceName() + "]...");
		String newTemplateBody = StringUtils.replace(PppManager.CHATSCRIPTS_TEMPLATE, "${APN}", PppManager.CACHE_CARRIER_PROPERTIES.get(getCarrier()).getProperty("apn"));
		LOG.debug("Template chatscripts para [" + getCarrier() + " em " + getDeviceName() + "] gerado!");
		
		LOG.debug("Salvando arquivo de chatscripts para [" + getCarrier() + " em " + getDeviceName() + "]...");
		Path path = FileSystems.getDefault().getPath("/etc/chatscripts/", getConnectionName());
		Files.write( path, newTemplateBody.getBytes(), StandardOpenOption.CREATE);
		LOG.debug("Arquivo de chatscripts para [" + getCarrier() + " em " + getDeviceName() + "] salvo!");
	}
	
	/**
	 * Gera arquivos em peers para a conexao a partir dos templates.
	 * 
	 * @param connectionName
	 * @throws IOException 
	 */
	private void generatePeersScripts(String connectionName) throws IOException {
		LOG.debug("Gerando template para peers para [" + getCarrier() + " em " + getDeviceName() + "]...");
		String newTemplateBody = StringUtils.replace(PppManager.PEERS_TEMPLATE, "${connectionName}", getConnectionName());
		newTemplateBody = StringUtils.replace(newTemplateBody, "${deviceName}", getDeviceName());
		newTemplateBody = StringUtils.replace(newTemplateBody, "${baudRate}", "9600"); //TODO PEGAR AUTOMATICO O BAUDRATE E PARIDADE
		newTemplateBody = StringUtils.replace(newTemplateBody, "${user}", PppManager.CACHE_CARRIER_PROPERTIES.get(getCarrier()).getProperty("user"));
		newTemplateBody = StringUtils.replace(newTemplateBody, "${remotename}", PppManager.CACHE_CARRIER_PROPERTIES.get(getCarrier()).getProperty("user"));
		LOG.debug("Template peers para [" + getCarrier() + " em " + getDeviceName() + "] gerado!");
		
		LOG.debug("Salvando arquivo de peers para [" + getCarrier() + " em " + getDeviceName() + "]...");
		Path path = FileSystems.getDefault().getPath("/etc/ppp/peers", getConnectionName());
		Files.write( path, newTemplateBody.getBytes(), StandardOpenOption.CREATE);
		LOG.debug("Arquivo de peers para [" + getCarrier() + " em " + getDeviceName() + "] salvo!");
	}
	
	/**
	 * Gera nome para a conexão.
	 * 
	 * @return
	 */
	private String generateConnectionName() {
		return getDeviceName().substring(getDeviceName().lastIndexOf('/')+"/ttyUSB".length(), getDeviceName().length()) + "_" + getCarrier();
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

}
