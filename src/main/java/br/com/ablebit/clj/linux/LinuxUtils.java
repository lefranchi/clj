package br.com.ablebit.clj.linux;


/**
 * Utilitadios Linux.
 * 
 * @author lfranchi
 *
 */
public class LinuxUtils {

	/**
	 * Extrai valor que indica ordem de criacao do hardware dentro do mesmo device.
	 * 	Ex: Conectando um modem USB, temos o seguinte path 
	 * 				/devices/pci0000:00/0000:00:02.0/usb1/1-3/1-3:1.0/ttyUSB0/tty/ttyUSB0
	 * 
	 * 	O quinto diretorio se refere ao hardware e o sexto, a porta dentro deste hardware. Neste caso o 
	 * 		metodo retornaria 0, referente ao 1-3:1.0
	 * 
	 * @param path
	 * @return
	 */
	public static int extractDeviceOrder(String path) {
		String deviceName = path.substring(path.lastIndexOf('/'), path.length());
		int deviceId_EndPosition = path.indexOf(deviceName);
		int deviceId_StartPosition = path.substring(0, deviceId_EndPosition).lastIndexOf('/');
		String devicePortComplete = path.substring(deviceId_StartPosition, deviceId_EndPosition);
		String deviceOrder = devicePortComplete.substring(devicePortComplete.lastIndexOf('.')+1, devicePortComplete.length());
		return Integer.parseInt(deviceOrder);
	}
}
