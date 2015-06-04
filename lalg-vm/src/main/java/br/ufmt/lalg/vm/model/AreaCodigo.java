package br.ufmt.lalg.vm.model;

import br.ufmt.lalg.model.Instrucao;
import br.ufmt.lalg.model.InstrucaoMaquina;
import java.util.ArrayList;

public class AreaCodigo extends ArrayList<InstrucaoMaquina>{

	private static final long serialVersionUID = 1L;
	
	public AreaCodigo(ArrayList<InstrucaoMaquina> instrucoes) {
		this.addAll(instrucoes);	
	}
	
	public AreaCodigo(String instrucoes) {
			
		loadInstrucoes(instrucoes);	
	}
	
	private void loadInstrucoes(String instrucoes){
		int label = 0;
		String[] linhas = instrucoes.split("\n");
		for(String linha : linhas){
		
				
			String[] params = linha.split(" ");
			InstrucaoMaquina inst = null;
			if(params.length > 2){
				//Tem parametro	
				
				String param = params[2];
				Number ds = null;
				if(param.contains(".")){
					ds = Float.parseFloat(param);
					inst = new InstrucaoMaquina(label, Instrucao.valueOf(params[1]),ds.floatValue());
				}else{
					
					if(param.contains(",")){
						ds = Float.parseFloat(param.replace(",","."));
						inst = new InstrucaoMaquina(label,Instrucao.valueOf(params[1]),ds.floatValue());
					}else{				
						ds = Integer.parseInt(param);				
						inst = new InstrucaoMaquina(label,Instrucao.valueOf(params[1]),ds.intValue());
					}
				}																
			}else{
				//Não tem
				inst = new InstrucaoMaquina(label,Instrucao.valueOf(params[1]));
			}
			
			this.add(inst);
			
		}	
	}
}
