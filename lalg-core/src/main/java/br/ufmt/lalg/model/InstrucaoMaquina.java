package br.ufmt.lalg.model;

public class InstrucaoMaquina {

	private int label;
	private Instrucao instrucao;
	private Number endereco;
	
	public InstrucaoMaquina(int label,Instrucao instrucao) {
		this.label = label;
		this.instrucao = instrucao;
		this.endereco = 0;
	}
		
	public InstrucaoMaquina(int label, Instrucao instrucao, int endereco) {
		this.label = label;
		this.instrucao = instrucao;
		this.endereco = endereco;
	}
	
	public InstrucaoMaquina(int label, Instrucao instrucao, float constante) {
		this.label = label;
		this.instrucao = instrucao;
		this.endereco = constante;
	}

	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public Instrucao getInstrucao() {
		return instrucao;
	}
	public void setInstrucao(Instrucao instrucao) {
		this.instrucao = instrucao;
	}
	public Number getEndereco() {
		return endereco;
	}
	public void setEndereco(int endereco) {
		this.endereco = endereco;
	}
	
	@Override
	public String toString(){
		if(instrucao.hasParameter()){
			if(endereco instanceof Integer){
				return String.format("%d %s %d",this.label,this.instrucao,this.endereco.intValue());
			}else{
				return String.format("%d %s %s",this.label,this.instrucao,this.endereco);
			}
		}else{
			return String.format("%d %s",this.label,this.instrucao);
		}
		
	}
	
}
