package br.com.ablebit.clj.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Utilitario para Rede.
 * 
 * @author leandro.franchi
 * 
 */
public class NetworkUtil {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(NetworkUtil.class);

	/**
	 * Metodo que retorna uma lista de interfaces.
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static List<NetworkInterface> listInterfaces() throws SocketException {
		
		List<NetworkInterface> interfaces = new ArrayList<>();

		Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
		
		for (NetworkInterface networkInterface : Collections.list(interfaceList)) {
			if (!networkInterface.isLoopback())
				interfaces.add(networkInterface);
		}
		
		return interfaces;
	}
	
	public static InetAddress getIpv4Address(NetworkInterface networkInterface) throws SocketException {
		
		Enumeration<NetworkInterface> ifaces = networkInterface.getNetworkInterfaces();
		
		while(ifaces.hasMoreElements()) {
			
			NetworkInterface iface = ifaces.nextElement();
			Enumeration<InetAddress> addresses = iface.getInetAddresses();
		
			while(addresses.hasMoreElements()) {
				
				InetAddress addr = addresses.nextElement();
				if(addr instanceof Inet4Address && !addr.isLoopbackAddress())
					return addr;
				
			}
			
		}

		return null;
		
	}


	/**
	 * Imprime informações da Interface.
	 * 
	 * @param networkInterface
	 * @throws SocketException
	 */
	static void displayInterfaceInformation(NetworkInterface networkInterface) throws SocketException {

		LOG.info("Display name: " + networkInterface.getDisplayName());
		LOG.info("Name: " + networkInterface.getName());
		Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			LOG.info("InetAddress: " + inetAddress);
		}

		LOG.info("Up? " + networkInterface.isUp());
		LOG.info("Loopback? " + networkInterface.isLoopback());
		LOG.info("PointToPoint? " + networkInterface.isPointToPoint());
		LOG.info("Supports multicast? " + networkInterface.supportsMulticast());
		LOG.info("Virtual? " + networkInterface.isVirtual());
		LOG.info("Hardware address: " + Arrays.toString(networkInterface.getHardwareAddress()));
		LOG.info("MTU: " + networkInterface.getMTU());
		
	}

}
