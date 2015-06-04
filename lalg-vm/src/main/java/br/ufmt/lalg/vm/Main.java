package br.ufmt.lalg.vm;

import br.ufmt.lalg.vm.view.PrincipalView;

import javax.swing.*;


public class Main {


	public static void main(String[] args) {		

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				new PrincipalView();
				
			}
		});

	}

}
