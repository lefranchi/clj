package br.com.ablebit.clj.data;

/**
 * Repositorio de Objetos.
 * 
 * @author leandrofranchi
 *
 */
public interface Repository<T> {

	/**
	 * Retorna objeto do repositório. Deve aguardar primeiro objeto caso o Repositório esteja vazio.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public T take() throws InterruptedException;
	
	/**
	 * Insere objeto no Repositorio.
	 * 
	 * @param object
	 */
	public void put(T object);
	
}