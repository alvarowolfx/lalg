package br.ufmt.lalg.ide.view;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CodigoView extends JFrame {

	private JTextPane txpSaida;
	private Container ct;
	private JXTitledPanel pnlEditor;
	private JXPanel pnlCentral;
	
	public CodigoView(String htmlContent){
		
		ct = this.getContentPane();

		txpSaida = new JTextPane();
		txpSaida.setContentType("text/html");
		txpSaida.setEditable(false);
		txpSaida.setText(htmlContent);
		
		JScrollPane scp = new JScrollPane(txpSaida);				
		
		pnlEditor = new JXTitledPanel(" Saida ",scp);
		
		pnlCentral = new JXPanel(new MigLayout("align center"));
		pnlCentral.add(pnlEditor,"dock center");
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fechar();
			}
		});
		
		ct.add(pnlCentral,BorderLayout.CENTER);
		ct.add(btnSair,BorderLayout.SOUTH);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		this.setSize(350,500);
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void fechar() {
		this.dispose();		
	}
}
