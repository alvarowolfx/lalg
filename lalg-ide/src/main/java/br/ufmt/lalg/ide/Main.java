package br.ufmt.lalg.ide;

import br.ufmt.lalg.ide.view.PrincipalView;

import javax.swing.*;


public class Main {


	public static void main(String[] args) {		

		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {

				new PrincipalView();
				
			}
		});

	}

}
