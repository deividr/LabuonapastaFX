package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;

public class ProdutoDao {

    private static final String MSG_COMPLEMENTO = " - CONTATE O ADMINISTRADOR.";

    /**
     * Obter o produto referente ao nome informado.
     *
     * @param nome Nome do produto que se deseja obter.
     * @return Produto correspondente ao nome informado.
     */
    public Produto ler(String nome) {

        Produto produto = null;

        String sql = "SELECT cd_produto, nm_produto, st_unidade, vl_produto, cd_tipo_produto, " +
                "cd_ativo FROM produto WHERE nm_produto = ?";


        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nome);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                ProdutoEnum produtoEnum = ProdutoEnum.valueOf(rs.getInt("cd_tipo_produto"));
                UnidadeEnum unidade = UnidadeEnum.valueOfCod(rs.getString("st_unidade"));

                produto = new Produto(rs.getInt("cd_produto"), rs.getString("nm_produto"),
                        unidade, rs.getBigDecimal("vl_produto"),
                        produtoEnum, rs.getByte("cd_ativo"));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao consultar Produto por nome" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "ler", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return produto;
    }

    /**
     * Obter o produto referente ao código informado.
     *
     * @param cdProduto Código do produto que se deseja obter.
     * @return Produto correspondente ao código informado.
     */
    public Produto ler(int cdProduto) {

        Produto produto = null;

        String sql = "SELECT cd_produto, nm_produto, st_unidade, "
                + "vl_produto, cd_tipo_produto, cd_ativo FROM produto WHERE cd_produto = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdProduto);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                ProdutoEnum produtoEnum = ProdutoEnum.valueOf(rs.getInt("cd_tipo_produto"));
                UnidadeEnum unidade = UnidadeEnum.valueOfCod(rs.getString("st_unidade"));

                produto = new Produto(rs.getInt("cd_produto"), rs.getString("nm_produto"),
                        unidade, rs.getBigDecimal("vl_produto"),
                        produtoEnum, rs.getByte("cd_ativo"));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao consultar Produto por código" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "ler", msgErro, e);

            throw new RuntimeException(msgErro);
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
                + "cd_tipo_produto, cd_ativo) VALUES (?, ?, ?, ?, 1)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getUnidade().getCodigo());
            stm.setBigDecimal(3, produto.getValor());
            stm.setInt(4, produto.getTipo().getCodigo());
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao incluir Produto" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "incluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Atualizar as informacões do Produto que foi passado como parametro
     *
     * @param produto Produto com as informações para serem atualizadas na base.
     */
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nm_produto = ?, st_unidade = ?,"
                + "vl_produto = ?, cd_tipo_produto = ?, cd_ativo = 1 WHERE cd_produto = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getUnidade().getCodigo());
            stm.setBigDecimal(3, produto.getValor());
            stm.setInt(4, produto.getTipo().getCodigo());
            stm.setInt(5, produto.getProdId());
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao atualizar Produto" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "atualizar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir o {@code Produto} que foi passado como parametro. A exclusão do
     * produto deve ser usada com cautela, pois está amarrada a outras
     * informações do sistema como histórico, então o mais indicado seria usar a
     * exclusão lógica através do método exclusaoLogica.
     *
     * @param cdProduto Código do {@code Produto} que se deseja excluir.
     */
    public void excluir(int cdProduto) {

        String sql = "DELETE FROM produto WHERE cd_produto = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdProduto);
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao excluir Produto" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "excluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir logicamente o {@code Produto} que foi passado como parametro.
     * <p>
     * Essa exclusão irá marcar o Produto como excluído através de um flag na
     * base, não será excluido o registro físico.
     *
     * @param cdProduto Código do {@code Produto} que se deseja excluir.
     */
    public void exclusaoLogica(int cdProduto) {

        String sql = "UPDATE produto SET cd_ativo = 0 WHERE cd_produto = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdProduto);
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao excluir logicamente o Produto" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "exclusaoLogica", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Listar todos os {@code Produto} que estão incluídos na base de dados.
     *
     * @return List de {@code Produto} que estão incluídos na base de
     * dados. Caso não exista nenhum será retornado um List vazio.
     */
    public List<Produto> listar() {

        String sql = "SELECT cd_produto, nm_produto, st_unidade, "
                + "vl_produto, cd_tipo_produto, cd_ativo FROM produto";

        List<Produto> produtos = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Obter o UnidadeEnum e ProdutoEnum relativo ao domínio
                // armazenado na base.
                UnidadeEnum unidade = UnidadeEnum.valueOfCod(rs.getString("st_unidade"));
                ProdutoEnum tipo =
                        ProdutoEnum.valueOf(Integer.parseInt(rs.getString("cd_tipo_produto")));

                // Carregar o usuario da base de dados
                produtos.add(new Produto(rs.getInt("cd_produto"), rs.getString("nm_produto"),
                        unidade, rs.getBigDecimal("vl_produto"), tipo, rs.getByte("cd_ativo")));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar Produtos" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "listar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return produtos;
    }

    /**
     * Listar todos os {@code Produto} que estão cadastrados.
     *
     * @param nome Nome que deseja usar para limitar a lista de consulta.
     * @return List com todas as ocorrências encontradas.
     */
    public List<Produto> listar(String nome) {

        String sql = "SELECT cd_produto, nm_produto, st_unidade, vl_produto, cd_tipo_produto, " +
                "cd_ativo FROM produto " + "WHERE nm_produto LIKE ?";

        List<Produto> produtos = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nome + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Obter o UnidadeEnum e ProdutoEnum relativo ao domínio
                // armazenado na base.
                UnidadeEnum unidade = UnidadeEnum.valueOfCod(rs.getString("st_unidade"));
                ProdutoEnum tipo = ProdutoEnum.valueOf(
                        Integer.parseInt(rs.getString("cd_tipo_produto")));

                // Carregar o usuario da base de dados
                produtos.add(new Produto(rs.getInt("cd_produto"), rs.getString("nm_produto"),
                        unidade, rs.getBigDecimal("vl_produto"), tipo, rs.getByte("cd_ativo")));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar Produtos por nome" + MSG_COMPLEMENTO;

            Log.logar(PedidoDao.class.getName(), "listar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return produtos;
    }
}
