package br.ufmt.lalg.ide.view;

import br.ufmt.lalg.compiler.lexico.AnalisadorLexico;
import br.ufmt.lalg.compiler.lexico.AnaliseLexicaException;
import br.ufmt.lalg.compiler.lexico.AnaliseLexicaUtil;
import br.ufmt.lalg.compiler.model.TabelaDeSimbolos;
import br.ufmt.lalg.compiler.model.TabelaDeSimbolosGlobal;
import br.ufmt.lalg.compiler.model.TabelaDeTokens;
import br.ufmt.lalg.compiler.sintatico.AnalisadorSintatico;
import br.ufmt.lalg.compiler.sintatico.AnaliseSintaticaException;
import br.ufmt.lalg.ide.view.tablemodel.TabelaDeSimbolosTableModel;
import br.ufmt.lalg.ide.view.tablemodel.TabelaDeTokensTableModel;
import com.explodingpixels.macwidgets.BottomBar;
import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.UnifiedToolBar;
import com.explodingpixels.swingx.EPButton;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;


public class PrincipalView extends JXFrame{

	JXPanel pnlCentral,pnlSouth;
	JXTitledPanel pnlEditor;
	Container ct;
	JCodeEditor txaPrograma;
	JTable tblSimbolos;
	JXTaskPane taskSimbolos;
	TabelaDeTokensTableModel model;
	File arquivoAtual;
	boolean compilado = false;
	
	
	public PrincipalView(){		
		initComponents();	
	}

