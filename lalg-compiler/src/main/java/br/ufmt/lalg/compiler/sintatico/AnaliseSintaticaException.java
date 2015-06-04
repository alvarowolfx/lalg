package br.ufmt.lalg.compiler.sintatico;

import br.ufmt.lalg.model.Token;

public class AnaliseSintaticaException extends Exception {

	private static final long serialVersionUID = 1L;

	public AnaliseSintaticaException(String msg,Token token){
		
		super(String.format("Erro sintático no token %s , %s , na linha %d e coluna %d",
							token.getConteudo(),
							msg,
							token.getLinha(),
							token.getColuna()));
	}
}
