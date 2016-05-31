package labuonapastafx.persistencia;

import javafx.collections.FXCollections;
import labuonapastafx.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por pela persistência nas bases de dados {@code Pedido} e {@code ItemPedido}.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class PedidoDao {

    private static final String SELECT_ALL = "SELECT cd_pedido, nr_pedido, cd_usuario, " +
            "cd_cliente, dt_pedido, dt_retirada, hr_de, hr_ate, nr_geladeira, ds_observacao, " +
            "st_retirado FROM pedido";

    private static final String MSG_COMPLEMENTO = " - CONTATE O ADMINISTRADOR.";

    /**
     * Incluir um novo {@code pedido}.
     *
     * @param ped Pedido que se deseja incluir.
     */
    public void incluir(Pedido ped) {

        String sql = "INSERT INTO pedido (nr_pedido, cd_cliente, cd_usuario, dt_pedido, " +
                "dt_retirada, hr_de, hr_ate, nr_geladeira, ds_observacao, st_retirado) " +
                "VALUES (?,?,?,?,?,?,?,?,?,false)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, ped.getNumeroPedido());
            stm.setInt(2, ped.getCliente().getClieId());
            stm.setInt(3, ped.getUsuario().getUserId());
            stm.setDate(4, java.sql.Date.valueOf(ped.getDataPedido()));
            stm.setDate(5, java.sql.Date.valueOf(ped.getDataRetirada()));

            if (ped.getHoraDe().equals("")) {
                stm.setNull(6, Types.CHAR);
            } else {
                stm.setString(6, ped.getHoraDe());
            }

            if (ped.getHoraAte().equals("")) {
                stm.setNull(7, Types.CHAR);
            } else {
                stm.setString(7, ped.getHoraAte());
            }

            if (ped.getGeladeira().equals("")) {
                stm.setNull(8, Types.CHAR);
            } else {
                stm.setString(8, ped.getGeladeira());
            }

            if (ped.getObsercao().equals("")) {
                stm.setNull(9, Types.CHAR);
            } else {
                stm.setString(9, ped.getObsercao());
            }

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();

            //Formatar o ID do pedido que foi criado.
            while (rs.next()) {
                ped.setPedId(rs.getInt(1));
            }


        } catch (SQLException e) {

            String msgErro = "Erro na inclusão do Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "incluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        incluirItens(ped);

    }

    /**
     * Incluir os itens pertencentes ao Pedido.
     *
     * @param ped Pedido da qual se deseja incluir os itens.
     */
    private void incluirItens(Pedido ped) {

        String sql = "INSERT INTO item_pedido (cd_pedido, nr_item, cd_produto,"
                + " cd_molho, vl_quantidade) VALUES (?,?,?,?,?)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            for (ItemPedido itemPedido : ped.getItens()) {
                stm.setInt(1, ped.getPedId());
                stm.setInt(2, itemPedido.getCodigo());
                stm.setInt(3, itemPedido.getProduto().getProdId());

                stm.setNull(4, Types.INTEGER);

                if (itemPedido.getMolho() != null
                        && itemPedido.getMolho().getProdId() != 0) {
                    stm.setInt(4, itemPedido.getMolho().getProdId());
                }

                stm.setBigDecimal(5, itemPedido.getQtde());
                stm.addBatch();

            }

            stm.executeBatch();

        } catch (SQLException e) {
            String msgErro = "Erro na inclusão de Itens do Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "incluirItens", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir todos os Pedidos do Cliente informado.
     *
     * @param clie Cliente que se deseja excluir os pedidos.
     */
    public void excluir(Cliente clie) {

        String sql = "DELETE FROM pedido WHERE cd_cliente = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, clie.getClieId());
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao excluir os Pedidos por Cliente" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "excluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir o Pedido que foi informado.
     *
     * @param pedido Pedido que se deseja excluir "permanentemente".
     */
    public void excluir(Pedido pedido) {

        String sql = "DELETE FROM pedido WHERE cd_pedido = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, pedido.getPedId());

            stm.executeUpdate();

        } catch (SQLException e) {
            String msgErro = "Erro ao excluir o Pedido Informado" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "excluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir todos os itens pertecentes ao Pedido informado.
     *
     * @param ped Pedido que se deseja excluir os itens que o compõe.
     */
    private void excluirItens(Pedido ped) {

        String sql = "DELETE FROM item_pedido WHERE cd_pedido = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, ped.getPedId());

            stm.executeUpdate();

        } catch (SQLException e) {
            String msgErro = "Erro ao excluir os Itens do Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "excluirItens", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Alterar o Pedido informado como parâmetro.
     *
     * @param ped Pedido que se deseja alterar as informações.
     */
    public void alterar(Pedido ped) {

        String sql = "UPDATE pedido SET cd_usuario = ?, dt_retirada = ?, hr_de = ?, hr_ate = ?, " +
                "nr_geladeira = ?, ds_observacao = ?, st_retirado = ? WHERE cd_pedido = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            java.sql.Date dtRetirada = java.sql.Date.valueOf(ped.getDataRetirada());

            stm.setInt(1, ped.getUsuario().getUserId());
            stm.setDate(2, dtRetirada);

            if (ped.getHoraDe().equals("")) {
                stm.setNull(3, Types.CHAR);
            } else {
                stm.setString(3, ped.getHoraDe());
            }

            if (ped.getHoraAte().equals("")) {
                stm.setNull(4, Types.CHAR);
            } else {
                stm.setString(4, ped.getHoraAte());
            }

            if (ped.getGeladeira().equals("")) {
                stm.setNull(5, Types.CHAR);
            } else {
                stm.setString(5, ped.getGeladeira());
            }

            if (ped.getObsercao().equals("")) {
                stm.setNull(6, Types.CHAR);
            } else {
                stm.setString(6, ped.getObsercao());
            }

            stm.setBoolean(7, ped.isRetirado());
            stm.setInt(8, ped.getPedId());

            stm.executeUpdate();

        } catch (SQLException e) {
            String msgErro = "Erro ao alterar Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "alterar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        excluirItens(ped);

        incluirItens(ped);

    }

    /**
     * Marcar o pedido como entregue ou não na tabela.
     *
     * @param pedido Objeto Pedido que será marcado como entregue ou não.
     */
    public void marcarPedidoEntregue(Pedido pedido, boolean status) {

        String sql = "UPDATE pedido SET st_retirado = ? WHERE cd_pedido = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setBoolean(1, status);
            stm.setInt(2, pedido.getPedId());

            stm.executeUpdate();

        } catch (SQLException e) {
            String msgErro = "Erro ao marcar Pedido como entregue" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "marcarPedidoEntregue", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param clie Cliente que se deseja consultar os pedidos.
     * @return Lista dos pedidos feito por esse Cliente, ordenada descendentemente por data em que
     * foi feito o pedido.
     */
    public List<Pedido> listarPedidos(Cliente clie) {
        String sql = SELECT_ALL + " WHERE cd_cliente = ? ORDER BY dt_retirada DESC";

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, clie.getClieId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Carregar o Pedido
                pedidos.add(readNextPedido(rs, null, clie));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar os Pedidos por Cliente" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "listarPedidos", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return pedidos;
    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param date Data que se deseja consultar os pedidos. Pesquisa será feita pela campo
     *             Data de Retirada e não Data do Pedido.
     * @return Lista de todos os pedidos feito na data informada em diante, ordenado de forma
     * crescente por data.
     */
    public List<Pedido> listarPedidos(LocalDate date) {
        String sql = SELECT_ALL + " WHERE dt_retirada >= ? ORDER BY dt_retirada ASC";

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setDate(1, Date.valueOf(date));
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Carregar o Pedido
                pedidos.add(readNextPedido(rs, null, null));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar os Pedidos por Data" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "listarPedidos", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return pedidos;
    }

    /**
     * Construir um objeto Pedido com as informações do ResultSet informado.
     *
     * @param rs    RecordSet para leitura da tabela de Pedido.
     * @param usuar Usuario que registrou o Pedido.
     * @param clie  Cliente que efetuou o Pedido.
     * @return Objeto Pedido com as informações do RecordSet.
     * @throws SQLException
     */
    private Pedido readNextPedido(ResultSet rs, Usuario usuar, Cliente clie) throws SQLException {

        Usuario usuarTab;

        //Se usuário já vier preenchido não é necessário consultar a base de usuário.
        if (usuar == null) {
            usuarTab = new UsuarioDao().ler(rs.getInt("cd_usuario"));
        } else {
            usuarTab = usuar;
        }

        Cliente clieTab;

        //Se cliente já vier preenchido não é necessário consultar a base de cliente.
        if (clie == null) {
            clieTab = new ClienteDao().ler(rs.getInt("cd_cliente"));
        } else {
            clieTab = clie;
        }

        LocalDate dtPedido = rs.getDate("dt_pedido").toLocalDate();
        LocalDate dtRetirada = rs.getDate("dt_retirada").toLocalDate();

        List<ItemPedido> itens = listarItensPedido(rs.getInt("cd_pedido"));

        String horaDe = rs.getString("hr_de") == null ? "" : rs.getString("hr_de");
        String horaAte = rs.getString("hr_ate") == null ? "" : rs.getString("hr_ate");
        String geladeira = rs.getString("nr_geladeira") == null ? "" : rs.getString("nr_geladeira");
        String observacao = rs.getString("ds_observacao") == null ? ""
                : rs.getString("ds_observacao");

        return new Pedido()
                .setPedId(rs.getInt("cd_pedido"))
                .setUsuario(usuarTab)
                .setCliente(clieTab)
                .setDataPedido(dtPedido)
                .setDataRetirada(dtRetirada)
                .setNumeroPedido(rs.getInt("nr_pedido"))
                .setHoraDe(horaDe)
                .setHoraAte(horaAte)
                .setGeladeira(geladeira)
                .setObservacao(observacao)
                .setRetirado(rs.getBoolean("st_retirado"))
                .setItens(FXCollections.observableList(itens));
    }

    /**
     * Obter todos os itens do Pedido.
     *
     * @param cdPedido Código do pedido que se deseja obter os itens.
     */
    private List<ItemPedido> listarItensPedido(int cdPedido) {

        String sql = "SELECT nr_item, cd_produto, cd_molho, vl_quantidade FROM item_pedido "
                + "WHERE cd_pedido = ?";

        List<ItemPedido> itens = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdPedido);

            ResultSet rs = stm.executeQuery();

            Produto molho;

            while (rs.next()) {

                ProdutoDao prodDao = new ProdutoDao();

                Produto prod = prodDao.ler(rs.getInt(2));

                if (rs.getInt(3) != 0) {
                    molho = prodDao.ler(rs.getInt(3));
                } else {
                    molho = new Produto();
                }

                itens.add(new ItemPedido(rs.getInt(1), prod, molho, rs.getBigDecimal(4)));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar os Itens do Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "listarItensPedido", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return itens;

    }

    /**
     * Obter o último número de pedido para um determinado período de tempo.
     *
     * @param dtInicial Data do início do período.
     * @param dtFinal   Data do fim do período.
     * @return Integer contendo o último pedido cadastrado para o período. Se não existir vai
     * retornar 0.
     */
    public int obterUltimoNumeroPedido(Date dtInicial, Date dtFinal) {
        String sql = "SELECT MAX(nr_pedido) FROM pedido WHERE dt_retirada >= ? " +
                "AND dt_retirada <= ?";

        Integer numeroPedido;

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setDate(1, dtInicial);
            stm.setDate(2, dtFinal);

            ResultSet rs = stm.executeQuery();

            rs.next();

            numeroPedido = rs.getInt(1);

        } catch (SQLException e) {
            String msgErro = "Erro ao obter último número de Pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "obterUltimoNumeroPedido", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return numeroPedido;
    }


    /**
     * Verificar se o número do pedido informado já existe no período solicitado.
     *
     * @param dtInicial Data do início do período.
     * @param dtFinal   Data do fim do período.
     * @param pedido Pedido a ser consultado.
     * @return Integer contendo o último pedido cadastrado para o período. Se não existir vai
     * retornar 0.
     */
    public boolean verificarNumeroPedido(Date dtInicial, Date dtFinal, Pedido pedido) {
        String sql = "SELECT nr_pedido FROM pedido WHERE dt_retirada >= ? " +
                "AND dt_retirada <= ? AND cd_pedido <> ? AND nr_pedido = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setDate(1, dtInicial);
            stm.setDate(2, dtFinal);
            stm.setInt(3, pedido.getPedId());
            stm.setInt(4, pedido.getNumeroPedido());

            ResultSet rs = stm.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            String msgErro = "Erro ao verificar o número do pedido" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "verificarNumeroPedido", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }


}
