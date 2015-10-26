package br.com.ablebit.clj.linux;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Comnando de PPPD.
 * 
 * @author lfranchi
 *
 */
public class PppCommandExecutor {

	/**
	 * Logger.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PppCommandExecutor.class);
	
	/**
	 * Executa PON.
	 * 
	 * @param interfaceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String pon(String interfaceName) throws IOException, InterruptedException {
		String command = "pon " + interfaceName;
		return CommandExecutor.execute(command);
	}
	
	/**
	 * Executa POFF.
	 * 
	 * @param interfaceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String poff(String interfaceName) throws IOException, InterruptedException {
		String command = "poff " + interfaceName;
		return CommandExecutor.execute(command);
	}
	

}
