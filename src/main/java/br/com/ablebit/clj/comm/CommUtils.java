package br.com.ablebit.clj.comm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gnu.io.CommPortIdentifier;

/**
 * Utilitarios.
 * 
 * @author lfranchi
 *
 */
public class CommUtils {

	/**
	 * Lista todas portas.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<CommPortIdentifier> listPorts() {
		
		List<CommPortIdentifier> portList = new ArrayList<>();
		
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		
		while (portIdentifiers.hasMoreElements())
		{
		    CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
	        portList.add(pid);
		}
		
		return portList;
	}
	
	/**
	 * Extrai propriedade do modem.
	 * 
	 * @param communicator
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static String extract(SerialPortCommunicator communicator, AT_COMMAND command) throws IOException, InterruptedException {
		
		/* Return Value */
		String retValue = "";
		
		/* Envia comando de status do Modem */
		communicator.sendMessage(command.getCommand(), 1000);

		/* Le o retorno */
		String message = null;
		while((message = communicator.getLastMessage())!=null) {
			if (StringUtils.isNotBlank(message)) {
				if (!message.contains(command.getCommand())) {
					retValue = message.trim();
					break;
				}
			}
		}
		
		return retValue;
	}
	
	/**
	 * Comandos AT.
	 * 
	 * @author lfranchi
	 *
	 */
	public enum AT_COMMAND {
		
		STATUS("ATi"),
		MANUFACTURER("AT+CGMI"),
		MODEL("AT+CGMM"),
		IMEI("AT+CGSN"),
		IMSI("AT+CIMI");
		
		private String command;
		
		AT_COMMAND(String command) {
			this.command = command;
		}

		public String getCommand() {
			return command;
		}

	}
}
