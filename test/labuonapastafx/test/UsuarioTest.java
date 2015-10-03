package labuonapastafx.test;

import static org.junit.Assert.*;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Usuario;

import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testIncluirUsuario() {

        UsuarioNe user = new UsuarioNe();

        assertTrue(user.incluirUsuario("incluir", "Teste de Inclusao", AcessoEnum.CADASTRO, "incluir"));

        Usuario usuario = user.obterUsuario("incluir");

        System.out.println(usuario.toString());

        assertEquals("incluir", usuario.getLogin());
        assertEquals("incluir", usuario.getSenha());
        assertTrue(usuario.isAtivo());

        assertFalse(user.incluirUsuario("incluir", "Teste de Inclusao", AcessoEnum.CADASTRO, "incluir"));

        user.excluirUsuario("incluir");

    }

    @Test
    public void testValidarSenha() {

        UsuarioNe user = new UsuarioNe();

        user.incluirUsuario("validarSenha", "Teste de Validar Senha", AcessoEnum.CADASTRO, "validarSenha");

        assertTrue(user.validarSenha("validarSenha", "validarSenha"));
        assertFalse(user.validarSenha("validarSenha", "naovalidousenha"));

        //excluir o usuario incluido para testes futuros
        user.excluirUsuario("validarSenha");

    }

    @Test
    public void testExcluirLogicamente() {
        UsuarioNe user = new UsuarioNe();

        user.incluirUsuario("exclusaoLogica", "Teste de Exclusao Logica", AcessoEnum.CADASTRO, "exclusaoLogica");

        //verificar se o usuario foi incluido como ativo
        assertTrue(user.obterUsuario("exclusaoLogica").isAtivo());

        //efetuar a exclusao logica, atualizando o usuario para inativo
        assertTrue(user.exclusaoLogica("exclusaoLogica"));

        //verificar se o usuario estah inativo
        assertFalse(user.obterUsuario("exclusaoLogica").isAtivo());

        //tentar excluir logicamente um usuario que nao existe
        assertFalse(user.excluirUsuario("exclusaoIlogica"));

        user.excluirUsuario("exclusaoLogica");

    }

    @Test
    public void testAlterarUsuario() {
        UsuarioNe user = new UsuarioNe();

        user.incluirUsuario("alterar", "Teste de Alteracao", AcessoEnum.CADASTRO, "alterar");

        assertTrue(user.alterarUsuario("alterar", "Teste de Alteracao com Sucesso", AcessoEnum.PEDIDO, "alterado"));

        Usuario usuario = user.obterUsuario("alterar");

        assertEquals("alterado", usuario.getSenha());
        assertEquals("alterar", usuario.getLogin());
        assertEquals("Teste de Alteracao com Sucesso", usuario.getNomeCompleto());
        assertEquals(AcessoEnum.PEDIDO, usuario.getTipoAcesso());
        assertTrue(usuario.isAtivo());

        user.excluirUsuario("alterar");

    }

    @Test
    public void testExcluirUsuario() {
        UsuarioNe user = new UsuarioNe();

        user.incluirUsuario("excluir", "Teste de Exclusao", AcessoEnum.CADASTRO, "excluir");

        assertTrue(user.excluirUsuario("excluir"));

        assertEquals(null, user.obterUsuario("excluir"));
    }

}
