package br.ufmt.lalg.vm.view;

import br.ufmt.lalg.vm.model.MaquinaVirtual;
import com.explodingpixels.macwidgets.BottomBar;
import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.UnifiedToolBar;
import com.explodingpixels.swingx.EPButton;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class PrincipalView extends JXFrame{

	JXPanel pnlCentral,pnlSouth;
	JXTitledPanel pnlCode,pnlExec;
	Container ct;
	JXTextArea txaPrograma,txaExec;
	File arquivoAtual;
	
	
	public PrincipalView(){		
		initComponents();	
	}

	private void initComponents() {
		
		ct = this.getContentPane();

		txaPrograma = new JXTextArea();						
		txaExec = new JXTextArea();		

		txaExec.setLineWrap(true);
		txaPrograma.setLineWrap(true);
		
		JScrollPane scpCode = new JScrollPane(txaPrograma);
		scpCode.setPreferredSize(new Dimension(300,600));
		
		JScrollPane scpExec = new JScrollPane(txaExec);
		scpExec.setPreferredSize(new Dimension(300,600));
		
		pnlCentral = new JXPanel(new MigLayout("align center"));
		pnlExec = new JXTitledPanel(" Execu��o ",scpExec);
		pnlCode = new JXTitledPanel(" Novo arquivo ",scpCode);
		
		pnlCentral.add(pnlCode,"dock center");
		pnlCentral.add(pnlExec,"dock east, w 300px");
		
												
		ct.add(createUnifiedToolBar(),BorderLayout.NORTH);
		ct.add(pnlCentral,BorderLayout.CENTER);
		ct.add(createBottomBar(),BorderLayout.SOUTH);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		this.setSize(600,600);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		

	}
	
	private Component createUnifiedToolBar() {
		
		UnifiedToolBar toolBar = new UnifiedToolBar();	
		//toolBar.installWindowDraggerOnWindow(this);
		toolBar.disableBackgroundPainter();			
		this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
					
		EPButton btnAbrir = new EPButton("Abrir",criarImageIcon("/icons/folder_explore.png"));
		btnAbrir.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				abrirArquivo();
			}
		});
		EPButton btnNovo = new EPButton("Novo",criarImageIcon("/icons/script_add.png"));

		toolBar.addComponentToLeft(btnNovo);
		toolBar.addComponentToLeft(btnAbrir);
		toolBar.addComponentToLeft(MacWidgetFactory.createSpacer(30, 20));
		
		toolBar.addComponentToCenter(MacWidgetFactory.createEmphasizedLabel("LALG Exec"));
		
		toolBar.addComponentToRight(MacWidgetFactory.createSpacer(30, 20));
		toolBar.addComponentToRight(MacWidgetFactory.createEmphasizedLabel("Desenvolvido por Alvaro Viebrantz"));
		
		
		return toolBar.getComponent();
		
	}

	private Component createBottomBar() {
		
		BottomBar toolBar = new BottomBar(BottomBarSize.LARGE);	
									
		EPButton btnCompilar = new EPButton("Executar",criarImageIcon("/icons/wand.png"));
		btnCompilar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				executar();
			}
		});
		
		EPButton btnSair = new EPButton("Sair",criarImageIcon("/icons/cross.png"));				
		btnSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		toolBar.addComponentToCenter(btnCompilar);
		toolBar.addComponentToCenter(btnSair);		
						
		return toolBar.getComponent();
		
	}
			
	private void executar() {
		MaquinaVirtual hipo = new MaquinaVirtual(txaPrograma.getText());
		hipo.execute();
		String out = hipo.getOut().toString();
		txaExec.setText(out);
		
	}
	
	protected void salvarArquivo() {
		if(arquivoAtual == null){
			
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());									
			int resultado = chooser.showSaveDialog(null);
			
			File arquivo;
			if(resultado == JFileChooser.APPROVE_OPTION){
				arquivo = chooser.getSelectedFile();
			}else{
				//Erro
				return;
			}					
			arquivoAtual = arquivo;
			
		}
		FileWriter writer;
		try {
			writer = new FileWriter(arquivoAtual);
			writer.write(txaPrograma.getText());
			writer.flush();
			
			writer.close();					
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		pnlCode.setTitle(arquivoAtual.getName());
		JOptionPane.showMessageDialog(null,"Arquivo Salvo com Sucesso");
		
	}

	protected void abrirArquivo() {
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {						
				return "Apenas arquivos .blalg";
			}					
			@Override
			public boolean accept(File f) {						
				return f.getName().endsWith(".blalg");
			}			
			
		});
		
		int resultado = chooser.showOpenDialog(null);				
		
		File arquivo;
		if(resultado == JFileChooser.APPROVE_OPTION){
			arquivo = chooser.getSelectedFile();
		}else{
			//Erro
			return;
		}									
		
		try {
			FileReader reader = new FileReader(arquivo);
			StringBuffer buffer = new StringBuffer();
			int val = reader.read(); 
			while(val != -1){						
				buffer.append((char) val);
				val = reader.read();
			}
			
			reader.close();
			txaPrograma.setText(buffer.toString());					
			arquivoAtual = arquivo;
			pnlCode.setTitle(arquivoAtual.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private  ImageIcon criarImageIcon(String caminho) {
		java.net.URL imgURL = getClass().getResource(caminho);
	    if (imgURL != null) {
	    	return new ImageIcon(imgURL);
	    } else {
	    	System.err.println("Não foi possível carregar o arquivo de imagem: " + caminho);
	        return null;
	    }
	}
	
}
