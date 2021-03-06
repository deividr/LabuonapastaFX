package labuonapastafx.controller;

import java.util.ArrayList;
import java.util.List;

import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;
import labuonapastafx.persistencia.UsuarioDao;

public class UsuarioNe {

    private final UsuarioDao usuarioDao;

    public UsuarioNe() {
        usuarioDao = new UsuarioDao();
    }

    /**
     * Metodo para verificar se existe usuario informado cadastrado na base de dados
     *
     * @param login Login do Usuário que se deseja pesquisar.
     * @return Retonar um objeto do tipo Usuário.
     */
    public Usuario obterUsuario(String login) {

        return usuarioDao.ler(login);

    }

    /**
     * Metodo para verificar se existe usuario informado cadastrado na base de dados
     *
     * @param cdUsuario Código do Usuário que se deseja pesquisar.
     * @return
     */
    public Usuario obterCodUsuario(int cdUsuario) {

        return usuarioDao.ler(cdUsuario);

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
        } else return usuario.getSenha().equals(senha);

    }

    /**
     * Incluir um novo usuario no sistema
     *
     * @param login
     * @param nomeCompleto
     * @param tipoAcesso
     * @param senha
     * @return true se incluido com sucesso e false se ocorreu algum erro (Ex.: Usuário já existe)
     */
    public boolean incluirUsuario(String login, String nomeCompleto, AcessoEnum tipoAcesso, String senha) {

        if (obterUsuario(login) == null) {

            Usuario usuario = new Usuario(0, login, nomeCompleto, tipoAcesso, senha, (byte) 1);

            usuarioDao.incluir(usuario);

            return true;
        } else {
            // retorno false indica que o usuario ja existe
            return false;
        }

    }

    /**
     * Alterar as informações do usuário conforme passado via parâmetro.
     *
     * @param cdUsuario    Código do Usuário que se deseja alterar.
     * @param login
     * @param nomeCompleto
     * @param tipoAcesso
     * @param senha
     * @return true Se a alteração ocorreu com sucesso, ou false se ocorreu algum erro(Ex.: Usuário já existe)
     */
    public boolean alterarUsuario(int cdUsuario, String login, String nomeCompleto, AcessoEnum tipoAcesso,
                                  String senha) {

        Usuario usuar = obterCodUsuario(cdUsuario);

        // Se o usuario existir atualiza, senao retorna false para o chamador
        if (usuar != null) {

            // Se o login foi alterado, verificar se não pertence a outro Usuário.
            if (!usuar.getLogin().equals(login)) {

                Usuario usuar2 = obterUsuario(login);

                if (usuar2 != null)
                    // Se o login do Usuário alterado pertence a outro Usuário, invalida a alteração.
                    if (usuar.getUserId() != usuar2.getUserId())
                        return false;
            }

            usuar.setNomeCompleto(nomeCompleto);
            usuar.setTipoAcesso(tipoAcesso);
            usuar.setSenha(senha);
            usuar.setAtivo((byte) 1);

            usuarioDao.atualizar(usuar);

            return true; //retorna que a atualizacao foi ok
        } else {
            return false;
        }

    }

    /**
     * Excluir o usuário logicamente, essa exclusão não irá eliminar o usuário
     * da tabela, apenas torna-lo inativo. Isso é útil para o histórico do sistema.
     *
     * @param cdUsuario
     * @return
     */
    public boolean exclusaoLogica(int cdUsuario) {

        if (obterCodUsuario(cdUsuario) != null) {
            usuarioDao.exclusaoLogica(cdUsuario);
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
     * @param cdUsuario
     * @return
     */
    public boolean excluirUsuario(int cdUsuario) {

        if (obterCodUsuario(cdUsuario) != null) {
            usuarioDao.excluir(cdUsuario);
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
    public List<Usuario> listarUsuarios() {

        return usuarioDao.listar();

    }

    /**
     * Retorna uma lista das usuários que começam com a informação
     * passa por parâmetro.
     *
     * @param login
     * @return
     */
    public ArrayList<Usuario> listarUsuarios(String login) {

        return usuarioDao.listar(login);

    }

}
