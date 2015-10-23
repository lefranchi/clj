package br.com.ablebit.clj.net;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Pacote que trafega entre o Transmissor e o Receptor.
 * 
 * @author lfranchi
 *
 */
public class Packet implements Serializable, Comparable<Packet> {

	private static final long serialVersionUID = 5565357008039470438L;

	/**
	 * Contador.
	 */
	private long counter;
	
	/**
	 * Conteudo do pacote.
	 */
	private byte[] content;
	
	/**
	 * De onde veio o pacote.
	 */
	private String source;
	
	/**
	 * Construtor.
	 * 
	 * @param protocolCommand
	 * @param counter
	 * @param content
	 */
	public Packet(long counter, byte[] content) {
		this.counter = counter;
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Packet o) {
		return new Long(getCounter()).compareTo(o.getCounter());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(":counter:").append(this.counter);
		sb.append(":source:").append(this.source);
		sb.append(":content:").append(new String(this.content));
		sb.append(":raw:").append(Arrays.toString(this.content));
		sb.append("-").append(super.toString());
		return sb.toString();
	}

	public long getCounter() {
		return counter;
	}
	public void setCounter(long counter) {
		this.counter = counter;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
