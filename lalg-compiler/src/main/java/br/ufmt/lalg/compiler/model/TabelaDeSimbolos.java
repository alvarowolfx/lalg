package br.ufmt.lalg.compiler.model;

import br.ufmt.lalg.model.Categoria;
import br.ufmt.lalg.model.Procedimento;
import br.ufmt.lalg.model.Simbolo;
import br.ufmt.lalg.model.Token;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

	public Map<String,Simbolo> simbolos = new HashMap<String,Simbolo>();
	private String identificador;
	private int enderecoAtual = 0;
	
	public TabelaDeSimbolos(String identificador){
		this.setIdentificador(identificador);
	}
	
	public void insere(Simbolo s){
		if(s.getCategoria() != Categoria.PROCEDIMENTO){
			simbolos.put(s.getConteudo(),s);
		}else{
			simbolos.put(s.getConteudo(),new Procedimento(s));
		}
	}
	
	public void insere(String ident,Token t,Categoria c,String tipo,Integer endereco){
		insere(new Simbolo(ident,c,t,tipo,endereco));
	}
	
	public void insere(String ident,Token t,Categoria c){
		insere(new Simbolo(ident,c,t,null,null));
	}
	
	public Simbolo busca(String ident){		
		return simbolos.get(ident);
	}
	
	public Collection<Simbolo> getSimbolos(){
		return simbolos.values();
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public int getEnderecoAtual() {
		return enderecoAtual;
	}
	
	public void setEnderecoAtual(int enderecoAtual) {
		this.enderecoAtual = enderecoAtual;
	}
	
	public void addEnderecoAtual() {
		this.enderecoAtual++;
	}
	
}
