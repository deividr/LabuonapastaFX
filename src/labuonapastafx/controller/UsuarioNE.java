package labuonapastafx.controller;

import java.util.ArrayList;

import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;
import labuonapastafx.persistencia.UsuarioDao;

public class UsuarioNe {

    private UsuarioDao usuarioDao;

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
     * @return true se incluido com sucesso e false se ocorreu algum erro (Ex.: Usuário já existe)
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

    /**
     * Alterar as informações do usuário conforme passado via parâmetro.
     * 
     * @param login
     * @param nomeCompleto
     * @param tipoAcesso
     * @param senha
     * @return true Se a alteração ocorreu com sucesso, ou false se ocorreu algum erro(Ex.: Usuário já existe)
     */
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

    /**
     * Excluir o usuário logicamente, essa exclusão não irá eliminar o usuário
     * da tabela, apenas torna-lo inativo. Isso é útil para o histórico do sistema.
     * 
     * @param login
     * @return
     */
    public boolean exclusaoLogica(String login) {

        if (obterUsuario(login) != null) {
            getUsuarioDAO().exclusaoLogica(login);
            return true;
        } else {
            //usuario nao existe
            return false;
        }

    }

    /**
     * Excluir o usuário fisicamente das bases de dados. CUIDADO:Essa exclusão pode
     * compromenter as informações de históricos do sistema
     * 
     * @param login
     * @return
     */
    public boolean excluirUsuario(String login) {

        if (obterUsuario(login) != null) {
            getUsuarioDAO().excluir(login);
            return true;
        } else {
            //usuario nao existe
            return false;
        }

    }

    /**
     * Retorna uma lista dos usuários cadastrados na base de dados.
     * 
     * @return
     */
    public ArrayList<Usuario> listarUsuarios() {

        return getUsuarioDAO().listar();

    }

    /**
     * Retorna uma lista das usuários que começam com a informação
     * passa por parâmetro.
     * 
     * @param login
     * @return
     */
    public ArrayList<Usuario> listarUsuarios(String login) {

        return getUsuarioDAO().listar(login);

    }

    /**
     * Irá retornar um objeto da classe de persistência UsuarioDao.
     * Esse método tem por objetivo evitar a criação de diversas instâncias dessa
     * classe que pode ocorrer durante o uso do sistema.
     * 
     * @return
     */
    private UsuarioDao getUsuarioDAO() {
        if (this.usuarioDao == null) {
            this.usuarioDao = new UsuarioDao();
        }

        return this.usuarioDao;
    }
}
