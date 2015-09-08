package br.com.ablebit.clj.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

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
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
