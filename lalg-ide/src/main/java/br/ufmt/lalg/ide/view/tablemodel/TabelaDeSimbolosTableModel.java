package br.ufmt.lalg.ide.view.tablemodel;

import br.ufmt.lalg.compiler.model.TabelaDeSimbolos;
import br.ufmt.lalg.model.Categoria;
import br.ufmt.lalg.model.Simbolo;
import br.ufmt.lalg.model.TipoToken;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class TabelaDeSimbolosTableModel implements TableModel {

	private TabelaDeSimbolos tabelaDeSimbolos;
	
	public TabelaDeSimbolosTableModel(TabelaDeSimbolos tbl){
		this.tabelaDeSimbolos = tbl;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
			case 0: return String.class;
			case 1: return TipoToken.class;
			case 2: return Categoria.class;
			case 3: return String.class;
		}
		return null;
	}

	@Override
	public int getColumnCount() {		
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
			case 0: return "Cadeia";
			case 1: return "Token";
			case 2: return "Categoria";
			case 3: return "Tipo";
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return tabelaDeSimbolos.getSimbolos().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ArrayList<Simbolo> simbolos =  new ArrayList<Simbolo>(tabelaDeSimbolos.simbolos.values());
		switch(columnIndex){
			case 0: return simbolos.get(rowIndex).getConteudo();
			case 1: return simbolos.get(rowIndex).getToken().getTipo();
			case 2: return simbolos.get(rowIndex).getCategoria();
			case 3: return simbolos.get(rowIndex).getTipo() != null ? simbolos.get(rowIndex).getTipo() : "-" ;
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {		
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {	
		/*
		List<Token> tokens = tabelaDeSimbolos.getTokens();
		switch(columnIndex){
			case 0: tokens.get(rowIndex).setConteudo((String) aValue);
			case 1: tokens.get(rowIndex).setTipo((TipoToken) aValue);
			case 2: tokens.get(rowIndex).setLinha((Integer) aValue);
			case 3: tokens.get(rowIndex).setColuna((Integer) aValue);
		}
		*/
	}

}
