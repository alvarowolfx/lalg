package br.ufmt.lalg.compiler.lexico;

public class AnaliseLexicaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AnaliseLexicaException(){
		super();
	}
	
	public AnaliseLexicaException(String msg,int linha,int coluna){		
		super(String.format("Erro de análise léxica : %s , na linha %d e coluna %d",msg,linha,coluna));
	}
}
