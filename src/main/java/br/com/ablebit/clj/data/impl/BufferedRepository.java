package br.com.ablebit.clj.data.impl;

import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;

import br.com.ablebit.clj.data.Repository;

/**
 * Repositorio de dados com Buffer.
 * 
 * 	Somente retorna valor quando o repositorio atinge o numero minimo de objetos. 
 * 	Bloqueia novamente quando o Buffer fica vazio.
 * 
 * @author leandrofranchi
 *
 */
public class BufferedRepository<T> extends PriorityBlockingQueue<T> implements Repository<T> {
	
	private static final long serialVersionUID = -3362690359596823493L;
	
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(BufferedRepository.class);
	
	
	/**
	 * Tamanho minimo para liberação de dados do buffer.
	 */
	private final Integer bufferSize;
	
	/**
	 * Tempo de recorrencia para o controlador de abertura do buffer.
	 */
	@SuppressWarnings("unused")
	private final Integer delayController;
	
	/**
	 * Variavel de controle de abertura/fechamento do buffer.
	 */
	private volatile boolean closed = true;
	
	/**
	 * Contrutor Padrão;
	 * 
	 * @param bufferSize 		Tamanho minimo para liberação de dados do buffer.
	 * @param delayController 	Tempo de recorrencia para o controlador de abertura do buffer.	
	 */
	public BufferedRepository(Integer bufferSize, Integer delayController) {
		this.bufferSize = bufferSize;
		this.delayController = delayController;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.PriorityBlockingQueue#take()
	 */
	@Override
	public T take() throws InterruptedException {
		
		/* Aguarda até que o Buffer esteja aberto */
		while(closed) {
			
			/* Abre a leitura do buffer quando o tamanho dele for maior que o minimo */
			closed = size()<bufferSize;
			
			Thread.sleep(10);
		}
		
		/* Pega o valor do Buffer, aguardando o primeiro que entrar caso ele esteja vazio */
		T object = super.take();
		
		LOG.debug("Tamanho do buffer " + size());
		
		/* Fecha a leitura do buffer quando nao existe mais objetos */
		closed = size()==0;
		
		return object;
		
	}
	
}
