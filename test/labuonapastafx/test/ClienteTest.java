package labuonapastafx.test;

import static org.junit.Assert.*;
import labuonapastafx.controller.ClienteNe;
import labuonapastafx.model.Cliente;
import org.junit.Before;
import org.junit.Test;

public class ClienteTest {

    private ClienteNe clieNe;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        clieNe = new ClienteNe();

        // Excluir das bases todos os clientes usados nos testes, para o caso de
        // algum existir ainda de testes anteriores.
        clieNe.excluir(clieNe.obterClienteNome("Incluir Cliente Tal").getClieId());
        clieNe.excluir(clieNe.obterClienteNome("Alterar Cliente Tal").getClieId());
        clieNe.excluir(clieNe.obterClienteNome("Alterar Cliente Tal para Isso").getClieId());
        clieNe.excluir(clieNe.obterClienteNome("Excluir Cliente Tal").getClieId());
    }

    @Test
    public void testIncluirCliente() {

        assertTrue(clieNe.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        Cliente cliente = clieNe.obterClienteNome("Incluir Cliente Tal");

        assertEquals("Incluir Cliente Tal", cliente.getNome());
        assertEquals("12345678912", cliente.getTelefone1());
        assertEquals("Rua Irlanda Creusa, 1754", cliente.getEndereco());

        assertFalse(clieNe.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(clieNe.excluir(cliente.getClieId()));

    }

    @Test
    public void testAlterarCliente() {

        assertTrue(clieNe.incluirCliente("Alterar Cliente Tal", "12345678912", "", "",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(clieNe.alterarCliente(clieNe.obterClienteNome("Alterar Cliente Tal").getClieId(),
                "Alterar Cliente Tal para Isso", "87654987654", "123456789", "alterar@alterar.com",
                "Rua Irlanda Creusa, 8498"));

        Cliente cliente = clieNe.obterClienteNome("Alterar Cliente Tal para Isso");

        assertEquals("Alterar Cliente Tal para Isso", cliente.getNome());
        assertEquals("87654987654", cliente.getTelefone1());
        assertEquals("123456789", cliente.getTelefone2());
        assertEquals("alterar@alterar.com", cliente.getEmail());
        assertEquals("Rua Irlanda Creusa, 8498", cliente.getEndereco());

        assertTrue(clieNe.excluir(cliente.getClieId()));

    }

    @Test
    public void testExcluirId() {

        clieNe.incluirCliente("Excluir Cliente Teste", "21654987789", "", "", "Rua Irlanda Creusa, 1754");

        Cliente clie = clieNe.obterClienteNome("Excluir Cliente Teste");

        assertTrue(clieNe.excluir(clie.getClieId()));

        assertEquals(null, clieNe.obterClienteNome("Excluir Cliente Teste"));
    }

}
