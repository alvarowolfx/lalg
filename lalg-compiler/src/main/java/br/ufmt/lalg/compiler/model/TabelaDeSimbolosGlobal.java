package br.ufmt.lalg.compiler.model;

import br.ufmt.lalg.model.Categoria;
import br.ufmt.lalg.model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolosGlobal extends TabelaDeSimbolos {

	public TabelaDeSimbolosGlobal() {
		super("Global");		
	}

	Map<String,TabelaDeSimbolos> procedimentos = new HashMap<String,TabelaDeSimbolos>();
	
	public void novoProcedimento(Token t){
		
		insere(t.getConteudo(),t, Categoria.PROCEDIMENTO);
		procedimentos.put(t.getConteudo(),new TabelaDeSimbolos(t.getConteudo()));
	}
	
	public TabelaDeSimbolos buscaTabelaDeSimbolos(String ident){
		return procedimentos.get(ident);
	}
	
	
	public void insere(String proc,String ident,Token t,Categoria c, String tipo){
		buscaTabelaDeSimbolos(ident).insere(ident, t, c, tipo,null);		
	}
	
	public void insere(String proc,String ident,Token t,Categoria c){
		buscaTabelaDeSimbolos(ident).insere(ident, t, c, null,null);		
	}
	
	public ArrayList<TabelaDeSimbolos> getTabelasDeSimbolos(){
		return new ArrayList<TabelaDeSimbolos>(procedimentos.values());
	}
}
