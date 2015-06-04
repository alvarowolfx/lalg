package br.ufmt.lalg.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class PilhaDeExpressao extends Stack<Simbolo> {

				
	public boolean avaliarExpressaoComAtribuicao(){
		
		Collections.reverse(this);	
		//Pega a vari�vel do lado esquerdo
		Simbolo var = this.pop();
		String tipo = var.getTipo();
		
		//Joga fora o simbolo de atribui��o
		this.pop();
		
		boolean eval = avaliarExpressaoComTipo(tipo);		
		this.push(var);
		
		return eval;
	}
	
	
	public boolean avaliarExpressao(){
		
		//Pega a vari�vel do lado esquerdo
		Simbolo var = this.peek();				
		
		//Retira parenteses ou -/+
		if(var.getToken().getTipo() == TipoToken.SIMBOLO){
			
			for (Simbolo s : this) {
				if(s.getToken().getTipo() != TipoToken.SIMBOLO){
					var = s;
					break;
				}
			}
		}
		
		String tipo = var.getTipo();
		
		Collections.reverse(this);					
				
		boolean eval = avaliarExpressaoComTipo(tipo);		
		this.push(var);
		
		return eval;
				
	}
	
	private boolean avaliarExpressaoComTipo(String tipo){
		
		boolean tiposDiferentes = false;
		while(!this.isEmpty()){
			
			Simbolo s = this.pop();
			
			if(s.getCategoria() == null && s.getToken().getTipo() != TipoToken.SIMBOLO){
				//� um numero
				if(!s.getTipo().contentEquals(tipo)){
					tiposDiferentes = true;
					break;
				}								
			}else if(s.getCategoria() == Categoria.VARIAVEL || s.getCategoria() == Categoria.PARAMETRO){
				//Verificar se tem tipos iguais ao da vari�vel			
				if(!s.getTipo().contentEquals(tipo)){
					tiposDiferentes = true;
					break;
				}				
			}			
			
		}
		if(tiposDiferentes){
			return false;
		}		
		return true;
		
	}

	//Retira atribui��o, poe parenteses e inverte a pilha
	//Usar depois
	public Simbolo retirarAtribuicao(){
		
		this.push(new Simbolo(")",null,new Token(")",TipoToken.SIMBOLO,0,0),"simbolo",null));
		Collections.reverse(this);
		Simbolo s = this.peek();
		while(!this.pop().getConteudo().contentEquals(":="));		
		this.push(new Simbolo("(",null,new Token("(",TipoToken.SIMBOLO,0,0),"simbolo",null));
		
		
		return s;
	}
	
	public Stack<Simbolo> infixaToPosfixa() {
		
	
		Stack<Simbolo> posfixa = new Stack<Simbolo>();
		Stack<Simbolo> aux = new Stack<Simbolo>();
		aux.push(new Simbolo("(",null,new Token("(",TipoToken.SIMBOLO,0,0),"simbolo",null));			
		
		System.out.println(Arrays.toString(this.toArray()));
		while(!this.isEmpty()){
			Simbolo s = this.pop();
			String conteudo = s.getConteudo();
			
			if(conteudo.contentEquals("(")){
				aux.push(s);
			}else if(conteudo.contentEquals(")")){
				
				while(true){

					s = aux.pop();	
					
					if(s.getConteudo().contentEquals("(")){
						//aux.push(s);
						break;
					}
					
					posfixa.push(s);
					
				}
																	
			}else if(conteudo.contentEquals("+") || conteudo.contentEquals("-")){
				Simbolo temp = null;
				temp = aux.peek();
				if(!temp.getConteudo().contentEquals("(")){
					posfixa.push(aux.pop());
				}
				aux.push(s);
				
			}else if(conteudo.contentEquals("*") || conteudo.contentEquals("/")){
				Simbolo temp = null;
				temp = aux.peek();
				if(temp.getConteudo().contentEquals("*") || temp.getConteudo().contentEquals("/")){
					posfixa.push(aux.pop());
				}
				aux.push(s);
				
			}else{
				
				posfixa.push(s);
				
			}
			
			
			System.out.println("Entrada");
			System.out.println(Arrays.toString(this.toArray()));
			
			System.out.println("Saida");
			System.out.println(Arrays.toString(posfixa.toArray()));
			
		}
		Collections.reverse(posfixa);
		return posfixa;
	}
	
		
}
