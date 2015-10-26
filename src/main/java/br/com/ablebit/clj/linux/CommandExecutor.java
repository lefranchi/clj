package br.com.ablebit.clj.linux;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * Executor de Comandos.
 * 
 * @author lfranchi
 *
 */
public class CommandExecutor {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(CommandExecutor.class);

	/**
	 * Executa um comando no SO.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String execute(String command) throws IOException, InterruptedException {
		
		/* Retorno do Comando */
		StringBuilder retValue = new StringBuilder();
		
		LOG.debug("Pegando runtime...");
		Runtime runtime = Runtime.getRuntime();
		LOG.debug("Runtime Ok!");
		
		LOG.debug("Executando processo[" + command + "]...");
		Process process = runtime.exec(command);
		
		LOG.debug("Pegando saida do processo...");
		InputStream is = process.getInputStream();
		LOG.debug("Saida do processo pega!");
		
		LOG.debug("Instanciando leitor para saida do processo [" + command + "]...");
		BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(is));
		LOG.debug("Leitor do Processo[" + command + "] Instanciado!");
		
		LOG.debug("Lendo saida do Processo[" + command + "]...");
		String s = null;
        while ((s = reader.readLine()) != null) {
            retValue.append(s);
        }
        LOG.debug("Processo[" + command + "] lido!");

		LOG.debug("Aguardando execucao do processo [" + command + "]...");
		process.waitFor();
		LOG.debug("Processo[" + command + "] executado!");

        LOG.debug("Fechando saida do Processo[" + command + "]...");
		is.close();
		LOG.debug("Saida do Processo[" + command + "] fechada!");
		
		return retValue.toString();		
		
	}
	
}