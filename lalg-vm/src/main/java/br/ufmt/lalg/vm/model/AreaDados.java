package br.ufmt.lalg.vm.model;

import java.util.Stack;

public class AreaDados extends Stack<Number>{
	
	private static final long serialVersionUID = 1L;
	
	public void aloca(){
		this.add(new Integer(0));				
	}
		
}
