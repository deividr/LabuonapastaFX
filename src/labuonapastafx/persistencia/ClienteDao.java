package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import labuonapastafx.model.Cliente;

public class ClienteDao {

	/**
	 * Obter o {@code Cliente} referente ao Id informado.
	 * 
	 * @param clieId
	 *            Código interno do cliente no cadastro.
	 * 
	 * @return Objeto {@code Cliente} referente ao Id informado.
	 */
	public Cliente lerId(int clieId) {
		Cliente cliente = null;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, clieId);
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				cliente = new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDate(5));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar cliente: " + e.getMessage());
		}

		return cliente;
	}

	/**
	 * Obter o {@code Cliente} referente ao nome informado.
	 *
	 * @param nome
	 *            {@code Cliente} que se deseja obter.
	 * @return retorna o {@code Cliente} correspondente ao nome informado.
	 */
	public Cliente lerNome(String nome) {

		Cliente cliente = null;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE nm_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome);
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				cliente = new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDate(5));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar cliente: " + e.getMessage());
		}

		return cliente;
	}

	/**
	 * Obter o {@code Cliente} referente ao telefone informado.
	 *
	 * @param telefone
	 *            do {@code Cliente} que se deseja obter.
	 * @return retorna o {@code Cliente} correspondente ao nome informado.
	 */
	public Cliente lerTelefone(String telefone) {

		Cliente cliente = null;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE nr_telefone = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, telefone);
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				cliente = new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDate(5));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar cliente: " + e.getMessage());
		}

		return cliente;
	}

	/**
	 * Incluir o novo {@code Cliente}.
	 *
	 * @param {@code Cliente} que deseja ser incluido.
	 */
	public void incluir(Cliente cliente) {

		String sql = "INSERT INTO cliente (nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao) VALUES (?, ?, ?, current_timestamp())";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, cliente.getNome());
			stm.setString(2, cliente.getTelefone());
			stm.setString(3, cliente.getEndereco());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
		}

	}

	/**
	 * Atualizar as informacões do {@code Cliente} que foi passado como
	 * parametro.
	 *
	 * @param {@code Cliente} com as informacoes para serem atualizadas na base.
	 */
	public void atualizar(Cliente cliente) {
		String sql = "UPDATE cliente SET nm_cliente = ?, nr_telefone = ?,"
				+ "ds_endereco = ? WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, cliente.getNome());
			stm.setString(2, cliente.getTelefone());
			stm.setString(3, cliente.getEndereco());
			stm.setInt(4, cliente.getClieId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
		}
	}

	/**
	 * Excluir o {@code Cliente} pelo código interno informado.
	 *
	 * @param clieId
	 *            do {@code Cliente} que se deseja excluir.
	 */
	public void excluirId(int clieId) {

		String sql = "DELETE FROM cliente WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, clieId);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
		}

	}

	/**
	 * Excluir o {@code Cliente} pelo nome informado.
	 *
	 * @param nome
	 *            do {@code Cliente} que se deseja excluir.
	 */
	public void excluirNome(String nome) {

		String sql = "DELETE FROM cliente WHERE nm_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
		}

	}

	/**
	 * Excluir o {@code Cliente} pelo telefone informado.
	 *
	 * @param nome
	 *            do {@code Cliente} que se deseja excluir.
	 */
	public void excluirTelefone(String telefone) {

		String sql = "DELETE FROM cliente WHERE nr_telefone = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, telefone);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
		}

	}

	/**
	 * Listar todos os {@code Cliente} que estão incluídos na base de dados.
	 *
	 * @return ArrayList de {@code Cliente} que estão incluídos na base de
	 *         dados. Caso não exista nenhum será retornado um ArrayList vazio.
	 */
	public ArrayList<Cliente> listar() {

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, dt_criacao "
				+ "FROM cliente";

		ArrayList<Cliente> clientes = new ArrayList<>();

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				// Carregar o cliente da base de dados
				clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDate(5)));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar clientes: " + e.getMessage());
		}

		return clientes;
	}

	/**
	 * Listar todos os {@code Cliente} que estão cadastrados.
	 *
	 * @param nome
	 * @return
	 */
	public ArrayList<Cliente> listar(String nome) {

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, dt_criacao "
				+ "FROM cliente WHERE nm_cliente LIKE ?";

		ArrayList<Cliente> clientes = new ArrayList<>();

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				// Carregar o cliente da base de dados
				clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDate(5)));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
		}

		return clientes;
	}

}
