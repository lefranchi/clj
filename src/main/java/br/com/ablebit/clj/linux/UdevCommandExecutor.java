package br.com.ablebit.clj.linux;


import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Comandos de UdevAdm.
 * 
 * @author lfranchi
 *
 */
public class UdevCommandExecutor {
	
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(UdevCommandExecutor.class);


	/**
	 * Executa udevadm info -q path -n "DEVICE_PATH"
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String executeInfo(String path) throws IOException, InterruptedException {
		String command = "udevadm info -q path -n " + path;
		return CommandExecutor.execute(command);
		
	}

	/**
	 * Testes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
		try {
			LOG.info("TESTE UDEVADM PATH");
			String udevRetValue = executeInfo("/dev/ttyUSB0");
			LOG.info(udevRetValue);
			LOG.info("FIM TESTE UDEVADM PATH");
		} catch (Exception e) {
			LOG.fatal(e);
		}
		
	}
	
}
