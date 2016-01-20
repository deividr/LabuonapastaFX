package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import labuonapastafx.model.Cliente;

/**
 * Responsável por todo o procedimento de persistência na base de dados Cliente
 *
 * @author   Deivid Assumpcao Rodrigues
 * @version  %I%, %G%
 * @since    1.0
 */
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
		Cliente cliente;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, clieId);
			ResultSet rs = stm.executeQuery();

			cliente = readNextCliente(rs);

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

		Cliente cliente;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE nm_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, nome);
			ResultSet rs = stm.executeQuery();

			cliente = readNextCliente(rs);

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar cliente: " + e.getMessage());
		}

		return cliente;
	}


	/**
	 * Efetuar a leitura do {@code Cliente} conforme ResultSet informado.
	 *
	 * @param rs ResultSet que se deseja efetuar a leitura para obtenção de um {@code Cliente}
	 * @return Um cliente se encontrado ou um objeto nullo caso não tenho encontrado.
	 * @throws SQLException Caso ocorra um erro na consulta será lançada essa exception.
     */
	private Cliente readNextCliente(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getDate(5));
		} else {
			return null;
		}
	}

	/**
	 * Obter o {@code Cliente} referente ao telefone informado.
	 *
	 * @param telefone
	 *            do {@code Cliente} que se deseja obter.
	 * @return retorna o {@code Cliente} correspondente ao nome informado.
	 */
	public Cliente lerTelefone(String telefone) {

		Cliente cliente;

		String sql = "SELECT cd_cliente, nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao FROM cliente WHERE nr_telefone = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, telefone);
			ResultSet rs = stm.executeQuery();

			cliente = readNextCliente(rs);

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar cliente: " + e.getMessage());
		}

		return cliente;
	}

	/**
	 * Incluir o novo {@code Cliente}.
	 *
	 * @param cliente que deseja ser incluido.
	 */
	public void incluir(Cliente cliente) {

		String sql = "INSERT INTO cliente (nm_cliente, nr_telefone, ds_endereco, "
				+ "dt_criacao) VALUES (?, ?, ?, current_timestamp())";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, cliente.getNome());
			stm.setString(2, cliente.getTelefone1());
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
	 * @param cliente com as informacoes para serem atualizadas na base.
	 */
	public void atualizar(Cliente cliente) {
		String sql = "UPDATE cliente SET nm_cliente = ?, nr_telefone = ?,"
				+ "ds_endereco = ? WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setString(1, cliente.getNome());
			stm.setString(2, cliente.getTelefone1());
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
	 * @param telefone
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
	 * Listar todos os {@code Cliente} que estão cadastrados conforme nome informado.
	 *
	 * @param nome do Cliente que deve ser pesquisado para montagem da lista
	 * @return ArrayList de todos os clientes que se assemelham com o nome informado.
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