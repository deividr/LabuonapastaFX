package labuonapastafx.test;

import static org.junit.Assert.*;
import labuonapastafx.controller.ClienteNe;
import labuonapastafx.model.Cliente;
import org.junit.Before;
import org.junit.Test;

public class ClienteTest {

    private ClienteNe clie;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        clie = new ClienteNe();

        // Excluir das bases todos os clientes usados nos testes, para o caso de
        // algum existir ainda de testes anteriores.
        clie.excluirNome("Incluir Cliente Tal");
        clie.excluirNome("Alterar Cliente Tal");
        clie.excluirNome("Alterar Cliente Tal para Isso");
        clie.excluirNome("Excluir Cliente Tal");
    }

    @Test
    public void testIncluirCliente() {

        assertTrue(clie.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        Cliente cliente = clie.obterClienteNome("Incluir Cliente Tal");

        assertEquals("Incluir Cliente Tal", cliente.getNome());
        assertEquals("12345678912", cliente.getTelefone1());
        assertEquals("Rua Irlanda Creusa, 1754", cliente.getEndereco());

        assertFalse(clie.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(clie.excluirNome("Incluir Cliente Tal"));

    }

    @Test
    public void testAlterarCliente() {

        assertTrue(clie.incluirCliente("Alterar Cliente Tal", "12345678912", "", "",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(clie.alterarCliente(clie.obterClienteNome("Alterar Cliente Tal").getClieId(),
                "Alterar Cliente Tal para Isso", "87654987654", "123456789", "alterar@alterar.com",
                "Rua Irlanda Creusa, 8498"));

        Cliente cliente = clie.obterClienteNome("Alterar Cliente Tal para Isso");

        assertEquals("Alterar Cliente Tal para Isso", cliente.getNome());
        assertEquals("87654987654", cliente.getTelefone1());
        assertEquals("123456789", cliente.getTelefone2());
        assertEquals("alterar@alterar.com", cliente.getEmail());
        assertEquals("Rua Irlanda Creusa, 8498", cliente.getEndereco());

        assertTrue(clie.excluirNome("Alterar Cliente Tal para Isso"));

    }

    @Test
    public void testExcluirId() {

        clie.incluirCliente("Excluir Cliente Teste", "21654987789", "", "", "Rua Irlanda Creusa, 1754");

        assertTrue(clie.excluirNome("Excluir Cliente Teste"));

        assertEquals(null, clie.obterClienteNome("Excluir Cliente Teste"));
    }

}
