package br.ufmt.lalg.ide.view.tablemodel;

import br.ufmt.lalg.compiler.model.TabelaDeTokens;
import br.ufmt.lalg.model.TipoToken;
import br.ufmt.lalg.model.Token;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class TabelaDeTokensTableModel implements TableModel{

	private TabelaDeTokens tabelaDeSimbolos;
	public TabelaDeTokensTableModel(TabelaDeTokens tbl){
		this.tabelaDeSimbolos = tbl;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
			case 0: return String.class;
			case 1: return TipoToken.class;
			case 2: return Integer.class;
			case 3: return Integer.class;
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
			case 0: return "Conteudo";
			case 1: return "Tipo";
			case 2: return "Linha";
			case 3: return "Coluna";
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return tabelaDeSimbolos.getTokens().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		List<Token> tokens = tabelaDeSimbolos.getTokens();
		switch(columnIndex){
			case 0: return tokens.get(rowIndex).getConteudo();
			case 1: return tokens.get(rowIndex).getTipo();
			case 2: return tokens.get(rowIndex).getLinha();
			case 3: return tokens.get(rowIndex).getColuna();
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
		List<Token> tokens = tabelaDeSimbolos.getTokens();
		switch(columnIndex){
			case 0: tokens.get(rowIndex).setConteudo((String) aValue);
			case 1: tokens.get(rowIndex).setTipo((TipoToken) aValue);
			case 2: tokens.get(rowIndex).setLinha((Integer) aValue);
			case 3: tokens.get(rowIndex).setColuna((Integer) aValue);
		}
	}

}