	private void initComponents() {
		
		ct = this.getContentPane();

		//this.setJMenuBar(createMenuBar());

		txaPrograma = new JCodeEditor();		
		
		pnlEditor = new JXTitledPanel(" Novo arquivo ",txaPrograma);
		
		pnlCentral = new JXPanel(new MigLayout("align center"));
		pnlCentral.add(createTaskPane(),"dock west");
		
		pnlCentral.add(pnlEditor,"dock center");
		
												
		ct.add(createUnifiedToolBar(),BorderLayout.NORTH);
		ct.add(pnlCentral,BorderLayout.CENTER);
		ct.add(createBottomBar(),BorderLayout.SOUTH);

		this.setLocationByPlatform(true);
		this.setVisible(true);
		this.setSize(1024,600);
		this.setResizable(true);
		//this.setExtendedState(MAXIMIZED_BOTH);
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
		EPButton btnSalvar = new EPButton("Salvar",criarImageIcon("/icons/bullet_disk.png"));
		btnSalvar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvarArquivo();
			}
		});
		
		//btnAbrir.putClientProperty("JButton.buttonType", "textured");
		toolBar.addComponentToLeft(btnNovo);
		toolBar.addComponentToLeft(btnAbrir);
		toolBar.addComponentToLeft(btnSalvar);
		toolBar.addComponentToLeft(MacWidgetFactory.createSpacer(30, 20));
		
		toolBar.addComponentToCenter(MacWidgetFactory.createEmphasizedLabel("LALG Compiler"));
		
		toolBar.addComponentToRight(MacWidgetFactory.createSpacer(30, 20));
		toolBar.addComponentToRight(MacWidgetFactory.createEmphasizedLabel("Desenvolvido por Alvaro Viebrantz"));
		
		
		return toolBar.getComponent();
		
	}

	private Component createBottomBar() {
		
		BottomBar toolBar = new BottomBar(BottomBarSize.LARGE);	
									
		EPButton btnCompilar = new EPButton("Compilar",criarImageIcon("/icons/wand.png"));
		btnCompilar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				compilar();
			}
		});
		
		EPButton btnGerarCodigo = new EPButton("Lalg Code",criarImageIcon("/icons/color_swatch.png"));
		btnGerarCodigo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String codigo = compilar();
				if(codigo != null){
					new LALGCodeView(codigo);
				}
				
			}
		});
		
		EPButton btnSair = new EPButton("Sair",criarImageIcon("/icons/cross.png"));				
		btnSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		EPButton btnVerCodigo = new EPButton("Ver Codigo Fonte",criarImageIcon("/icons/script_code_red.png"));
		btnVerCodigo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				verCodigoFonte();
			}
		});

		toolBar.addComponentToCenter(btnCompilar);
		toolBar.addComponentToCenter(btnGerarCodigo);
		toolBar.addComponentToCenter(btnVerCodigo);
		toolBar.addComponentToCenter(btnSair);		
						
		return toolBar.getComponent();
		
	}

	private JXTaskPaneContainer createTaskPane() {

		JXTaskPaneContainer tpcTarefas = new JXTaskPaneContainer();
		
		JXTaskPane taskTokens = new JXTaskPane(" Tabela de Tokens ");
		taskTokens.setAnimated(false);

		model = new TabelaDeTokensTableModel(new TabelaDeTokens());
		tblSimbolos = new JTable(model);
		JScrollPane scp = new JScrollPane(tblSimbolos);		
		scp.setPreferredSize(new Dimension(400,200));		
		taskTokens.add(scp);
		
		taskSimbolos = new JXTaskPane(" Tabela de Simbolos ");
		taskSimbolos.setAnimated(false);
		
		tpcTarefas.add(taskTokens);
		tpcTarefas.add(taskSimbolos);
		tpcTarefas.setPreferredSize(new Dimension(300,getHeight()));
		tpcTarefas.setBackground(new Color(100,100,100));

		return tpcTarefas;
	}
	
	private JPanel createMenuButton(){
		Font f = new Font(Font.DIALOG,Font.BOLD,10);

		JButton btnCompilar= new JButton("Compilar");
		btnCompilar.setFont(f);
		btnCompilar.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnCompilar.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCompilar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				
				compilar();
				
			}
		});
		
		JButton btnFonte= new JButton("Ver codigo-fonte");
		btnFonte.setFont(f);
		btnFonte.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnFonte.setHorizontalTextPosition(SwingConstants.CENTER);
		btnFonte.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				verCodigoFonte();
				
			}
		});
		
		JButton btnSair = new JButton("Sair");
		btnSair.setFont(f);
		btnSair.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnSair.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				System.exit(0);
			}
		});
		
		FlowLayout l = new FlowLayout();
		l.setAlignment(FlowLayout.CENTER);
		pnlSouth = new JXPanel(l);


		GlossPainter gloss = new GlossPainter(new Color(100,100,100,150),GlossPainter.GlossPosition.TOP);
		MattePainter matte = new MattePainter(new Color(0,0,0,255));
		pnlSouth.setBackgroundPainter(new CompoundPainter(matte,gloss));
		pnlSouth.add(btnCompilar);
		pnlSouth.add(btnFonte);		
		pnlSouth.add(btnSair);

		//pnlNorth.setBackground(new Color(0,0,100,100));

		return pnlSouth;
	}
	
	private void verCodigoFonte() {
		AnalisadorLexico al = new AnalisadorLexico(txaPrograma.getConteudo());
		try {
			al.fazerAnalise();					
			new CodigoView(AnaliseLexicaUtil.getCodigoFonteEmHTML(al.tabela));
			
		} catch (AnaliseLexicaException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
		
	}

	private String compilar() {
		AnalisadorLexico al = new AnalisadorLexico(txaPrograma.getConteudo());
		AnalisadorSintatico as = new AnalisadorSintatico(al.tabela);
		try {
			al.fazerAnalise();
			model = new TabelaDeTokensTableModel(al.tabela);
			tblSimbolos.setModel(model);
			tblSimbolos.revalidate();
			tblSimbolos.updateUI();
			as.fazerAnalise();
			mostraTabelasDeSimbolos(as.tabelaDeSimbolos);
											
			return as.getCodigoIntemediario();
			
		} catch (AnaliseLexicaException e) {					
			JOptionPane.showMessageDialog(null,e.getMessage());
		} catch (AnaliseSintaticaException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
		
		return null;
		
	}

	private void mostraTabelasDeSimbolos(TabelaDeSimbolosGlobal tabelaDeSimbolos) {
		
		taskSimbolos.removeAll();
		
		JXTaskPaneContainer tpcTabelas = new JXTaskPaneContainer();
		
		JXTaskPane taskGlobal = new JXTaskPane(" Global ");
		taskGlobal.setAnimated(false);
								
		TabelaDeSimbolosTableModel model = new TabelaDeSimbolosTableModel(tabelaDeSimbolos);
		JTable tbl = new JTable(model);
		JScrollPane scp = new JScrollPane(tbl);	
		scp.setPreferredSize(new Dimension(400,150));
		
		taskGlobal.add(scp);
		
		tpcTabelas.add(taskGlobal);
		
		List<TabelaDeSimbolos> tabelas = tabelaDeSimbolos.getTabelasDeSimbolos();
		
		for(TabelaDeSimbolos tblS : tabelas){
			JXTaskPane task = new JXTaskPane(tblS.getIdentificador());
			task.setAnimated(false);
			
			TabelaDeSimbolosTableModel tableModel = new TabelaDeSimbolosTableModel(tblS);
			JTable table = new JTable(tableModel);
			JScrollPane scroll = new JScrollPane(table);
			scroll.setPreferredSize(new Dimension(400,150));
			
			task.add(scroll);
			
			tpcTabelas.add(task);
		
		}				
		
		taskSimbolos.add(tpcTabelas);			
	
	}

	private JMenuBar createMenuBar(){

		JMenuBar jmb;
		JMenu jmArquivo,jmCompilador,jmVisualizar;
		JMenuItem jmiAbrir,jmiNovo,jmiSalvar,jmiVisualizarTabelas;

		jmb = new JMenuBar();
		jmArquivo = new JMenu("Arquivo");
		jmVisualizar = new JMenu("Visualizar");

		jmiNovo = new JMenuItem("Novo arquivo...");
		jmiAbrir = new JMenuItem("Abrir arquivo...");
		jmiAbrir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				abrirArquivo();
			}


		});
		jmiSalvar = new JMenuItem("Salvar arquivo");
		jmiSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				salvarArquivo();
				
			}
		});
			
		

		jmiVisualizarTabelas = new JMenuItem("Visualizar Tabelas");		

		jmArquivo.add(jmiNovo);
		jmArquivo.add(jmiAbrir);
		jmArquivo.add(jmiSalvar);
		jmVisualizar.add(jmiVisualizarTabelas);

		jmb.add(jmArquivo);
		jmb.add(jmVisualizar);		
		

		return jmb;
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
			writer.write(txaPrograma.getTextArea().getText());
			writer.flush();
			
			writer.close();					
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		pnlEditor.setTitle(arquivoAtual.getName());
		JOptionPane.showMessageDialog(null,"Arquivo Salvo com Sucesso");
		
	}

	protected void abrirArquivo() {
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {						
				return "Apenas arquivos .lalg";
			}					
			@Override
			public boolean accept(File f) {						
				return f.getName().endsWith(".lalg");
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
			txaPrograma.getTextArea().setText(buffer.toString());					
			arquivoAtual = arquivo;
			pnlEditor.setTitle(arquivoAtual.getName());
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
