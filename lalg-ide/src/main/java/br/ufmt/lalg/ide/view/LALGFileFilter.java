package br.ufmt.lalg.ide.view;

import java.io.File;
import java.io.FileFilter;

public class LALGFileFilter implements FileFilter {

	
	public String getDescription() {						
		return "Apenas arquivos .lalg";
	}					
	@Override
	public boolean accept(File f) {						
		return f.getName().endsWith(".lalg");
	}

}
