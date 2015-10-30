package br.com.ablebit.clj.data.impl;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.StatisticsWriter;
import br.com.ablebit.clj.net.TransmissorSocketProcessor;

public class MongoStatisticsWriter implements StatisticsWriter {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(MongoStatisticsWriter.class);

	/**
	 * Lista de transmissores.
	 */
	private List<TransmissorSocketProcessor> transmissorSocketProcessors;
	
	public MongoStatisticsWriter(List<TransmissorSocketProcessor> transmissorSocketProcessors) {
		this.setTransmissorSocketProcessors(transmissorSocketProcessors);
	}

	@Override
	public void run() {
		// TODO Implementar gravador de statisticas Mongo.
		
		LOG.debug("Executando escritor de estatisticas mongo.");
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public List<TransmissorSocketProcessor> getTransmissorSocketProcessors() {
		return transmissorSocketProcessors;
	}

	public void setTransmissorSocketProcessors(List<TransmissorSocketProcessor> transmissorSocketProcessors) {
		this.transmissorSocketProcessors = transmissorSocketProcessors;
	}

}
