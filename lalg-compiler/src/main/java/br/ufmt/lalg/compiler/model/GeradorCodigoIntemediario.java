package br.ufmt.lalg.compiler.model;

import br.ufmt.lalg.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeradorCodigoIntemediario {
	
	private StringBuffer buffer;
	private ArrayList<InstrucaoMaquina> instrucoes;
	private int labelAtual;
	
	
	public GeradorCodigoIntemediario() {
		buffer = new StringBuffer();
		instrucoes = new ArrayList<InstrucaoMaquina>();
		labelAtual = 0;
	}

	public void geraCodigoInit(){
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.INPP);
		instrucoes.add(i);
			
	}

	
	public void alocaMemoria(){
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++,Instrucao.ALME,1);
		instrucoes.add(i);	
		
	}
	
	public void leituraVariavel(List<Simbolo> variaveisAtuais){
		
		for(Simbolo s : variaveisAtuais){
			
			InstrucaoMaquina i1 = new InstrucaoMaquina(labelAtual++,Instrucao.LEIT);
			InstrucaoMaquina i2 = new InstrucaoMaquina(labelAtual++,Instrucao.ARMZ,s.getEnderecoRelativo());
		
			instrucoes.add(i1);	
		
			instrucoes.add(i2);	
			
		}
		
	}
	
	public void escritaVariaveis(List<Simbolo> variaveisAtuais){		
		
		for(Simbolo s : variaveisAtuais){
			
			InstrucaoMaquina i1 = new InstrucaoMaquina(labelAtual++,Instrucao.CRVL,s.getEnderecoRelativo());
			InstrucaoMaquina i2 = new InstrucaoMaquina(labelAtual++,Instrucao.IMPR);
		
			instrucoes.add(i1);	
		
			instrucoes.add(i2);
			
		}
				
	}
	
	public void atribuicaoVar(PilhaDeExpressao atribuicao){
		PilhaDeExpressao copia = (PilhaDeExpressao) atribuicao.clone();		
		
		Simbolo lval = copia.retirarAtribuicao();
						
		geraCodigoExpressaoPosfixa(copia);
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++,Instrucao.ARMZ,lval.getEnderecoRelativo());		
		instrucoes.add(i);						
				
	}
	
	public void expressaoCondicional(PilhaDeExpressao lval,PilhaDeExpressao rval,Token condicao){
		
		geraCodigoExpressaoPosfixa((PilhaDeExpressao) lval.clone());
		geraCodigoExpressaoPosfixa((PilhaDeExpressao) rval.clone());
		
		InstrucaoMaquina i = null;		
		if(condicao.getConteudo().contentEquals("<")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CPME);
		}else if(condicao.getConteudo().contentEquals("<=")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CPMI);
		}else if(condicao.getConteudo().contentEquals(">")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CPMA);
		}else if(condicao.getConteudo().contentEquals(">=")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CMAI);
		}else if(condicao.getConteudo().contentEquals("=")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CPIG);
		}else if(condicao.getConteudo().contentEquals("<>")){
			i = new InstrucaoMaquina(labelAtual++,Instrucao.CDES);
		}
		
		instrucoes.add(i);	
		
		i = new InstrucaoMaquina(labelAtual++, Instrucao.DSVF);
		
		instrucoes.add(i);	
	}
	
	public void finalizaCondicional(int labelInicial,int labelCondicional) {
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.DSVI,labelInicial);
		
		instrucoes.add(i);		
		
		instrucoes.get(labelCondicional).setEndereco(labelAtual);
		
	}
	
	public void finalizaCondicionalComElse(int labelInicialElse, int labelCondicional, int labelIncondicional){
		
		instrucoes.get(labelCondicional).setEndereco(labelInicialElse);
		instrucoes.get(labelIncondicional).setEndereco(labelAtual);
		
	}
	
	public void finalizaCondicionalSemElse(int labelCondicional){
		
		instrucoes.get(labelCondicional).setEndereco(labelAtual);
		
	}
	
	public void adicionaDesvioIncondicional(){
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.DSVI);
		instrucoes.add(i);				
		
	}
	
	public void fimPrograma(){
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.PARA);		
		instrucoes.add(i);								
	}
	
	public void removeInstrucao(int label){
		instrucoes.remove(label);
		labelAtual--;
	}
	
	public void chamadaProcedimento(List<Simbolo> parametros,int labelProcedimento){
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.PUSHER);		
		instrucoes.add(i);
		
		if(parametros.size() > 0){
						
			for (Simbolo s : parametros) {
				
				InstrucaoMaquina i2 = new InstrucaoMaquina(labelAtual++, Instrucao.PARAM,s.getEnderecoRelativo());		
				instrucoes.add(i2);
				
			}			
			
		}
		
		InstrucaoMaquina i2 = new InstrucaoMaquina(labelAtual++, Instrucao.CHPR,labelProcedimento);		
		instrucoes.add(i2);
		
		i.setEndereco(labelAtual);
	}
	
	public void finalizaProcedimento(int labelIncondicional, int tam) {
		
		InstrucaoMaquina i = new InstrucaoMaquina(labelAtual++, Instrucao.DESM,tam);		
		InstrucaoMaquina i2 = new InstrucaoMaquina(labelAtual++, Instrucao.RTPR);
		
		instrucoes.add(i);						
		instrucoes.add(i2);
		
		instrucoes.get(labelIncondicional).setEndereco(labelAtual);
		
	}
	
	private void geraCodigoExpressaoPosfixa(PilhaDeExpressao infixa){
		Stack<Simbolo> posfix = infixa.infixaToPosfixa();
		while(!posfix.isEmpty()) {
			Simbolo s = posfix.pop();
			InstrucaoMaquina i = null;
			if(s.getToken().getTipo() == TipoToken.IDENTIFICADOR){				
				
				i = new InstrucaoMaquina(labelAtual++,Instrucao.CRVL,s.getEnderecoRelativo());											
			
			}else if(s.getToken().getTipo() == TipoToken.SIMBOLO){
				
				switch (s.getConteudo().charAt(0)) {
					case '+':
						i = new InstrucaoMaquina(labelAtual++,Instrucao.SOMA,s.getEnderecoRelativo());
						break;
					case '*':
						i = new InstrucaoMaquina(labelAtual++,Instrucao.MULT,s.getEnderecoRelativo());
						break;
	
					case '/':
						i = new InstrucaoMaquina(labelAtual++,Instrucao.DIVI,s.getEnderecoRelativo());
						break;
	
					case '-':
						i = new InstrucaoMaquina(labelAtual++,Instrucao.SUBT,s.getEnderecoRelativo());
						break;
				}
			}else{
				if(s.getTipo().contentEquals("integer")){
					i = new InstrucaoMaquina(labelAtual++,Instrucao.CRCT,Integer.valueOf(s.getConteudo()));
				}else{
					i = new InstrucaoMaquina(labelAtual++,Instrucao.CRCT,Float.valueOf(s.getConteudo()));
				}
			}
			
			
			instrucoes.add(i);	
		}
	}
	
	public String imprimeCodigo(){
		buffer = new StringBuffer();
		for (InstrucaoMaquina inst : instrucoes) {
			buffer.append(inst);
			quebraLinha();
		}
		
		return buffer.toString();
	}
	
	private void quebraLinha() {
		buffer.append("\n");		
	}
	
	public int getLabelAtual() {
		return labelAtual;
	}


}
