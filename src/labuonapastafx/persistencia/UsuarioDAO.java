package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;

/**
 * Responsavel por todo o procedimento de persistencia na base de dados Usuario
 *  
 * @author   Deivid Assumpcao Rodrigues
 * @version  %I%, %G%
 * @since    1.0
 */
public class UsuarioDao {
	
	/**
	 * Obter o usuario relacionado ao login passado como parametro.
	 * 
	 * @param login do usuario que se deseja obter as informacoes.
	 * @return Objeto Usuario que possua o login que foi informado.
	 */
	public Usuario ler(String login) {
		
		Usuario usuarioBase = null;
		
		String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
				+ "st_acesso, ds_senha, cd_ativo FROM USUARIO WHERE nm_login = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, login);
			ResultSet rs = stm.executeQuery();
			
			if (rs.next()) {
				// obter o AcessoEnum relativo ao dominio int armazenado na base.
				AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt(4));
				
				//carregar o usuario da base de dados
				usuarioBase = new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3), 
						acesso, rs.getString(5), rs.getByte(6)); 
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar usuario: " + e.getMessage());
		}
		
		return usuarioBase;
	}

	/**
	 * Incluir um novo usuario
	 * 
	 * @param usuario que se deja incluir nas bases de dados
	 */
	public void incluir(Usuario usuario) {

		String sql = "INSERT INTO usuario (nm_login, nm_usuario, st_acesso, "
				+ "ds_senha, cd_ativo) VALUES(?, ?, ?, ?, 1)";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getNomeCompleto());
			stm.setInt(3, usuario.getTipoAcesso().obterTipoInt());
			stm.setString(4, usuario.getSenha());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir usuario: " + e.getMessage());
		}
		
	}
	
	/**
	 * Atualizar as informacoes do Usuario que foi passado como parametro
	 * 
	 * @param usuario Usuario com as informacoes para serem atualizadas na base
	 */
	public void atualizar(Usuario usuario) {
		
		String sql = "UPDATE usuario SET nm_usuario = ?, st_acesso = ?,"
				+ "ds_senha = ?, cd_ativo = ? WHERE nm_login = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, usuario.getNomeCompleto());
			stm.setInt(2, usuario.getTipoAcesso().obterTipoInt());
			stm.setString(3, usuario.getSenha());
			stm.setInt(4, usuario.getAtivo());
			stm.setString(5, usuario.getLogin());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
		}
		
	}
	
	/**
	 * Excluir o Usuario que foi passado como parametro. A exclusao de usuario deve
	 * ser usada com cautela, pois estah amarrada a outras informacoes do sistema como 
	 * historico, entao o mais indicado seria usar a exclusao logica atraves do metodo
	 * exclusaoLogica.
	 * 
	 * @param login Texto com a informacao de login do usuario que se deseja excluir.
	 */
	public void excluir(String login) {

		String sql = "DELETE FROM usuario WHERE nm_login = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, login);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar usuario: " + e.getMessage());
		}
		
	}

	/**
	 * Excluir logicamente o Usuario que foi passado como parametro. Essa exclusao irah 
	 * marcar o Usuario como excluido atraves de um flag na base, nao serah excluido o
	 * registro fisico.
	 * 
	 * @param login Texto com a informacao de login do usuario que se deseja excluir.
	 */
	public void exclusaoLogica(String login) {
		
		String sql = "UPDATE usuario SET cd_ativo = 0 WHERE nm_login = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, login);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir logicamente usuario: " + e.getMessage());
		}
		
	}
	
	/**
	 * Listar todos os usuarios que estao incluidos na base de dados.
	 * 
	 * @return ArrayList de Usuario que estao incluidos na base de dados.
	 * Caso nao exista nenhum serah retornado um ArrayList vazio.
	 */
	public ArrayList<Usuario> listar() {
		
		String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
				+ "st_acesso, ds_senha, cd_ativo FROM usuario";
		
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				// Obter o AcessoEnum relativo ao dominio int armazenado na base.
				AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt(4));
			
			 	// Carregar o usuario da base de dados
				usuarios.add(new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3), 
						acesso, rs.getString(5), rs.getByte(6)));
			}
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar usuario: " + e.getMessage());
		}
		
		return usuarios;
	}
	
	public ArrayList<Usuario> listar(String login) {
		
		String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
				+ "st_acesso, ds_senha, cd_ativo FROM usuario WHERE nm_login LIKE ?";
		
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)){
			stm.setString(1, login + "%");
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				// Obter o AcessoEnum relativo ao dominio int armazenado na base.
				AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt(4));
			
			 	// Carregar o usuario da base de dados
				usuarios.add(new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3), 
						acesso, rs.getString(5), rs.getByte(6)));
			}
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar usuario: " + e.getMessage());
		}
		
		return usuarios;
	}

}