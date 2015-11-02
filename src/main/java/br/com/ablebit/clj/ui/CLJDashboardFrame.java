package br.com.ablebit.clj.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		
		setLayout(new WrapLayout());
		setSize(new Dimension(540, 540));
		setTitle("CLJ");

		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				loadNetworkInterfacePanels();
			}
		}, 0, 1, TimeUnit.SECONDS);
		
	}

	private void loadNetworkInterfacePanels() {

		for (TransmissorSocketProcessor transmissorSocketProcessor : transmissorSocketProcessors) {

			boolean found = false;
			
			for (Component c : Arrays.stream(getContentPane().getComponents())
					.filter(p -> p instanceof NetworkInterfacePanel)
					.filter(p -> transmissorSocketProcessor == NetworkInterfacePanel.class.cast(p)
							.getTransmissorSocketProcessor())
					.collect(Collectors.toList())) {
				
				NetworkInterfacePanel.class.cast(c).loadData();
				
				found = true;
				
			}

			if (!found) {
				
				NetworkInterfacePanel networkInterfacePanel = new NetworkInterfacePanel(transmissorSocketProcessor);
				networkInterfacePanels.add(networkInterfacePanel);
				
				this.add(networkInterfacePanel);
				
			}
			
		}
		
		for (Component c : Arrays.stream(getContentPane().getComponents())
                .filter(i -> !transmissorSocketProcessors.contains(NetworkInterfacePanel.class.cast(i).getTransmissorSocketProcessor()))
                .collect (Collectors.toList())) {
			
			remove(c);
			
		}

	}

	public List<TransmissorSocketProcessor> getTransmissorSocketProcessors() {
		return transmissorSocketProcessors;
	}


	public void setTransmissorSocketProcessors(List<TransmissorSocketProcessor> transmissorSocketProcessors) {
		this.transmissorSocketProcessors = transmissorSocketProcessors;
	}
	
	public static void main(String[] args) {
		
		try {
		
			List<TransmissorSocketProcessor> transmissorSocketProcessors = new CopyOnWriteArrayList<>();

			TransmissorSocketProcessor t1 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t1);
			
			CLJDashboardFrame dashboardFrame = new CLJDashboardFrame(transmissorSocketProcessors);
			dashboardFrame.setVisible(true);
	
			Thread.sleep(3000);
			
			t1.setTotalPacketSentGB(234.56);
			t1.setTotalPacketSent(3456);
			
			TransmissorSocketProcessor t2 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t2);

			Thread.sleep(3000);

			t1.setTotalPacketSentGB(1234.56);
			t1.setTotalPacketSent(345609);

			TransmissorSocketProcessor t3 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t3);

			Thread.sleep(3000);

			t2.setTotalPacketSentGB(234.56);
			t2.setTotalPacketSent(3456);
			
			TransmissorSocketProcessor t4 = new TransmissorSocketProcessor(null, InetAddress.getLoopbackAddress(), 5001, "29998494", 34645);
			transmissorSocketProcessors.add(t4);
			
			Thread.sleep(3000);
			
			transmissorSocketProcessors.remove(0);
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
