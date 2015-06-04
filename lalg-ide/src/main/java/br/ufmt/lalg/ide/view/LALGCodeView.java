package br.ufmt.lalg.ide.view;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LALGCodeView extends JFrame {

	private JTextPane txpSaida;
	private Container ct;
	private JXTitledPanel pnlEditor;
	private JXPanel pnlCentral;
	
	public LALGCodeView(String lalgCode){
		
		ct = this.getContentPane();

		txpSaida = new JTextPane();
		
		txpSaida.setEditable(false);
		txpSaida.setText(lalgCode);
		
		JScrollPane scp = new JScrollPane(txpSaida);				
		
		pnlEditor = new JXTitledPanel(" LALGCode ",scp);
		
		pnlCentral = new JXPanel(new MigLayout("align center"));
		pnlCentral.add(pnlEditor,"dock center");
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvar();
			}

		});
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fechar();
			}
		});
		
		ct.add(pnlCentral,BorderLayout.CENTER);
		ct.add(btnSalvar,BorderLayout.SOUTH);
		//ct.add(btnSair,BorderLayout.SOUTH);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		this.setSize(350,500);
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void salvar() {
		
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());									
		int resultado = chooser.showSaveDialog(null);
			
		File arquivo;
		if(resultado == JFileChooser.APPROVE_OPTION){
			arquivo = chooser.getSelectedFile();
		}else{
			//Erro
			return;
		}							
					
		FileWriter writer;
		try {
			writer = new FileWriter(arquivo);
			writer.write(txpSaida.getText());
			writer.flush();
			
			writer.close();					
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		JOptionPane.showMessageDialog(null,"Arquivo Salvo com Sucesso");
		
		
	}
	
	private void fechar() {
		this.dispose();		
	}
}
