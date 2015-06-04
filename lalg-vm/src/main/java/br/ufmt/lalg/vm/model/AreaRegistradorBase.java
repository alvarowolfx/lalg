package br.ufmt.lalg.vm.model;

import java.util.Stack;

public class AreaRegistradorBase extends Stack<Integer>{
	
	private static final long serialVersionUID = 1L;
	
	private int scope = 0;

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

}
