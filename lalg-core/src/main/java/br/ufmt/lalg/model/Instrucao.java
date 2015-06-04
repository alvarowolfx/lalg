package br.ufmt.lalg.model;

public enum Instrucao {
	
	//Instru��es com parametro	
	CRVL,CRCT,ALME,DSVI,DSVF,ARMZ,CHPR,DESM,PARAM,PUSHER,
	//Instru��es sem parametro	
	INPP,PARA,LEIT,IMPR,CPMI,CMAI,CDES,CPIG,CPMA,CPME,NEGA,DISJ,CONJ,DIVI,MULT,SUBT,SOMA,INVE,RTPR;
	
	
	public boolean hasParameter(){						
		
		if(this.ordinal() > 9){
			return false;
		}else{
			return true;
		}
	}
}
