package br.ufmt.lalg.model;

import java.util.ArrayList;

public class Procedimento extends Simbolo {

	private ArrayList<Simbolo> parametros = new ArrayList<Simbolo>();
	
	public Procedimento(Simbolo s) {
		super(s.getConteudo(),s.getCategoria(),s.getToken(),s.getTipo(),null);
		
	}

	public int getNumeroDeParametros(){
		return this.parametros.size();
	}
	
	public ArrayList<Simbolo> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<Simbolo> parametros) {
		this.parametros = parametros;
	}
	
	
	
	

}
