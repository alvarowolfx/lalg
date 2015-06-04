package br.ufmt.lalg.compiler.lexico;

import br.ufmt.lalg.compiler.model.TabelaDeTokens;
import br.ufmt.lalg.model.TipoToken;
import br.ufmt.lalg.model.Token;

public class AnaliseLexicaUtil {
	
	public static String getCodigoFonteEmHTML(TabelaDeTokens tabela){
		StringBuffer buffer = new StringBuffer();
		int linha = 1, coluna = 1;		
		if(!tabela.getTokens().isEmpty()){			
			Token token = tabela.getTokens().get(0);
			linha = token.getLinha();
			coluna = token.getColuna();
		}
		
		for(Token t :tabela.getTokens()){		
			boolean pulouLinha = false;
			if(linha < t.getLinha()){
				for(int i = 0;i < t.getLinha()-linha ; i++){
					buffer.append("<br/>");					
				}
				linha = t.getLinha();
				pulouLinha = true;
			}
			
			if(coluna < t.getColuna()){
				int tmp = t.getColuna()-coluna-1;
				for(int i = 0;i < tmp ; i++){
					buffer.append("&nbsp;");
					coluna++;
				}
				buffer.append("<b><font size='14px' color=\""+getColor(t.getTipo())+"\">");
				buffer.append(t.getConteudo().replaceAll("<","&#60;").replaceAll(">","&#62;"));
				buffer.append("</font></b>");
				coluna += t.getConteudo().length();
				
			}else{	
				if(pulouLinha){
					if(t.getColuna() > 1){
						for(int i = 0;i < t.getColuna()-1; i++){
							buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;");
							coluna++;
						}
					}
				}
				coluna = t.getConteudo().length();				
				buffer.append("<b><font size='14px' color=\""+getColor(t.getTipo())+"\">");
				buffer.append(t.getConteudo().replaceAll("<","&#60;").replaceAll(">","&#62;"));
				buffer.append("</font></b>");
			}
			
		}
		return buffer.toString();
	}
	
	public static String getCodigoFonte(TabelaDeTokens tabela){
		StringBuffer buffer = new StringBuffer();
		int linha = 1, coluna = 1;		
		if(!tabela.getTokens().isEmpty()){			
			Token token = tabela.getTokens().get(0);
			linha = token.getLinha();
			coluna = token.getColuna();
		}
		
		for(Token t :tabela.getTokens()){		
			boolean pulouLinha = false;
			if(linha < t.getLinha()){
				buffer.append("\n");
				linha = t.getLinha();
				pulouLinha = true;
			}
			
			if(coluna < t.getColuna()){
				for(int i = 0;i < t.getColuna()-coluna-1 ; i++){
					buffer.append(" ");
					coluna++;
				}
				
				buffer.append(t.getConteudo());				
				coluna += t.getConteudo().length();
				
			}else{	
				if(pulouLinha){
					if(t.getColuna() > 1){
						for(int i = 0;i < t.getColuna()-1; i++){
							buffer.append("\t");
							coluna++;
						}
					}
				}
				coluna = t.getConteudo().length();								
				buffer.append(t.getConteudo());
				
			}
			
		}
		return buffer.toString();
	}
	
	private static String getColor(TipoToken tt){
		switch(tt){
			case IDENTIFICADOR : return "green";
			case NUMERO_INTEIRO : return "blue";
			case NUMERO_REAL : return "blue";
			case PALAVRA_RESERVADA : return "red";
			case SIMBOLO : return "black";
		}
		return "";
	}

}
