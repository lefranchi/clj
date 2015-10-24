package br.com.ablebit.clj.config;

import org.apache.log4j.Logger;

public class ConfigurationLoader {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(ConfigurationLoader.class);

	public static Configuration loadConfiguration() throws Exception {
		
		LOG.info("Carregando configuracoes...");
		
		Configuration configuration = new Configuration("clj.properties");
		configuration.load();
		
		LOG.info("Configuracoes carregadas com sucesso.");

		return configuration;
	}

}
