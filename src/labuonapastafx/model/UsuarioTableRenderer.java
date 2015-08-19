package labuonapastafx.model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class UsuarioTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		String ativo = (String) table.getValueAt(row, table.getColumnCount() - 1);
		Color corAtivo = new Color(255,255,255);
		Color corInativo = new Color(200,200,200);
		
		if (ativo == "Sim") {
			setBackground(corAtivo);
		} else {
			setBackground(corInativo);
		}
			
		return this;
	}
	
}
