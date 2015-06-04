package br.ufmt.lalg.model;

public enum TipoToken {

	SIMBOLO,IDENTIFICADOR,PALAVRA_RESERVADA,NUMERO_INTEIRO,NUMERO_REAL;
		
	public String getTipo(){
		switch (this) {
			case NUMERO_INTEIRO:
				return "integer";
			case NUMERO_REAL:
				return "real";
			case SIMBOLO:
				return "simbolo";
		}		
		return super.toString();		
	}
}
