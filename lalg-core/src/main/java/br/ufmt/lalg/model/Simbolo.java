package br.ufmt.lalg.model;

public class Simbolo {
	
	private String conteudo;
	private Categoria categoria;
	private Token token;
	private String tipo;
	private Integer enderecoRelativo;
	private Integer primeiraInstrucao;
	
	public Simbolo(String conteudo, Categoria categoria, Token token,
			String tipo,Integer endereco) {
		super();
		this.conteudo = conteudo;
		this.categoria = categoria;
		this.token = token;
		this.tipo = tipo;
		this.enderecoRelativo = endereco;
	}	
	
	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}		

	public Integer getEnderecoRelativo() {
		return enderecoRelativo;
	}

	public void setEnderecoRelativo(Integer enderecoRelativo) {
		this.enderecoRelativo = enderecoRelativo;
	}

	public Integer getPrimeiraInstrucao() {
		return primeiraInstrucao;
	}

	public void setPrimeiraInstrucao(Integer primeiraInstrucao) {
		this.primeiraInstrucao = primeiraInstrucao;
	}

	@Override
	public String toString(){
		return getConteudo();
		//return String.format("%s - %s - %s - %s",conteudo,token.getTipo(),categoria,tipo);
	}
}
