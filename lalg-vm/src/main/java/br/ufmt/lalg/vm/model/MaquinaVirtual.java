package br.ufmt.lalg.vm.model;

import br.ufmt.lalg.model.InstrucaoMaquina;
import br.ufmt.lalg.vm.model.AreaCodigo;
import br.ufmt.lalg.vm.model.AreaDados;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MaquinaVirtual {
	
	
	private int i;//Registrador de programa que aponta para a próxima instrução
	private AreaCodigo C;
	private AreaDados D;
	private StringBuilder out;
		
	private InstrucaoMaquina instrucaoAtual = null;
	
	public MaquinaVirtual(ArrayList<InstrucaoMaquina> instrucoes) {

		i = 0;
		C = new AreaCodigo(instrucoes);
		D = new AreaDados();
		setOut(new StringBuilder());
	}
	
	public MaquinaVirtual(String instrucoes) {

		i = 0;
		C = new AreaCodigo(instrucoes);
		D = new AreaDados();
		setOut(new StringBuilder());
		
	}
	
	public void execute(){
		
		while(C.size() > i){
			
			instrucaoAtual = C.get(i);
			System.out.println(" D -> "+Arrays.toString(D.toArray()));
			System.out.println(" Label -> "+i);
			switch (instrucaoAtual.getInstrucao()) {
				case ALME:
					alme();
					break;
				case ARMZ:
					armz();
					break;
				case CDES:
					cdes();
					break;
				case CHPR:
					chpr();
					break;
				case CMAI:
					cmai();
					break;
				case CONJ:
					conj();
					break;
				case CPIG:
					cpig();
					break;
				case CPMA:
					cpma();
					break;
				case CPME:
					cpme();
					break;
				case CPMI:
					cpmi();
					break;
				case CRCT:
					crct();
					break;
				case CRVL:
					crvl();
					break;
				case DESM:
					desm();
					break;
				case DISJ:
					disj();
					break;
				case DIVI:
					divi();
					break;
				case DSVF:
					dsvf();
					break;
				case DSVI:
					dsvi();
					break;
				case IMPR:
					impr();
					break;
				case INPP:
					inpp();
					break;
				case INVE:
					inve();
					break;
				case LEIT:
					leit();
					break;
				case MULT:
					mult();
					break;
				case NEGA:
					nega();
					break;
				case PARA:
					para();
					break;
				case PARAM:
					param();
					break;
				case PUSHER:
					pusher();
					break;
				case RTPR:
					rtpr();
					break;
				case SOMA:
					soma();
					break;
				case SUBT:
					subt();
					break;
				default:
					break;
	
				}
			i++;
		}
			
	}
	
	private void subt() {
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds instanceof Integer && ds1 instanceof Integer){
			D.push(new Integer(ds1.intValue()-ds.intValue()));
		}else{
			D.push(new Float(ds1.floatValue()-ds.floatValue()));
		}
		
	}

	private void soma() {
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds instanceof Integer && ds1 instanceof Integer){
			D.push(new Integer(ds1.intValue()+ds.intValue()));
		}else{
			D.push(new Float(ds1.floatValue()+ds.floatValue()));
		}
		
	}

	private void rtpr() {
		
		int endereco = D.pop().intValue();
		i = endereco - 1;
		
	}

	private void pusher() {
		
		int endereco = instrucaoAtual.getEndereco().intValue();
		D.push(endereco);
					
	}

	private void param() {

		int endereco = instrucaoAtual.getEndereco().intValue();		
		D.push(D.get(endereco));		
				
	}

	private void para() {
		// TODO Auto-generated method stub
		
	}

	private void nega() {
		// TODO Auto-generated method stub
		
	}

	private void mult() {
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds instanceof Integer && ds1 instanceof Integer){
			D.push(new Integer(ds1.intValue()*ds.intValue()));
		}else{
			D.push(new Float(ds1.floatValue()*ds.floatValue()));
		}
	}

	private void leit() {
		
		String in = JOptionPane.showInputDialog("Digite a entrada ");
		Number ds = null;
		if(in.contains(".")){
			ds = Float.parseFloat(in);
		}else{
			
			if(in.contains(",")){
				ds = Float.parseFloat(in.replace(",","."));
			}else{				
				ds = Integer.parseInt(in);				
			}
		}
		D.push(ds);
		in = null;		
		
	}

	private void inve() {
		Number ds = D.pop();
		
		if(ds instanceof Integer){
			D.push(new Integer(-ds.intValue()));
		}else{
			D.push(new Float(-ds.floatValue()));
		}	
		
	}

	private void inpp() {
		// TODO Auto-generated method stub
		
	}

	private void impr() {
		
		Number ds = D.pop();
		if(ds instanceof Integer){
			getOut().append(String.format("%d\n",ds.intValue()));
		}else{
			getOut().append(String.format("%s\n",ds));
		}
		
	}

	private void dsvi() {
		
		int endereco = instrucaoAtual.getEndereco().intValue();
		
		i = endereco - 1 ;
	}

	private void dsvf() {
		
		Number ds = D.pop();
		int endereco = instrucaoAtual.getEndereco().intValue();
		
		if(ds.intValue() == 0)
			i = endereco - 1 ;
						
	}

	private void divi() {
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds instanceof Integer && ds1 instanceof Integer){
			D.push(new Integer(ds1.intValue()/ds.intValue()));
		}else{
			D.push(new Float(ds1.floatValue()/ds.floatValue()));
		}
				
	}

	private void disj() {
		// TODO Auto-generated method stub
		
	}

	private void desm() {

		int m = instrucaoAtual.getEndereco().intValue()-1;
		for(int i = 0 ; i < m ; i++){
			D.pop();
		}
		
	}

	private void crvl() {
		D.push(D.get(instrucaoAtual.getEndereco().intValue()));
		
	}

	private void crct() {
		D.push(instrucaoAtual.getEndereco());
				
	}

	private void cpmi() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() <= ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
		
	}

	private void cpme() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() < ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
			
	}

	private void cpma() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() > ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
	}

	private void cpig() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() == ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
	}

	private void conj() {
		// TODO Auto-generated method stub
		
	}

	private void cmai() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() >= ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
	}

	private void chpr() {

		int endereco = instrucaoAtual.getEndereco().intValue();
		i = endereco - 1;
								
	}

	private void cdes() {
		
		Number ds = D.pop();
		Number ds1 = D.pop();
		
		if(ds1.floatValue() != ds.floatValue()){
			D.push(1);
		}else{
			D.push(0);
		}
		
	}

	private void armz() {
		
		Number ds = D.pop();
		
		int endereco = instrucaoAtual.getEndereco().intValue();
		
		D.set(endereco, ds);
		
	}

	private void alme() {		
		D.aloca();
	}

	public StringBuilder getOut() {
		return out;
	}

	public void setOut(StringBuilder out) {
		this.out = out;
	}

}
