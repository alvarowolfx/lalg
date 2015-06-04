package br.ufmt.lalg.model;

public class Token {

	private String conteudo;
	private TipoToken tipo;
	private int linha;
	private int coluna;
	
	public Token(String conteudo, TipoToken tipo,int linha,int coluna) {
		super();
		this.conteudo = conteudo;
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna - conteudo.length();
	}
	
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public TipoToken getTipo() {
		return tipo;
	}
	public void setTipo(TipoToken tipo) {
		this.tipo = tipo;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	@Override
	public String toString(){
		return this.conteudo+" - "+this.tipo+ " - posi��o("+linha+","+coluna+")";
	}
	
	
}
