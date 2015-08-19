package labuonapastafx.controller;

import java.util.ArrayList;

import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;
import labuonapastafx.persistencia.UsuarioDAO;

public class UsuarioNE {

    private UsuarioDAO usuarioDAO;

    /**
     * Metodo para verificar se existe usuario informado cadastrado na base de
     * dados
     */
    public Usuario obterUsuario(String login) {

        return getUsuarioDAO().ler(login);

    }

    /**
     * Verificar se a senha confere com o login informado.
     *
     * @param login
     * @param senha
     * @return True caso a senha e o usuario estejam tudo ok. False caso a senha
     * nao coincida, usuario invalido ou usuario esteja inativo.
     */
    public boolean validarSenha(String login, String senha) {

        Usuario usuario = obterUsuario(login);

        //Se o usuario nao existir ou estiver inativo serah retornado false
        if (usuario == null || !usuario.isAtivo()) {
            return false;
        } else if (usuario.getSenha().equals(senha)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Incluir um novo usuario no sistema
     *
     * @param usuario
     * @param senha
     * @param nomeCompleto
     * @param tipoAcesso
     * @return
     */
    public boolean incluirUsuario(String login, String nomeCompleto, AcessoEnum tipoAcesso, String senha) {

        if (obterUsuario(login) == null) {

            Usuario usuario = new Usuario(0, login, nomeCompleto, tipoAcesso, senha, (byte) 1);

            getUsuarioDAO().incluir(usuario);

            return true;
        } else {
            // retorno false indica que o usuario ja existe
            return false;
        }

    }

    public boolean alterarUsuario(String login, String nomeCompleto, AcessoEnum tipoAcesso, String senha) {

        Usuario usuario = obterUsuario(login);

        // se o usuario existir atualiza, senao retorna false para o chamador
        if (usuario != null) {
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setTipoAcesso(tipoAcesso);
            usuario.setSenha(senha);
            usuario.setAtivo((byte) 1);

            getUsuarioDAO().atualizar(usuario);

            return true; //retorna que a atualizacao foi ok
        } else {
            return false;
        }

    }

    public boolean exclusaoLogica(String login) {

        if (obterUsuario(login) != null) {
            getUsuarioDAO().exclusaoLogica(login);
            return true;
        } else {
            //usuario nao existe
            return false;
        }

    }

    public boolean excluirUsuario(String login) {

        if (obterUsuario(login) != null) {
            getUsuarioDAO().excluir(login);
            return true;
        } else {
            //usuario nao existe
            return false;
        }

    }

    public ArrayList<Usuario> listarUsuarios() {

        return getUsuarioDAO().listar();

    }

    public ArrayList<Usuario> listarUsuarios(String login) {

        return getUsuarioDAO().listar(login);

    }

    private UsuarioDAO getUsuarioDAO() {
        if (this.usuarioDAO == null) {
            this.usuarioDAO = new UsuarioDAO();
        }

        return this.usuarioDAO;
    }
}
