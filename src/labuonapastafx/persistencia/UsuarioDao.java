package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;

/**
 * Responsável pelos procedimentos de persistência na base de dados {@code Usuário}.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class UsuarioDao {

    private static final String MSG_COMPLEMENTO = " - CONTATE O ADMINISTRADOR.";

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
                AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt("st_acesso"));

                //carregar o usuario da base de dados
                usuarioBase = new Usuario(rs.getInt("cd_usuario"), rs.getString("nm_login"),
                        rs.getString("nm_usuario"), acesso, rs.getString("ds_senha"),
                        rs.getByte("cd_ativo"));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao consultar usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "ler", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return usuarioBase;
    }

    /**
     * Obter o Usuário relacionado ao código do Usuário passado como parâmetro.
     *
     * @param cdUsuario Código do Usuário que se deseja obter as informações.
     * @return Usuário que possua o login que foi informado.
     */
    public Usuario ler(int cdUsuario) {

        Usuario usuarioBase = null;

        String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
                + "st_acesso, ds_senha, cd_ativo FROM usuario WHERE cd_usuario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                // obter o AcessoEnum relativo ao dominio int armazenado na base.
                AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt("st_acesso"));

                //carregar o usuario da base de dados
                usuarioBase = new Usuario(rs.getInt("cd_usuario"), rs.getString("nm_login"),
                        rs.getString("nm_usuario"), acesso, rs.getString("ds_senha"),
                        rs.getByte("cd_ativo"));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao consultar usuário por código" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "ler", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return usuarioBase;
    }

    /**
     * Incluir um novo usuário
     *
     * @param usuario que se deja incluir nas bases de dados
     */
    public void incluir(Usuario usuario) {

        String sql = "INSERT INTO usuario (nm_login, nm_usuario, st_acesso, "
                + "ds_senha, cd_ativo) VALUES(?, ?, ?, ?, 1)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, usuario.getLogin());
            stm.setString(2, usuario.getNomeCompleto());
            stm.setInt(3, usuario.getTipoAcesso().obterTipoInt());
            stm.setString(4, usuario.getSenha());
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao incluir Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "incluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Atualizar as informacoes do Usuario que foi passado como parametro
     *
     * @param usuario Usuario com as informacoes para serem atualizadas na base
     */
    public void atualizar(Usuario usuario) {

        String sql = "UPDATE usuario SET nm_login = ?, nm_usuario = ?, st_acesso = ?,"
                + "ds_senha = ?, cd_ativo = ? WHERE cd_usuario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, usuario.getLogin());
            stm.setString(2, usuario.getNomeCompleto());
            stm.setInt(3, usuario.getTipoAcesso().obterTipoInt());
            stm.setString(4, usuario.getSenha());
            stm.setInt(5, usuario.getAtivo());
            stm.setInt(6, usuario.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao atualizar Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "ler", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir o Usuario que foi passado como parametro. A exclusao de usuario deve
     * ser usada com cautela, pois estah amarrada a outras informacoes do sistema como
     * historico, entao o mais indicado seria usar a exclusao logica atraves do metodo
     * exclusaoLogica.
     *
     * @param cdUsuario Código de Usuário que se deseja excluir.
     */
    public void excluir(int cdUsuario) {

        String sql = "DELETE FROM usuario WHERE cd_usuario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao excluir Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "excluir", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Excluir logicamente o Usuario que foi passado como parametro. Essa exclusao irah
     * marcar o Usuario como excluido atraves de um flag na base, nao serah excluido o
     * registro fisico.
     *
     * @param cdUsuario Código do Usuario que se deseja excluir.
     */
    public void exclusaoLogica(int cdUsuario) {

        String sql = "UPDATE usuario SET cd_ativo = 0 WHERE cd_usuario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            stm.executeUpdate();
        } catch (SQLException e) {
            String msgErro = "Erro ao excluir logicamente Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "exclusaoLogica", msgErro, e);

            throw new RuntimeException(msgErro);
        }

    }

    /**
     * Listar todos os usuarios que estao incluidos na base de dados.
     *
     * @return List de Usuario que estao incluidos na base de dados.
     * Caso nao exista nenhum serah retornado um List vazio.
     */
    public List<Usuario> listar() {

        String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
                + "st_acesso, ds_senha, cd_ativo FROM usuario";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Obter o AcessoEnum relativo ao dominio int armazenado na base.
                AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt("st_acesso"));

                //carregar o usuario da base de dados
                usuarios.add(new Usuario(rs.getInt("cd_usuario"), rs.getString("nm_login"),
                        rs.getString("nm_usuario"), acesso, rs.getString("ds_senha"),
                        rs.getByte("cd_ativo")));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "listar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return usuarios;
    }

    public ArrayList<Usuario> listar(String login) {

        String sql = "SELECT cd_usuario, nm_login, nm_usuario, "
                + "st_acesso, ds_senha, cd_ativo FROM usuario WHERE nm_login LIKE ?";

        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, login + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Obter o AcessoEnum relativo ao dominio int armazenado na base.
                AcessoEnum acesso = AcessoEnum.valueOf(rs.getInt("st_acesso"));

                //carregar o usuario da base de dados
                usuarios.add(new Usuario(rs.getInt("cd_usuario"), rs.getString("nm_login"),
                        rs.getString("nm_usuario"), acesso, rs.getString("ds_senha"),
                        rs.getByte("cd_ativo")));
            }

        } catch (SQLException e) {
            String msgErro = "Erro ao listar Usuário" + MSG_COMPLEMENTO;

            Log.logar(UsuarioDao.class.getName(), "listar", msgErro, e);

            throw new RuntimeException(msgErro);
        }

        return usuarios;
    }

}
