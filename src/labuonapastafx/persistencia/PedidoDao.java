package labuonapastafx.persistencia;

import labuonapastafx.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Responsável por todo o procedimento de persistência na base de dados
 * {@code pedido} e {@code item_pedido}.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class PedidoDao {

	/**
	 * Incluir um novo {@code pedido}.
	 *
	 * @param ped
	 *            Pedido que se deseja incluir.
	 */
	public Pedido incluir(Pedido ped) {

		String sql = "INSERT INTO pedido (cd_cliente, cd_usuario, dt_pedido, dt_retirada, hr_de, "
				+ "hr_ate) VALUES (?,?,?,?,?,?)";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql,
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			stm.setInt(1, ped.getClie().getClieId());
			stm.setInt(2, ped.getUsuario().getUserId());
			stm.setDate(3, java.sql.Date.valueOf(ped.getDtPedido()));
			stm.setDate(4, java.sql.Date.valueOf(ped.getDtRetirada()));
			stm.setInt(5, ped.getHoraDe());
			stm.setInt(6, ped.getHoraAte());

			stm.executeUpdate();

			ResultSet rs = stm.getGeneratedKeys();

			while (rs.next()) {
				ped.setPedId(rs.getInt(1));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir pedido: " + e.getMessage());
		}

		incluirItens(ped);

		return ped;

	}

	/**
	 * Incluir os itens pertencentes ao Pedido.
	 *
	 * @param ped
	 */
	private void incluirItens(Pedido ped) {

		String sql = "INSERT INTO item_pedido (cd_pedido, nr_item, cd_produto,"
				+ " cd_molho, vl_quantidade) VALUES (?,?,?,?,?)";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {

			for (ItemPedido itemPedido : ped.getItens()) {
				try {
					stm.setInt(1, ped.getPedId());
					stm.setInt(2, itemPedido.getCodigo());
					stm.setInt(3, itemPedido.getProduto().getProdId());

					stm.setNull(4, Types.INTEGER);

					if (itemPedido.getMolho().getProdId() != 0)
						stm.setInt(4, itemPedido.getMolho().getProdId());

					stm.setBigDecimal(5, itemPedido.getQtde());
					stm.addBatch();
				} catch (SQLException e) {
					throw new RuntimeException(
							"Erro ao incluir itens do pedido: " + e.getMessage());
				}
			}

			stm.executeBatch();

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao incluir itens do pedido: " + e.getMessage());
		}

	}

	/**
	 * Obter pedidos que pertence a um determinado {@code Cliente}.
	 *
	 * @param clieId
	 *            Código do Cliente que se deseja consultar os pedidos.
	 * @return
	 */
	public ArrayList<Pedido> obterPedidosCliente(int clieId) {
		String sql = "SELECT cd_pedido, cd_usuario, cd_cliente, dt_pedido, dt_retirada, hr_de, "
				+ "hr_ate FROM pedido WHERE cd_cliente = ?";

		ArrayList<Pedido> pedidos = new ArrayList<>();

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, clieId);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				// Carregar o Pedido
				Usuario usuar = new UsuarioDao().lerCodUsuario(rs.getInt(2));
				Cliente clie = new ClienteDao().lerCodCliente(rs.getInt(3));

				LocalDate dtPedido = rs.getDate(4).toLocalDate();
				LocalDate dtRetirada = rs.getDate(5).toLocalDate();

				ArrayList<ItemPedido> itens = obterItensPedido(rs.getInt(1));

				pedidos.add(new Pedido(rs.getInt(1), usuar, clie, dtPedido, dtRetirada,
						rs.getInt(6), rs.getInt(7), itens));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao consultar pedidos do Cliente: " + e.getMessage());
		}

		return pedidos;
	}

	/**
	 * Obter todos os itens do Pedido.
	 *
	 * @param cdPedido
	 *            Código do pedido que se deseja obter os itens.
	 */
	private ArrayList<ItemPedido> obterItensPedido(int cdPedido) {

		String sql = "SELECT nr_item, cd_produto, cd_molho, vl_quantidade FROM item_pedido "
				+ "WHERE cd_pedido = ?";

		ArrayList<ItemPedido> itens = new ArrayList<>();

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, cdPedido);

			ResultSet rs = stm.executeQuery();

			Produto molho;

			while (rs.next()) {

				ProdutoDao prodDao = new ProdutoDao();

				Produto prod = prodDao.lerCodProduto(rs.getInt(2));

				if (rs.getInt(3) != 0) {
					molho = prodDao.lerCodProduto(rs.getInt(3));
				} else {
					molho = new Produto();
				}

				itens.add(new ItemPedido(rs.getInt(1), prod, molho, rs.getBigDecimal(4)));
			}

		} catch (SQLException e) {
			throw new RuntimeException(
					"Erro ao consultar itens dos pedidos do Cliente: " + e.getMessage());
		}

		return itens;

	}

	/**
	 * Excluir todos os Pedidos do Cliente informado.
	 *
	 * @param clie
	 *            Cliente que se deseja excluir os pedidos.
	 */
	public void excluirPorCliente(Cliente clie) {

		String sql = "DELETE FROM pedido WHERE cd_cliente = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {
			stm.setInt(1, clie.getClieId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir os pedidos do Cliente: " + e.getMessage());
		}

	}

	/**
	 * Excluir o Pedido que foi informado.
	 *
	 * @param pedido
	 *            Pedido que se deseja excluir "permanentemente".
	 */
	public void excluir(Pedido pedido) {

		String sql = "DELETE FROM pedido WHERE cd_pedido = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {

			stm.setInt(1, pedido.getPedId());

			stm.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir o pedido do Cliente: " + e.getMessage());
		}

	}

	/**
	 * Excluir todos os itens pertecentes ao Pedido informado.
	 *
	 * @param pedido
	 *            Pedido que se deseja excluir os itens que o compõe.
	 */
	private void excluirItensPedido(Pedido pedido) {

		String sql = "DELETE FROM item_pedido WHERE cd_pedido = ?";

		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {

			stm.setInt(1, pedido.getPedId());

			stm.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(
					"Erro ao excluir itens dos pedidos do Cliente: " + e.getMessage());
		}

	}

	/**
	 * Alterar o Pedido informado como parâmetro.
	 * 
	 * @param pedido
	 *            Pedido que se deseja alterar as informações.
	 */
	public void alterar(Pedido pedido) {
		
		excluirItensPedido(pedido);
		
		String sql = "UPDATE pedido SET cd_usuario = ?, dt_retirada = ?, hr_de = ?, hr_ate = ? "
				+ "WHERE cd_pedido = ?";
		
		try (Connection con = Conexao.getConexao();
				PreparedStatement stm = con.prepareStatement(sql)) {

			java.sql.Date dtRetirada = java.sql.Date.valueOf(pedido.getDtRetirada());

			stm.setInt(1, pedido.getUsuario().getUserId());
			stm.setDate(2, dtRetirada);
			stm.setInt(3, pedido.getHoraDe());
			stm.setInt(4, pedido.getHoraAte());
			stm.setInt(5, pedido.getPedId());			

			stm.executeUpdate();
			
			incluirItens(pedido);

		} catch (SQLException e) {
			throw new RuntimeException(
					"Erro ao atualizar o Pedido do Cliente: " + e.getMessage());
		}				
		
	}

}
