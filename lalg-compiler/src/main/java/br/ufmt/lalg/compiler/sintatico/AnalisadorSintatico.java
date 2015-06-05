package br.ufmt.lalg.compiler.sintatico;

import br.ufmt.lalg.compiler.model.GeradorCodigoIntemediario;
import br.ufmt.lalg.compiler.model.TabelaDeSimbolos;
import br.ufmt.lalg.compiler.model.TabelaDeSimbolosGlobal;
import br.ufmt.lalg.compiler.model.TabelaDeTokens;
import br.ufmt.lalg.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnalisadorSintatico {

	private TabelaDeTokens tabela;
	private Token tokenAtual;
	private Categoria categoriaAtual;
	private Iterator<Token> it;
	private TabelaDeSimbolos tabelaAtual;
	private PilhaDeExpressao pilhaDeExpressoes = new PilhaDeExpressao();
	private GeradorCodigoIntemediario gerador = new GeradorCodigoIntemediario();
	private boolean modoDeclaracao = false;
	public TabelaDeSimbolosGlobal tabelaDeSimbolos = new TabelaDeSimbolosGlobal();
	
	
	private List<Simbolo> variaveisAtuais = new ArrayList<Simbolo>(); 
	
	public AnalisadorSintatico(TabelaDeTokens tabela){
		this.tabela = tabela;
	}
	
	public void fazerAnalise() throws AnaliseSintaticaException{

		it = tabela.getTokens().iterator();
		tokenAtual = it.next();
		tabelaAtual = tabelaDeSimbolos;
		
		programa();
				
	}
	
	private void programa() throws AnaliseSintaticaException{		
		
		if(tokenAtual.getConteudo().contentEquals("program")){
			
			proximoToken();
			
			if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
				
				addIdentificadorDePrograma();
				
				proximoToken();	
				
				gerador.geraCodigoInit();
				
				corpo();
				
				if(tokenAtual.getConteudo().contentEquals(".")){
					
					if(it.hasNext()){						
						//Erro
						proximoToken();
						throw new AnaliseSintaticaException("Não deve-se ter nada após o final de programa",tokenAtual);
					}
					
				}else{
					//Erro
					throw new AnaliseSintaticaException("Deve-se terminar um programa com ponto",tokenAtual);
					
				}
				
			}else{
				//Erro
				throw new AnaliseSintaticaException("Identificador inválido para program",tokenAtual);
				
			}
			
		}else{
			//Erro 
			throw new AnaliseSintaticaException("Deve-se iniciar o programa com 'program'",tokenAtual);
		}
	}
		
	private void corpo() throws AnaliseSintaticaException {
		
		dc();
		
		if(tokenAtual.getConteudo().contentEquals("begin")){
			
			modoDeclaracao = false;
			proximoToken();
			comandos();
			
			if(tokenAtual.getConteudo().contentEquals("end")){
				
				
				proximoToken();
				
				gerador.fimPrograma();
			}else{				
				//Erro
				throw new AnaliseSintaticaException("Corpo do programa não possui 'end'",tokenAtual);
				
			}
						
			
		}else{
			//Erro
			if(tokenAtual.getConteudo().contentEquals("var") || tokenAtual.getConteudo().contentEquals("procedure")){
				throw new AnaliseSintaticaException("Para declarar mais variaveis ou procedimentos, coloque ';' depois do tipo",tokenAtual);
			}else{
				if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
					throw new AnaliseSintaticaException("Esperado 'var' ou  'procedure' mas foi encontrado "+tokenAtual.getConteudo(),tokenAtual);
				}else{
					throw new AnaliseSintaticaException("Deve-se iniciar corpo do programa com 'begin' mas foi encontrado "+tokenAtual.getConteudo(),tokenAtual);
				}
			}
			
		}
		
	}

	private void dc() throws AnaliseSintaticaException {
				
		
		if(tokenAtual.getConteudo().contentEquals("var")){
			
			iniciaDeclaracaoVariavel();
			
			proximoToken();
			dc_v();
			mais_dc();
			
		}else{
			
			if(tokenAtual.getConteudo().contentEquals("procedure")){
					
				modoDeclaracao = true;
				proximoToken();
				dc_p();
				mais_dc();
				
			}		
			
		}
				
	}	

	private void mais_dc() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(";")){		
			proximoToken();
			dc();	
		}		
		
	}

	private void tipo_var() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals("integer") || tokenAtual.getConteudo().contentEquals("real")){		
			
			addSimbolosComTipo(tokenAtual.getConteudo());			
			proximoToken();
					
		}else{
			//Erro
			throw new AnaliseSintaticaException("Variaveis devem ser do tipo real ou integer",tokenAtual);
		}
	}

	private void dc_v() throws AnaliseSintaticaException {
		
		variaveis();
		
		if(tokenAtual.getConteudo().contentEquals(":")){
			
			proximoToken();
			tipo_var();
			
		}else{			
			//Erro
			throw new AnaliseSintaticaException("Declaração de variaveis de ser feita do tipo <variaveis> : <tipoDaVariavel>",tokenAtual);
		}
				
	}
	
	private void variaveis() throws AnaliseSintaticaException {
		
		if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
			
			addVar();						
			proximoToken();
			mais_var();
			
		}else{			
			//Erro
			throw new AnaliseSintaticaException("Esperado identificador válido",tokenAtual);
		}
		
		
	}
	
	private void mais_var() throws AnaliseSintaticaException{
		
		if(tokenAtual.getConteudo().contentEquals(",")){
			
			proximoToken();
			variaveis();
			
		}
		
	}

	private void dc_p() throws AnaliseSintaticaException {
						
		if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
			
			int incondicional = gerador.getLabelAtual();
			gerador.adicionaDesvioIncondicional();
			
			iniciaProcedimento();			
			proximoToken();
			parametros();
			corpo_p();
			
			gerador.finalizaProcedimento(incondicional,tabelaAtual.getEnderecoAtual()-tabelaDeSimbolos.getEnderecoAtual());
			
			finalizaProcedimento();
			
		}else{
			//Erro
			throw new AnaliseSintaticaException("Esperado identificador válido para procedimento",tokenAtual);
		}
		
	}
	
	private void parametros() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals("(")){
		
			proximoToken();
			lista_par();
			
			if(tokenAtual.getConteudo().contentEquals(")")){
				
				proximoToken();								
				
			}else{
				//Erro
				throw new AnaliseSintaticaException("Esperado ')' para fechar procedimento",tokenAtual);
			}			
		}
		
	}

	private void corpo_p() throws AnaliseSintaticaException {
		
		dc_loc();
		
		if(tokenAtual.getConteudo().contentEquals("begin")){
			
			modoDeclaracao = false;
			proximoToken();
			comandos();
			
			if(tokenAtual.getConteudo().contentEquals("end")){
				proximoToken();
				
			}else{
				//Erro
				throw new AnaliseSintaticaException("Procedimento deve ser finalizado com 'end'",tokenAtual);				
				
			}
			
		}else{
			//Erro
			throw new AnaliseSintaticaException("Procedimento deve ser iniciado com 'begin'",tokenAtual);
		}
		
	}
	
	private void dc_loc() throws AnaliseSintaticaException {
						
		if(tokenAtual.getConteudo().contentEquals("var")){
			
			iniciaDeclaracaoVariavel();
			proximoToken();
			dc_v();
			mais_dcloc();
			
		}		
	}

	private void mais_dcloc() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(";")){
			
			proximoToken();
			dc_loc();
			
		}
		
	}

	private void lista_par() throws AnaliseSintaticaException {
		
		variaveis();
		
		if(tokenAtual.getConteudo().contentEquals(":")){
			
			proximoToken();
			tipo_var();
			mais_par();
			
		}else{
			//Erro
			throw new AnaliseSintaticaException("Declaração de parametros de ser feita do tipo <variaveis> : <tipoDaVariavel>",tokenAtual);
		}
		
	}
	
	private void mais_par() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(";")){
			
			proximoToken();
			lista_par();
			
		}
		
		
	}

	private void comandos() throws AnaliseSintaticaException {		
		
		comando();
		mais_comandos();
		
		
		
	}

	private void comando() throws AnaliseSintaticaException {
		
		if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
						
			verificaExistenciaToken(true);
			
			iniciaPilhaDeExpressoes();
			
			empilhaItemDeExpressao();
			
			proximoToken();							
			
			
			restoIdent();
			
			variaveisAtuais.clear();
			
		}else{
			
			if(tokenAtual.getConteudo().contentEquals("read") || tokenAtual.getConteudo().contentEquals("write")){
				
				String func = tokenAtual.getConteudo();
				
				proximoToken();
				
				if(tokenAtual.getConteudo().contentEquals("(")){
					
					proximoToken();
					variaveis();
					
					if(tokenAtual.getConteudo().contentEquals(")")){
						
						verificaExistenciaToken(false);
						
						if(func.contentEquals("read")){
							gerador.leituraVariavel(variaveisAtuais);
						}else{
							gerador.escritaVariaveis(variaveisAtuais);
						}
						
						proximoToken();
						variaveisAtuais.clear();
						
					}else{
						//Erro
						throw new AnaliseSintaticaException("Esperado ')' para fechar o procedimento",tokenAtual);					
					}														
				}else{
					//Erro
					throw new AnaliseSintaticaException("Esperado '(' depois de"+tokenAtual.getConteudo(),tokenAtual);
				}
				
				
			}else{
				
				if(tokenAtual.getConteudo().contentEquals("while")){
					
					int inicioWhile = gerador.getLabelAtual();
					
					proximoToken();
					condicao();
					
					int desvioSeFalso = gerador.getLabelAtual()-1;
					
					if(tokenAtual.getConteudo().contentEquals("do")){
						
						proximoToken();
						comandos();
						
						if(tokenAtual.getConteudo().contentEquals("$")){
														
							gerador.finalizaCondicional(inicioWhile, desvioSeFalso);
							proximoToken();
							
						}else{
							//Erro finalizar com $ o while
							throw new AnaliseSintaticaException("Esperado '$' para delimitar o fim do while",tokenAtual);
							
						}
						
					}else{
						//Erro sintaxe do while é 'while <condicao> do'
						throw new AnaliseSintaticaException("A sintaxe do while é 'while <condicao> do'	",tokenAtual);
					}
					
				}else{									
					
					if(tokenAtual.getConteudo().contentEquals("if")){

						proximoToken();
						condicao();
						
						int desvioSeFalso = gerador.getLabelAtual()-1;
						
						if(tokenAtual.getConteudo().contentEquals("then")){
							
							proximoToken();
							comandos();
							
							int incondicional = gerador.getLabelAtual();
							gerador.adicionaDesvioIncondicional();
														
							pfalsa();
																					
							if(tokenAtual.getConteudo().contentEquals("$")){
																
								if(incondicional == gerador.getLabelAtual()-1){
									//Não teve else
									gerador.removeInstrucao(incondicional);
									gerador.finalizaCondicionalSemElse(desvioSeFalso);
								}else{
									//Teve else
									gerador.finalizaCondicionalComElse(incondicional+1, desvioSeFalso, incondicional);
								}
								
								proximoToken();
								
							}else{
								//Erro finalizar com $ o if
								throw new AnaliseSintaticaException("Esperado '$' para delimitar o fim do if",tokenAtual);
								
							}
							
						}else{
							//Erro sintaxe do if é 'if <condicao> then'
							throw new AnaliseSintaticaException("A sintaxe do if é 'if <condicao> then'	",tokenAtual);
						}
					}else{
						//Erro esperado algum comando válido
						throw new AnaliseSintaticaException("Esperado algum comando válido mas foi encontrado "+tokenAtual.getConteudo(),tokenAtual);
					}
					
				}
				
				
			}
			
		}
		
	}
	

	private void mais_comandos() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(";")){
			
			proximoToken();
			comandos();
						
			
		}else{
			
			if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
				throw new AnaliseSintaticaException("Insira ';' para seguir com mais comandos",tokenAtual);			
			}else{
				String[] sinais = {"=",":=","<>",">=","<=","<",">","+","*","-","/"};		
				for (String rel : sinais) {
					if(tokenAtual.getConteudo().contentEquals(rel)){												
						throw new AnaliseSintaticaException("Expressão não pode ser avaliada",tokenAtual);
					}
				}
				
				String[] reservadas = {"integer","real","read","write","if","while"};		
				for (String rel : reservadas) {
					if(tokenAtual.getConteudo().contentEquals(rel)){												
						throw new AnaliseSintaticaException("Insira ';' para seguir com mais comandos",tokenAtual);						
					}
				}
				
			}
						
			
			
			
		}
		
	}
	
	private void restoIdent() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(":=")){
			
			Simbolo proc = tabelaDeSimbolos.busca(pilhaDeExpressoes.peek().getConteudo());
			if(proc != null){
				if(proc.getCategoria() == Categoria.PROCEDIMENTO){
					throw new AnaliseSintaticaException("Apenas variáveis são permitidas",pilhaDeExpressoes.peek().getToken());
				}
			}
			
			empilhaItemDeExpressao();
			
			proximoToken();
									
			expressao();			
			
			PilhaDeExpressao temp = (PilhaDeExpressao) pilhaDeExpressoes.clone();
			
			avaliaExpressao();
			 
			gerador.atribuicaoVar(temp);
			
		}else{
			
			lista_arg();
			
		}
		
	}
	

	private void lista_arg() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals("(")){
			
									
			proximoToken();
			argumentos();
			
			verificaParametros();
			
			Procedimento proc = (Procedimento) tabelaDeSimbolos.busca(pilhaDeExpressoes.peek().getConteudo());
			gerador.chamadaProcedimento(variaveisAtuais, proc.getPrimeiraInstrucao().intValue());
			
			if(tokenAtual.getConteudo().contentEquals(")")){
				
				proximoToken();
				variaveisAtuais.clear();
				
			}else{
				//Erro
				throw new AnaliseSintaticaException("Esperado ')' para fechar o procedimento",tokenAtual);					
			}														
		}
		
	}

	private void argumentos() throws AnaliseSintaticaException {

		if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
			
			verificaExistenciaToken(false);
			
			addVar();
			
			proximoToken();
			mais_ident();
			
		}else{
			//Erro
			throw new AnaliseSintaticaException("Esperado identificador válido para procedimento",tokenAtual);
		}
	}

	private void mais_ident() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals(";")){
			
			proximoToken();
			argumentos();
			
		}
		
	}

	private void expressao() throws AnaliseSintaticaException {
		
		
		termo();		
		outros_termos();		
		
		
	}



	private void termo() throws AnaliseSintaticaException {
		
				
		op_un();
		fator();		
		mais_fatores();		
		
	}

	private void op_un() {
		
		if(tokenAtual.getConteudo().contentEquals("+") || tokenAtual.getConteudo().contentEquals("-")){
			empilhaItemDeExpressao();
			proximoToken();
		}
		
	}

	private void fator() throws AnaliseSintaticaException {
				
		if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR || tokenAtual.getTipo() == TipoToken.NUMERO_INTEIRO || tokenAtual.getTipo() == TipoToken.NUMERO_REAL){
			
			empilhaItemDeExpressao();
			if(tokenAtual.getTipo() == TipoToken.IDENTIFICADOR){
				verificaExistenciaToken(false);
			}
			proximoToken();
			
		}else{
			 if(tokenAtual.getConteudo().contentEquals("(")){
				 
				 empilhaItemDeExpressao();
				 proximoToken();
				 expressao();
				 
				 if(tokenAtual.getConteudo().contentEquals(")")){
					 empilhaItemDeExpressao();
					 proximoToken();					 					 
					 
				 }else{
						//Erro
						throw new AnaliseSintaticaException("Esperado ')' para completar par de parenteses",tokenAtual);					
				}														
			}else{
				//Erro
				throw new AnaliseSintaticaException("Esperado identificador , numero inteiro , numero real ou expressao",tokenAtual);
			}
			
		}
		
	}

	private void mais_fatores() throws AnaliseSintaticaException {
				
		if(tokenAtual.getConteudo().contentEquals("*") || tokenAtual.getConteudo().contentEquals("/")){				
				
				
				op_mul();
				fator();
				mais_fatores();
				
		}
		
	}

	private void op_mul() {
		
		empilhaItemDeExpressao();
		proximoToken();
		
	}

	private void outros_termos() throws AnaliseSintaticaException {
				
		if(tokenAtual.getConteudo().contentEquals("+") || tokenAtual.getConteudo().contentEquals("-")){
			
			op_ad();
			termo();
			outros_termos();
		}
		
	}
	
	private void op_ad() {
		
		empilhaItemDeExpressao();
		proximoToken();
		
	}

	private void pfalsa() throws AnaliseSintaticaException {
		
		if(tokenAtual.getConteudo().contentEquals("else")){
			
			proximoToken();			
			comandos();
			
		}
		
	}

	private void condicao() throws AnaliseSintaticaException {
		
		Simbolo lval = null;
		Simbolo rval = null;
		PilhaDeExpressao lstack = null;
		PilhaDeExpressao rstack = null;
		Token condicao = null;
		
		iniciaPilhaDeExpressoes();		
		expressao();
		
		lstack = (PilhaDeExpressao) pilhaDeExpressoes.clone();
		avaliaCondicao();
		
		lval = pilhaDeExpressoes.pop();
		condicao = tokenAtual;		
		
		relacao();
		
		iniciaPilhaDeExpressoes();
		expressao();
		
		rstack = (PilhaDeExpressao) pilhaDeExpressoes.clone();
		avaliaCondicao();
		
		rval = pilhaDeExpressoes.pop();
		
		if(!rval.getTipo().contentEquals(lval.getTipo())){
			throw new AnaliseSintaticaException("Os dois lados da condição devem ser do mesmo tipo",tokenAtual);
		}
		
		gerador.expressaoCondicional(lstack,rstack,condicao);
		
		
	}

	private void relacao() throws AnaliseSintaticaException {
		
		String[] relacoes = {"=","<>",">=","<=","<",">"};		
		for (String rel : relacoes) {
			if(tokenAtual.getConteudo().contentEquals(rel)){												
				proximoToken();
				return;
			}
		}
		
		throw new AnaliseSintaticaException("Esperado simbolo relacional válido ",tokenAtual);

		
	}

	private void proximoToken(){
		if(it.hasNext()){
			tokenAtual = it.next();
		}
	}

	private void addVar(){		
		
		if(modoDeclaracao){
			variaveisAtuais.add(new Simbolo(tokenAtual.getConteudo(),categoriaAtual, tokenAtual, null,tabelaAtual.getEnderecoAtual()));
			tabelaAtual.addEnderecoAtual();
		}else{
			
			Simbolo s = null;
			s = tabelaAtual.busca(tokenAtual.getConteudo());
			if(s == null){
				s = tabelaDeSimbolos.busca(tokenAtual.getConteudo());
				
				if(s == null){					
					s = new Simbolo(tokenAtual.getConteudo(),categoriaAtual, tokenAtual, null,0);
				}
			}
			s.setToken(tokenAtual);
			variaveisAtuais.add(s);
		}
						
	}
	
	private void iniciaDeclaracaoVariavel(){
		categoriaAtual = Categoria.VARIAVEL;
		modoDeclaracao = true;
	}
	
	private void iniciaProcedimento() throws AnaliseSintaticaException{
		modoDeclaracao = true;
		Simbolo sim = tabelaAtual.busca(tokenAtual.getConteudo());
		if(sim==null){
			tabelaDeSimbolos.novoProcedimento(tokenAtual);
			tabelaAtual = tabelaDeSimbolos.buscaTabelaDeSimbolos(tokenAtual.getConteudo());			
			tabelaAtual.setEnderecoAtual(tabelaDeSimbolos.getEnderecoAtual()+1);
			
			tabelaDeSimbolos.busca(tokenAtual.getConteudo()).setPrimeiraInstrucao(gerador.getLabelAtual());
			
			categoriaAtual = Categoria.PARAMETRO;	
		}else{
			//Erro
			if(sim.getCategoria() == Categoria.PROCEDIMENTO){
				throw new AnaliseSintaticaException(String.format("Identificador inválido para procedimento, %s já existe com o mesmo nome",sim.getCategoria()),sim.getToken());
			}else{
				throw new AnaliseSintaticaException(String.format("Identificador inválido para procedimento, %s já existe com o mesmo nome com o tipo %s",sim.getCategoria(),sim.getTipo()),sim.getToken());
			}
		}					
		
	}
	
	private void finalizaProcedimento(){
		
		/* Debugar ondem dos parametros do procedimento
		Procedimento proc = (Procedimento) tabelaDeSimbolos.busca(tabelaAtual.getIdentificador());
		System.out.println(Arrays.toString(proc.getParametros().toArray()));
		*/		
		tabelaAtual = tabelaDeSimbolos;
		categoriaAtual = Categoria.VARIAVEL;
		modoDeclaracao = true;
	}
	
	private void verificaExistenciaToken(boolean duvida) throws AnaliseSintaticaException {	
		
		//Uma variável
		if(variaveisAtuais.size() == 0){
			Simbolo s = null;
			//Verifica no escopo atual		
			s = tabelaAtual.busca(tokenAtual.getConteudo());
			if( s == null){
				//Verifica no escopo global
				s = tabelaDeSimbolos.busca(tokenAtual.getConteudo());
				if(s == null){		
					if(duvida){
						throw new AnaliseSintaticaException(String.format("Variável ou procedimento %s não foi declarado",tokenAtual.getConteudo()),tokenAtual);
					}else{
						throw new AnaliseSintaticaException(String.format("Variável %s não foi declarado",tokenAtual.getConteudo()),tokenAtual);
					}
				}else{
					if(!duvida){
						if(s.getCategoria() != Categoria.VARIAVEL && s.getCategoria() != Categoria.PARAMETRO){
							throw new AnaliseSintaticaException("Apenas variáveis são permitidas",s.getToken());
						}
					}
				}
			}else{
				if(!duvida){
					if(s.getCategoria() != Categoria.VARIAVEL && s.getCategoria() != Categoria.PARAMETRO){
						throw new AnaliseSintaticaException("Apenas variáveis são permitidas",s.getToken());
					}
				}
			}
			
		}else{
			for(Simbolo s : variaveisAtuais){
				Simbolo temp = null;
				//Verifica no escopo atual
				temp = tabelaAtual.busca(s.getConteudo());
				if( temp == null){
					//Verifica no escopo global
					temp = tabelaDeSimbolos.busca(s.getConteudo());
					if(temp == null){				
						throw new AnaliseSintaticaException(String.format("Váriavel %s não foi declarada",s.getConteudo()),s.getToken());
					}else{
						if(!duvida){
							if(temp.getCategoria() != Categoria.VARIAVEL && temp.getCategoria() != Categoria.PARAMETRO){
								throw new AnaliseSintaticaException("Apenas variáveis são permitidas",temp.getToken());
							}
						}
					}
				}else{
					if(!duvida){
						if(temp.getCategoria() != Categoria.VARIAVEL && temp.getCategoria() != Categoria.PARAMETRO){
							throw new AnaliseSintaticaException("Apenas variáveis são permitidas",temp.getToken());
						}
					}
				}
			}
			
		}
	}
	
	private void addIdentificadorDePrograma(){
		tabelaAtual.insere(tokenAtual.getConteudo(),tokenAtual,Categoria.NOMEPROGRAMA);		
	}

	private void addSimbolosComTipo(String tipo) throws AnaliseSintaticaException{
		for(Simbolo s : variaveisAtuais){
			
			//Aloca memória das variaveis
			if(s.getCategoria() == Categoria.VARIAVEL)
				gerador.alocaMemoria();
			
			s.setTipo(tipo);									
			
			Simbolo sim = tabelaAtual.busca(s.getConteudo());
			if(sim==null){
				sim = tabelaDeSimbolos.busca(s.getConteudo());
				if(sim == null){
					tabelaAtual.insere(s);
					if(s.getCategoria() == Categoria.PARAMETRO){
						Procedimento proc = (Procedimento) tabelaDeSimbolos.busca(tabelaAtual.getIdentificador());
						proc.getParametros().add(s);
					}
				}else{
					if(sim.getCategoria() == Categoria.PROCEDIMENTO){
						throw new AnaliseSintaticaException(String.format("%s já existe com o mesmo nome ",sim.getCategoria()),sim.getToken());
					}else{
						tabelaAtual.insere(s);
						if(s.getCategoria() == Categoria.PARAMETRO){
							Procedimento proc = (Procedimento) tabelaDeSimbolos.busca(tabelaAtual.getIdentificador());
							proc.getParametros().add(s);
						}
					}
				}
			}else{
				//Erro
				if(sim.getCategoria() == Categoria.PROCEDIMENTO){
					throw new AnaliseSintaticaException(String.format("%s já existe com o mesmo nome ",sim.getCategoria()),sim.getToken());
				}else{
					throw new AnaliseSintaticaException(String.format("%s já existe com o mesmo nome com o tipo %s",sim.getCategoria(),sim.getTipo()),sim.getToken());
				}
			}		
		}
		variaveisAtuais.clear();
	}
	
	private void iniciaPilhaDeExpressoes() {
		pilhaDeExpressoes = new PilhaDeExpressao();		
	}
	
	private void empilhaItemDeExpressao() {		
		if(tokenAtual.getTipo() == TipoToken.NUMERO_INTEIRO || tokenAtual.getTipo() == TipoToken.NUMERO_REAL || tokenAtual.getTipo() == TipoToken.SIMBOLO){
			pilhaDeExpressoes.push(new Simbolo(tokenAtual.getConteudo(),null,tokenAtual, tokenAtual.getTipo().getTipo(),0));
		}else{
			Simbolo s = null;
			s = tabelaAtual.busca(tokenAtual.getConteudo());
			if(s == null){
				s = tabelaDeSimbolos.busca(tokenAtual.getConteudo());
				if( s == null){
					s = new Simbolo(tokenAtual.getConteudo(),null,tokenAtual, null,0);
				}
			}			
			s.setToken(tokenAtual);
			pilhaDeExpressoes.push(s);
		}
		
	}	

	private void verificaParametros() throws AnaliseSintaticaException {
		
		Procedimento proc = (Procedimento) tabelaDeSimbolos.busca(pilhaDeExpressoes.peek().getConteudo());
		ArrayList<Simbolo> parametros = proc.getParametros();
				
		
		if(parametros.size() == variaveisAtuais.size()){
			
			boolean diferentes = false;
			for(int i = 0 ; i < parametros.size() ; i++){
				
				if(!parametros.get(i).getTipo().contentEquals(variaveisAtuais.get(i).getTipo())){
					
					diferentes = true;
					break;
					
				}
				
			}
			if(diferentes){
				throw new AnaliseSintaticaException("Parametros com tipos incompatíveis",pilhaDeExpressoes.peek().getToken());
			}			
			
		}else{
			throw new AnaliseSintaticaException("Número incorreto de parâmetros para a chamada de procedimento",pilhaDeExpressoes.peek().getToken());
		}
		
		
	}
	
	private void avaliaExpressao() throws AnaliseSintaticaException{
		if(!pilhaDeExpressoes.avaliarExpressaoComAtribuicao()){
			Simbolo sim = pilhaDeExpressoes.pop();
			throw new AnaliseSintaticaException(String.format("Atribuição na variável do tipo %s não pode receber um tipo diferente",sim.getTipo()),sim.getToken());
		}
	}
	
	private void avaliaCondicao() throws AnaliseSintaticaException {
		Simbolo sim = pilhaDeExpressoes.peek();		
		if(!pilhaDeExpressoes.avaliarExpressao()){
			
			throw new AnaliseSintaticaException(String.format("Expressão com tipos diferentes"),sim.getToken());
			
		}
		
	}
	
	public String getCodigoIntemediario(){
		return gerador.imprimeCodigo();
	}
}
