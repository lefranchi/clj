package br.com.ablebit.clj.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Configuration {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Configuration.class);

	private String fileName;
	private boolean loaded;
	private Properties properties = new Properties();
	
	public Configuration(String fileName) {
		this.fileName = fileName;
		this.properties = new Properties();
	}
	
	public void load() throws IOException {
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getFileName());
		properties.load(inputStream);
		loaded = true;
	}
	
	public String getConfiguration(ConfigurationProperty property) {
		
		if(!loaded) {
			LOG.error("As configurações não foram carregadas!");

		}
		
		return properties.getProperty(property.name());
		
	}
	
	/**
	 * Imprime Chaves e Valores da Propriedade.
	 * 
	 * @param properties
	 */
	public static void print(Properties properties, Priority logPriority) {
		
		for(Entry<Object, Object> e : properties.entrySet())
			LOG.log(logPriority, e.getKey() + "=" + e.getValue());
		
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
