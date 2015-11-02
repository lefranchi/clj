package br.com.ablebit.clj.ui;

import java.awt.FlowLayout;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import br.com.ablebit.clj.net.TransmissorSocketProcessor;

public class CLJDashboardFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private List<TransmissorSocketProcessor> transmissorSocketProcessors;
	
	private List<NetworkInterfacePanel> networkInterfacePanels = new ArrayList<>();
	
	private ScheduledExecutorService scheduledExecutorService;
	
	public CLJDashboardFrame(List<TransmissorSocketProcessor> transmissorSocketProcessors) {
		this.transmissorSocketProcessors = transmissorSocketProcessors;
		initComponents();
	}
	
	private void initComponents() {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		setTitle("CLJ");

		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				loadNetworkInterfacePanels();
				
			}
		}, 0, 10, TimeUnit.SECONDS);
		
	}

	private void loadNetworkInterfacePanels() {

		for (TransmissorSocketProcessor transmissorSocketProcessor : transmissorSocketProcessors) {
			
			NetworkInterfacePanel networkInterfacePanel = new NetworkInterfacePanel(transmissorSocketProcessor.getInetAddress(), transmissorSocketProcessor.getTotalPacketSent(), transmissorSocketProcessor.getTotalPackeSentGB());
			networkInterfacePanels.add(networkInterfacePanel);
			
			this.add(networkInterfacePanel);
			
		}

		this.setSize(540, 540);
		this.pack();
		
	}

	public List<TransmissorSocketProcessor> getTransmissorSocketProcessors() {
		return transmissorSocketProcessors;
	}


	public void setTransmissorSocketProcessors(List<TransmissorSocketProcessor> transmissorSocketProcessors) {
		this.transmissorSocketProcessors = transmissorSocketProcessors;
	}
	
	public static void main(String[] args) {
		
		try {
		
			List<TransmissorSocketProcessor> transmissorSocketProcessors = new ArrayList<>();
	
			CLJDashboardFrame dashboardFrame = new CLJDashboardFrame(transmissorSocketProcessors);
			dashboardFrame.setVisible(true);
	
			TransmissorSocketProcessor t1 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t1);
	
			Thread.sleep(30000);
			
			t1.setTotalPackeSentGB(234.56);
			t1.setTotalPacketSent(3456);
			
			TransmissorSocketProcessor t2 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t2);

			Thread.sleep(30000);

			t1.setTotalPackeSentGB(1234.56);
			t1.setTotalPacketSent(345609);

			TransmissorSocketProcessor t3 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t3);

			Thread.sleep(30000);

			TransmissorSocketProcessor t4 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t4);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
