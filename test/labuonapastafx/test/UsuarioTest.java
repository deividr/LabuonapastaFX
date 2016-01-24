package labuonapastafx.test;

import static org.junit.Assert.*;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;

import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {

    private UsuarioNe userNe;

    @Before
    public void setUp() throws Exception {

        userNe = new UsuarioNe();

        try {
            userNe.excluirUsuario(userNe.obterUsuario("incluir").getUserId());
            userNe.excluirUsuario(userNe.obterUsuario("validarSenha").getUserId());
            userNe.excluirUsuario(userNe.obterUsuario("alterar").getUserId());
            userNe.excluirUsuario(userNe.obterUsuario("excluir").getUserId());
            userNe.excluirUsuario(userNe.obterUsuario("exclusaoLogica").getUserId());
        } catch (NullPointerException ne) {

        }

    }

    @Test
    public void testIncluirUsuario() {

        assertTrue(userNe.incluirUsuario("incluir", "Teste de Inclusao", AcessoEnum.CADASTRO, "incluir"));

        Usuario usuario = userNe.obterUsuario("incluir");

        assertEquals("incluir", usuario.getLogin());
        assertEquals("Teste de Inclusao", usuario.getNomeCompleto());
        assertEquals(AcessoEnum.CADASTRO, usuario.getTipoAcesso());
        assertEquals("incluir", usuario.getSenha());
        assertTrue(usuario.isAtivo());

        assertFalse(userNe.incluirUsuario("incluir", "Teste de Inclusao", AcessoEnum.CADASTRO, "incluir"));

        userNe.excluirUsuario(usuario.getUserId());

    }

    @Test
    public void testValidarSenha() {

        userNe.incluirUsuario("validarSenha", "Teste de Validar Senha", AcessoEnum.CADASTRO, "validarSenha");

        assertTrue(userNe.validarSenha("validarSenha", "validarSenha"));
        assertFalse(userNe.validarSenha("validarSenha", "naovalidousenha"));

        //excluir o usuario incluido para testes futuros
        userNe.excluirUsuario(userNe.obterUsuario("validarSenha").getUserId());

    }

    @Test
    public void testExcluirLogicamente() {

        userNe.incluirUsuario("exclusaoLogica", "Teste de Exclusao Logica", AcessoEnum.CADASTRO, "exclusaoLogica");

        Usuario usuar = userNe.obterUsuario("exclusaoLogica");

        //verificar se o usuario foi incluido como ativo
        assertTrue(usuar.isAtivo());

        //efetuar a exclusao logica, atualizando o usuario para inativo
        assertTrue(userNe.exclusaoLogica(usuar.getUserId()));

        usuar = userNe.obterUsuario("exclusaoLogica");

        //verificar se o usuario estah inativo
        assertFalse(usuar.isAtivo());

        userNe.excluirUsuario(usuar.getUserId());

    }

    @Test
    public void testAlterarUsuario() {

        userNe.incluirUsuario("alterar", "Teste de Alteracao", AcessoEnum.CADASTRO, "alterar");

        Usuario user = userNe.obterUsuario("alterar");

        assertTrue(userNe.alterarUsuario(user.getUserId(), "alterar", "Teste de Alteracao com Sucesso",
                AcessoEnum.PEDIDO, "alterado"));

        Usuario usuar = userNe.obterUsuario("alterar");

        assertEquals("alterar", usuar.getLogin());
        assertEquals("Teste de Alteracao com Sucesso", usuar.getNomeCompleto());
        assertEquals(AcessoEnum.PEDIDO, usuar.getTipoAcesso());
        assertEquals("alterado", usuar.getSenha());
        assertTrue(usuar.isAtivo());

        userNe.excluirUsuario(usuar.getUserId());

    }

    @Test
    public void testExcluirUsuario() {

        userNe.incluirUsuario("excluir", "Teste de Exclusao", AcessoEnum.CADASTRO, "excluir");

        Usuario usuar = userNe.obterUsuario("excluir");

        assertTrue(userNe.excluirUsuario(usuar.getUserId()));

        assertEquals(null, userNe.obterUsuario("excluir"));

    }

}
