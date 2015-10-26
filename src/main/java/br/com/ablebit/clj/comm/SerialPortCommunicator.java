package br.com.ablebit.clj.comm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * Responsável por toda comunicação Serial.
 * 
 * @author leandro.franchi
 * 
 */
public class SerialPortCommunicator {
	
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(SerialPortCommunicator.class);

	/**
	 * Identificacao da Porta.
	 */
	private String portName;

	/**
	 * Porta Serial.
	 */
	private SerialPort serialPort;
	
	/**
	 * Mensagens de Retorno da Porta Serial.
	 */
	private BlockingQueue<String> messagesQueue = new LinkedBlockingQueue<>();

	/**
	 * Contrutor Padrão.
	 * 
	 * @param portName
	 * @throws Exception 
	 */
	public SerialPortCommunicator(String portName) throws Exception {
		this.portName = portName;
	}

	/**
	 * Executa a conexão com a Porta e inicia Threads de Escrita e Leitura.
	 * 
	 * @throws UnsupportedCommOperationException
	 * @throws PortInUseException
	 * @throws IOException
	 * @throws TooManyListenersException 
	 * @throws NoSuchPortException 
	 */
	public void connect() throws UnsupportedCommOperationException, PortInUseException, IOException, TooManyListenersException, NoSuchPortException {
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {

			LOG.warn("A porta " + portIdentifier.getName() + " se encontra em uso.");

		} else {

			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {

				this.serialPort = (SerialPort) commPort;

				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				
				serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
                
			} else {
				LOG.warn("A porta " + portIdentifier.getName() + " nao e uma porta serial.");
			}
		}
	}
	
	/**
	 * Envia comando a Posta Serial e antes efetua limpesa na fila de respostas dos comandos.
	 * 
	 * @param message
	 * @param timeout
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendMessage(String message, int timeout) throws IOException, InterruptedException {
		sendMessage(message, timeout, true);
	}
	
	/**
	 * Envia comando a Porta Serial.
	 * 
	 * @param message
	 * @param clearOutput
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void sendMessage(String message, int timeout, boolean clearOutput) throws IOException, InterruptedException {

		/* Limpa o Buffer de Respostas */
		if (clearOutput) {
			messagesQueue.clear();
		}
		
		/* Envia o Comando */
		if (this.serialPort!=null) {
			this.serialPort.getOutputStream().write((message+"\r").getBytes());
			Thread.sleep(timeout);
		}
	}

	/**
	 * Executa finalização da Comunicação com a porta.
	 */
	public void shutdown() {
		this.serialPort.close();
	}

	/**
	 * Efetua leitura da porta quando acionado.
	 * 
	 * @author leandro.franchi
	 *
	 */
	public class SerialReader implements SerialPortEventListener {
		
		private InputStream inputStream;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public void serialEvent(SerialPortEvent event) {

			switch (event.getEventType()) {
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				readData();
				break;
			}

		}
		
		private void readData() {

			int data;

			try {
				int len = 0;
				while ((data = inputStream.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				messagesQueue.put(new String(buffer, 0, len));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * Retorna ultima mensagen da Fila.
	 * 
	 * @return
	 */
	public String getLastMessage() {
		return messagesQueue.poll();
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

}
