package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;

public class ProdutoDAO {
	
	/**
	 * Obter o produto referente ao nome informado
	 * @param nome do produto que se deseja obter
	 * @return retorna o produto nome correspondente ao nome informado
	 */
	public Produto ler(String nome) {
		
		Produto produto = null;
		
		String sql = "SELECT cd_produto, nm_produto, st_unidade, "
				+ "vl_produto, cd_tipo_produto FROM produto WHERE nm_produto = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome);
			ResultSet rs = stm.executeQuery();
			
			if (rs.next()){
				ProdutoEnum produtoEnum = ProdutoEnum.valueOf(rs.getInt(5));
				UnidadeEnum unidade = UnidadeEnum.valueOfCod(rs.getString(3));
				produto = new Produto(rs.getInt(1), rs.getString(2), unidade, rs.getDouble(4), produtoEnum);
			}
				
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar produto: " + e.getMessage());
		}
		
		
		return produto;
	}

	/**
	 * Incluir o novo produto na base de produtos
	 * 
	 * @param produto que deseja ser incluido
	 */
	public void incluir(Produto produto) {
		
		String sql = "INSERT INTO produto (nm_produto, st_unidade, vl_produto, "
				+ "cd_tipo_produto) VALUES (?,?,?,?)";
		
		try(Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, produto.getNome());
			stm.setString(2, produto.getUnidade().getCodigo());
			stm.setDouble(3, produto.getValor());
			stm.setInt(4, produto.getTipo().getCodigo());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir produto: " + e.getMessage());
		}
				
	}
	
	/**
	 * Atualizar as informacões do Produto que foi passado como parametro
	 * 
	 * @param produto Produto com as informacoes para serem atualizadas na base
	 */
	public void atualizar(Produto produto) {
		String sql = "UPDATE produto SET nm_produto = ?, st_unidade = ?,"
				+ "vl_produto = ?, cd_tipo_produto = ? WHERE cd_produto = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, produto.getNome());
			stm.setString(2, produto.getUnidade().getCodigo());
			stm.setDouble(3, produto.getValor());
			stm.setInt(4, produto.getTipo().getCodigo());
			stm.setInt(5, produto.getCodigo());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
		}		
	}

	/**
	 * Excluir o produto que possua o nome igual ao informado
	 * 
	 * @param nome do produto que se deseja excluir
	 */
	public void excluir(String nome) {
		
		String sql = "DELETE FROM produto WHERE nm_produto = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir produto: " + e.getMessage());			
		}
		
	}

}
