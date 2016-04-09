package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import labuonapastafx.model.Cliente;

/**
 * Responsável por todo o procedimento de persistência na base de dados {@code Cliente}.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ClienteDao {

    /**
     * Obter o {@code Cliente} referente ao Id informado.
     *
     * @param cdCliente Código interno do cliente no cadastro.
     *
     * @return Objeto {@code Cliente} referente ao Id informado.
     */
    public Cliente lerCodCliente(int cdCliente) {
        Cliente cliente;

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, "
                + "ds_endereco, dt_criacao FROM cliente WHERE cd_cliente = ?";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdCliente);
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
     * @param nome {@code Cliente} que se deseja obter.
     * @return retorna o {@code Cliente} correspondente ao nome informado.
     */
    public Cliente lerNome(String nome) {

        Cliente cliente;

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco, "
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
     * Obter o {@code Cliente} referente ao telefone informado.
     *
     * @param telefone Telefone principal do {@code Cliente} que se deseja obter.
     * @return retorna o {@code Cliente} correspondente ao nome informado.
     */
    public Cliente lerTelefone(String telefone) {

        Cliente cliente;

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco, "
                + "dt_criacao FROM cliente WHERE nr_telefone1 = ? or nr_telefone2 = ?";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, telefone);
            stm.setString(2, telefone);
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
            return new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                    rs.getString(6), rs.getDate(7));
        } else {
            return null;
        }
    }

    /**
     * Incluir o novo {@code Cliente}.
     *
     * @param cliente que deseja ser incluido.
     */
    public void incluir(Cliente cliente) {

        String sql = "INSERT INTO cliente (nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco, "
                + "dt_criacao) VALUES (?, ?, ?, ?, ?, current_timestamp())";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone1());
            stm.setString(3, cliente.getTelefone2());
            stm.setString(4, cliente.getEmail());
            stm.setString(5, cliente.getEndereco());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
        }

    }

    /**
     * Atualizar as informacões do {@code Cliente} que foi passado como parametro.
     *
     * @param cliente com as informacoes para serem atualizadas na base.
     */
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nm_cliente = ?, nr_telefone1 = ?, nr_telefone2 = ?, ds_email = ?, "
                + "ds_endereco = ? WHERE cd_cliente = ?";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone1());
            stm.setString(3, cliente.getTelefone2());
            stm.setString(4, cliente.getEmail());
            stm.setString(5, cliente.getEndereco());
            stm.setInt(6, cliente.getClieId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Excluir o {@code Cliente} pelo código interno informado.
     *
     * @param cdCliente Código do {@code Cliente} que se deseja excluir.
     */
    public void excluir(int cdCliente) {

        String sql = "DELETE FROM cliente WHERE cd_cliente = ?";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdCliente);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
        }

    }

    /**
     * Listar todos os {@code Cliente} que estão incluídos na base de dados.
     *
     * @return ArrayList de {@code Cliente} que estão incluídos na base de dados. Caso não exista
     * nenhum será retornado um ArrayList vazio.
     */
    public ArrayList<Cliente> listar() {

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco,"
                + " dt_criacao FROM cliente";

        ArrayList<Cliente> clientes = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Carregar o cliente da base de dados
                clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7)));
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

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco,"
                + " dt_criacao FROM cliente WHERE nm_cliente LIKE ?";

        ArrayList<Cliente> clientes = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nome + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Carregar o cliente da base de dados
                clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7)));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }

        return clientes;
    }

    /**
     * Listar todos os {@code Cliente} que estão cadastrados conforme telefone informado.
     *
     * @param telefone do Cliente que deve ser pesquisado para montagem da lista
     * @return ArrayList de todos os clientes que se assemelham com o nome informado.
     */
    public ArrayList<Cliente> listarTelefone(String telefone) {

        String sql = "SELECT cd_cliente, nm_cliente, nr_telefone1, nr_telefone2, ds_email, ds_endereco,"
                + " dt_criacao FROM cliente WHERE nr_telefone1 LIKE ? OR nr_telefone2 LIKE ?";

        ArrayList<Cliente> clientes = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, telefone + "%");
            stm.setString(2, telefone + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Carregar o cliente da base de dados
                clientes.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7)));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }

        return clientes;
    }

}
