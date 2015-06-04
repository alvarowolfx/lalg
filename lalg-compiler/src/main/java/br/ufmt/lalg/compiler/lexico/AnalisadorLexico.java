package br.ufmt.lalg.compiler.lexico;

import br.ufmt.lalg.compiler.model.TabelaDeTokens;
import br.ufmt.lalg.model.TipoToken;
import br.ufmt.lalg.model.Token;

import java.io.*;

public class AnalisadorLexico {

	public TabelaDeTokens tabela;
	public String[] palavrasReservadas = {"procedure","var","integer","real","program","for","while","do","if","then","write","read","else","begin","end"};	
	
	//private String content;
	private BufferedReader br;
	private Character caracterAtual;
	private Character caracterAnterior;
	private int intVal;
	private String token = "";
	private int estado = 1;
	private int linha = 1;
	private int coluna = 1;
	
	public AnalisadorLexico(String conteudo){
		StringReader sr = new StringReader(conteudo);
		br = new BufferedReader(sr);
		tabela = new TabelaDeTokens();
	}
	
	public AnalisadorLexico(File f) throws IOException{
		FileReader fr = new FileReader(f);
		br = new BufferedReader(fr);
		tabela = new TabelaDeTokens();
	}
		
	public void fazerAnalise() throws AnaliseLexicaException{		
		caracterAtual = new Character(' ');
	
		leCaracter();
		linha = 1;
		coluna = 1;			
		
		while(intVal != -1){			
			executaEstado();
		}
		if(estado != 1){
			executaEstado();
		}
							
	}		
	
	private void executaEstado() throws AnaliseLexicaException{
		switch(estado){
			case 1:
				estado1();
				break;
			case 2 :
				estado2();
				break;
			case 4 :
				estado4();
				break;
			case 6 :
				estado6();
				break;
			case 7 :
				estado7();
				break;
			case 9 :
				estado9();
				break;
			case 11 :
				estado11();
				break;
			case 12 :
				estado12();
				break;	
			case 14 :
				estado14();
				break;
			case 17 :
				estado17();
				break;
			case 20 :
				estado20();
				break;
			case 22 :
				estado22();
				break;
		}
	}
	
	private void estado1() throws AnaliseLexicaException{
		if(caracterAtual == ' ' || caracterAtual == '\n' || caracterAtual == '\t'){
			a0();
			estado = 1;
			return;
		}
		if("()=+-*$;,.".indexOf(caracterAtual) >= 0){				
			a2();
			estado = 1;
			return;
		}
		if(Character.isLetter(caracterAtual)){
			a1();
			estado = 2;
			return;
		}
		if(Character.isDigit(caracterAtual)){
			a1();
			estado = 4;
			return;
		}
		if(caracterAtual == '/'){
			a1();
			estado = 9;
			return;
		}
		if(caracterAtual == '<'){
			a1();
			estado = 14;
			return;
		}
		if(caracterAtual == '>'){
			a1();
			estado = 17;
			return;
		}
		if(caracterAtual == '{'){
			a0();
			estado = 20;
			return;
		}
		if(caracterAtual == ':'){
			a1();
			estado = 22;
			return;
		}		
		throw new AnaliseLexicaException(String.format("Caractér %c não esperado na gramática",caracterAtual), linha, coluna);
	}		

	private void estado2(){
		if(Character.isLetterOrDigit(caracterAtual)){
			a3();
			estado = 2;
		}else{
			a4();		
			estado = 1;
		}		
	}
		
	private void estado4(){
		if(Character.isDigit(caracterAtual)){
			a3();
			estado = 4;			
		}else{
			if(caracterAtual == '.'){
				a3();
				estado = 6;
			}else{
				a5();
				estado = 1;
			}
		}		
	}

	private void estado6() throws AnaliseLexicaException {
		if(Character.isDigit(caracterAtual)){
			a3();
			estado = 7;			
		}else{
			//Erro;
			throw new AnaliseLexicaException("Número real espera pelo menos um digito depois do ponto", linha, coluna);
		}		
	}
	
	private void estado7() {
		if(Character.isDigit(caracterAtual)){
			a3();
			estado = 7;			
		}else{
			a6();
			estado = 1;
		}		
	}
	
	private void estado9() {
		if(caracterAtual == '*'){
			a0();
			estado = 11;			
		}else{
			a7();
			estado = 1;
		}		
	}
	
	private void estado11() {
		if(caracterAtual == '*'){
			a0();
			estado = 12;			
		}else{
			a0();
			estado = 11;			
		}		
	}
	
	private void estado12() {
		if(caracterAtual == '/'){
			a0();
			estado = 1;			
		}else{
			a0();
			estado = 11;			
		}		
	}
	
	private void estado14() {
		if(caracterAtual == '=' || caracterAtual == '>'){
			a2();
			estado = 1;			
		}else{
			a7();
			estado = 1;			
		}		
	}
	
	private void estado17() {
		if(caracterAtual == '='){
			a2();
			estado = 1;			
		}else{
			a7();
			estado = 1;			
		}		
	}
	
	private void estado20() {
		if(caracterAtual == '}'){
			a0();
			estado = 1;			
		}else{
			a0();
			estado = 20;			
		}		
	}
	
	private void estado22() {
		if(caracterAtual == '='){
			a2();
			estado = 1;			
		}else{
			a7();
			estado = 1;			
		}		
	}
	
	private void a0(){
		leCaracter();		
	}
	
	private void a1(){
		token = "" + caracterAtual;
		leCaracter();	
	}
	
	private void a2(){
		token += caracterAtual;
		leCaracter();
		tabela.addToken(new Token(token,TipoToken.SIMBOLO,linha,coluna));
		token = "";
	}
	
	private void a3(){
		token += caracterAtual;
		leCaracter();		
	}
	
	private void a4(){
		for(String pr : palavrasReservadas){
			if(pr.contentEquals(token)){
				tabela.addToken(new Token(token,TipoToken.PALAVRA_RESERVADA,linha,coluna));
				token = "";
				return;
			}
		}
		tabela.addToken(new Token(token,TipoToken.IDENTIFICADOR,linha,coluna));
		token = "";		
	}
	
	private void a5(){
		tabela.addToken(new Token(token,TipoToken.NUMERO_INTEIRO,linha,coluna));
		token = "";
	}
	
	private void a6(){
		tabela.addToken(new Token(token,TipoToken.NUMERO_REAL,linha,coluna));
		token = "";
	}
	
	private void a7(){
		tabela.addToken(new Token(token, TipoToken.SIMBOLO,linha,coluna));
		token = "";		
	}
	
	private void leCaracter(){
		try{
			caracterAnterior = caracterAtual;
			intVal = br.read();			
			caracterAtual = new Character((char)intVal);
			
			if(caracterAnterior == '\n'){
				linha++;
				coluna = 1;
			}else{
				coluna++;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
