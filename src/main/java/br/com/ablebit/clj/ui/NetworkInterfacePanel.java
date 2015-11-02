package br.com.ablebit.clj.ui;

import br.com.ablebit.clj.net.TransmissorSocketProcessor;

/**
 *
 * @author lfranchi
 */
public class NetworkInterfacePanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = -6397209139538789206L;
	
	private TransmissorSocketProcessor transmissorSocketProcessor;
	
    public NetworkInterfacePanel(TransmissorSocketProcessor transmissorSocketProcessor) {
    	this.transmissorSocketProcessor = transmissorSocketProcessor;
    	
        initComponents();
        
        loadData();
        
    }
    
    public void loadData() {
    	
    	jLabel1.setText(getTransmissorSocketProcessor().getInetAddress().getHostName());
    	lblIp.setText(getTransmissorSocketProcessor().getInetAddress().getHostAddress());
    	lblTotalSent.setText(String.valueOf(getTransmissorSocketProcessor().getTotalPacketSent()));
    	lblTotalSentGB.setText(String.valueOf(getTransmissorSocketProcessor().getTotalPacketSentGB()));
    	
    	validate();
    	
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblIp = new javax.swing.JLabel();
        lblTotalSent = new javax.swing.JLabel();
        lblTotalSentGB = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("NOME DA INTERFACE");

        jLabel2.setText("IP: ");

        jLabel3.setText("Sent:");

        jLabel4.setText("Sent (GBs):");

        lblIp.setText("XXX.XXX.XXX.XXX:PPPP");

        lblTotalSent.setText("NNNNNNNNNNN");

        lblTotalSentGB.setText("NNNGB");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotalSent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(lblTotalSentGB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblIp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalSent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblTotalSentGB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }     


	public TransmissorSocketProcessor getTransmissorSocketProcessor() {
		return transmissorSocketProcessor;
	}

	public void setTransmissorSocketProcessor(TransmissorSocketProcessor transmissorSocketProcessor) {
		this.transmissorSocketProcessor = transmissorSocketProcessor;
	}


	// Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblIp;
    private javax.swing.JLabel lblTotalSent;
    private javax.swing.JLabel lblTotalSentGB;
    // End of variables declaration                   
}
