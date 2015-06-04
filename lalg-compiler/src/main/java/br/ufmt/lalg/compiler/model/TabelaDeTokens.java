package br.ufmt.lalg.compiler.model;

import br.ufmt.lalg.model.Token;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeTokens {

	private List<Token> tokens = new ArrayList<Token>();
	
	public void addToken(Token token){
		this.tokens.add(token);
	}
	
	public List<Token> getTokens() {
		return tokens;
	}	
	
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	
	
	
}
