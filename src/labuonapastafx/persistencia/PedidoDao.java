package labuonapastafx.persistencia;

import labuonapastafx.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import labuonapastafx.model.Pedido;

/**
 * Responsável por todo o procedimento de persistência na base de dados {@code pedido} e
 * {@code item_pedido}.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class PedidoDao {

    /**
     * Incluir um novo {@code pedido}.
     *
     * @param ped Pedido que se deseja incluir.
     */
    public void incluir(Pedido ped) {

        String sql = "INSERT INTO pedido (cd_cliente, cd_usuario, dt_pedido, dt_retirada, hr_de, "
                + "hr_ate) VALUES (?,?,?,?,?,?)";

        try (Connection con = Conexao.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, ped.getClie().getClieId());
            stm.setInt(2, ped.getUsuar().getUserId());
            stm.setDate(3, ped.getDtPedido());
            stm.setString(4, cliente.getEmail());
            stm.setString(5, cliente.getEndereco());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir cliente: " + e.getMessage());
        }

    }

}
