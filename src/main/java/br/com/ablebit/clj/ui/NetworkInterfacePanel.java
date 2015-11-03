package br.com.ablebit.clj.ui;

import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import br.com.ablebit.clj.net.TransmissorSocketProcessor;

/**
 *
 * @author lfranchi
 */
public class NetworkInterfacePanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = -6397209139538789206L;
	
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblIp;
    private javax.swing.JLabel lblTotalSent;
    private javax.swing.JLabel lblTotalSentMb;
    private javax.swing.JLabel lblBandwidth;

	private TransmissorSocketProcessor transmissorSocketProcessor;
	
	private TimeSeries series;
	private ChartPanel chartPanel;
    
    public NetworkInterfacePanel(TransmissorSocketProcessor transmissorSocketProcessor) {
    	this.transmissorSocketProcessor = transmissorSocketProcessor;
    	
    	initChart();
    	
        initComponents();
        
        loadData();
        
    }
    
    private void initChart() {
    	
    	this.series = new TimeSeries("Mb", Millisecond.class);
    	final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, dataset, true, true, false);
		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(60000.0); // 60 seconds
		axis = plot.getRangeAxis();
		axis.setRange(0.0, 10.0);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        
    }
    
    public void loadData() {
    	
    	//TODO: Colocar o nome da Operadora.
    	setBorder(new TitledBorder("Nome da Operadora"));
    	
    	lblIp.setText(getTransmissorSocketProcessor().getInetAddress().getHostAddress());
    	lblTotalSent.setText(String.valueOf(getTransmissorSocketProcessor().getTotalPacketSent()));
    	lblTotalSentMb.setText(String.format("%.2f", getTransmissorSocketProcessor().getTotalPacketSentMb().get()));
    	lblBandwidth.setText(String.format("%.2f Mbps",getTransmissorSocketProcessor().getBandwidth()));
    	
    	this.series.add(new Millisecond(), getTransmissorSocketProcessor().getBandwidth());
    	
    	validate();
    	
    }

    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblIp = new javax.swing.JLabel();
        lblTotalSent = new javax.swing.JLabel();
        lblTotalSentMb = new javax.swing.JLabel();
        lblBandwidth = new javax.swing.JLabel();

        jLabel2.setText("IP: ");

        jLabel3.setText("Enviados:");

        jLabel4.setText("Enviados (Mb):");
        
        jLabel5.setText("Bandwidth:");

        lblIp.setText("XXX.XXX.XXX.XXX:PPPP");

        lblTotalSent.setText("NNNNNNNNNNN");

        lblTotalSentMb.setText("NNNMb");
        
        lblBandwidth.setText("NNN.NN");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                	.addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotalSent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(lblTotalSentMb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                			.addComponent(lblBandwidth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartPanel)
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
                    .addComponent(lblTotalSentMb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lblBandwidth))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }     


	public TransmissorSocketProcessor getTransmissorSocketProcessor() {
		return transmissorSocketProcessor;
	}

	public void setTransmissorSocketProcessor(TransmissorSocketProcessor transmissorSocketProcessor) {
		this.transmissorSocketProcessor = transmissorSocketProcessor;
	}


}
