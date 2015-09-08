package br.com.ablebit.clj.data.impl;

import java.util.concurrent.PriorityBlockingQueue;

import br.com.ablebit.clj.data.Repository;

/**
 * Repositorio de dados em fila com prioridade.
 * 
 * @author lfranchi
 *
 * @param <T>
 */
public class PriorityRepository<T> extends PriorityBlockingQueue<T> implements Repository<T> {

	private static final long serialVersionUID = 3706467594856374123L;

}
