package labuonapastafx.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class UsuarioTableModel extends AbstractTableModel {

	List<Usuario> linhas;
	
	// Array para os nomes das colunas
	private String[] colunas = new String[] {"Login", "Nome", "Permissao", "Ativo"};
	
	// Constantes para representar os indices das colunas
	private static final int LOGIN = 0;
	private static final int NOME = 1;
	private static final int PERMISSAO = 2;
	private static final int ATIVO = 3;
	
	// criar uma lista vazia
	public UsuarioTableModel() {
		linhas = new ArrayList<Usuario>();
	}
	
	// criar uma lista com linhas passadas via parametros
	public UsuarioTableModel(ArrayList<Usuario> linhas) {
		this.linhas = linhas;
	}
	
	@Override
	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public int getRowCount() {
		return linhas.size();
	}
	
        @Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}
	
        @Override
	public Class<?> getColumnClass(int indexColumn) {
		switch (indexColumn) {
		case LOGIN:
			return String.class;
		case NOME:
			return String.class;
		case PERMISSAO:
			return String.class;
		case ATIVO:
			return String.class;
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
        @Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Obter o usuario referente a linha especificada
		Usuario usuarioSel = linhas.get(rowIndex);
		
		switch (columnIndex) {
		case LOGIN:
			return usuarioSel.getLogin();
		case NOME:
			return usuarioSel.getNomeCompleto();
		case PERMISSAO:
			return usuarioSel.getTipoAcesso();
		case ATIVO:
			if (usuarioSel.getAtivo() == 1) {
				return "Sim";
			} else {
				return "N�o";
			}
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Obter o usuario referente a linha especificada
		Usuario usuarioSel = linhas.get(rowIndex);
		
		switch (columnIndex) {
		case LOGIN:
			usuarioSel.setLogin((String)aValue);
			break;
		case NOME:
			usuarioSel.setNomeCompleto((String)aValue);
			break;
		case PERMISSAO:
			usuarioSel.setTipoAcesso((AcessoEnum)aValue);
			break;
		case ATIVO:
			usuarioSel.setAtivo((byte)aValue);
			break;
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualizacao da celula
	}
	
	// Retorna o usuario da linha especificada no parametro
	public Usuario getUsuario(int rowIndex) {
		return linhas.get(rowIndex);
	}
	
	// Adiciona um Usuario
	public void addUsuario(Usuario usuario) {
		linhas.add(usuario);
		
		// Pega a quantidade de linhas e subtrai 1 para
		// achar o ultimo indice. A subtracao � necess�ria
		// porque o indice comeca em zero.
		int ultimoIndice = getRowCount() - 1;
		
		// Notificar a inclusao da linha
		fireTableRowsInserted(ultimoIndice, ultimoIndice);
	}
	
	// Remove o usuario da linha especificada
	public void removeUsuario(int rowIndex) {
		linhas.remove(rowIndex);
		
		// Notifica a exclusao da linha
		fireTableRowsDeleted(rowIndex, rowIndex);
		
	}
	
	// Remove o Usuario informado
	public void removeUsuario(Usuario usuario) {
		
		int indx = getIndexUsuario(usuario);
		
		// Se encontrou o indice entao remove da tabela
		if (indx > -1) {
			removeUsuario(indx);
		}
		
	}
	
	public void updateUsuario(Usuario usuario) {
		
		int indx = getIndexUsuario(usuario);
		
		linhas.set(indx, usuario);
		
		fireTableRowsUpdated(indx, indx);
	}
	
	public int getIndexUsuario(Usuario usuario) {
		// Iniciar a variavel do indice como -1, ou seja,
		// objeto nao encontrado
		int indx = -1;

		for (Usuario linha : linhas) {
			if (linha.getLogin().equals(usuario.getLogin())) {
				indx = linhas.indexOf(linha);
				break;
			}
		}	
		
		return indx;
	}
	
	// Adiciona uma lista de Usuario ao final da lista
	public void addListaDeUsuario(List<Usuario> usuarios) {
		// Pega o tamanho antigo da tabela, que servira
		// como indice para o primeiros dos novos registros
		int indice = getRowCount();
		
		// Adiciona os novos registros
		linhas.addAll(usuarios);
		
		// Notifica a inclusao dos novos usuarios
		fireTableRowsInserted(indice, indice + usuarios.size());
	}
	
	// Remove todos os registros
	public void limpar() {
		linhas.clear();
		
		// Notifica a limpeza da tabela
		fireTableDataChanged();
	}
	

}
