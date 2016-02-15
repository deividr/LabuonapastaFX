package labuonapastafx.test;

import labuonapastafx.controller.ClienteNe;
import labuonapastafx.controller.PedidoNe;
import labuonapastafx.model.Cliente;
import labuonapastafx.model.ItemPedido;
import labuonapastafx.model.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PedidoTest {

    private PedidoNe pedNe;
    private Usuario usuar;
    private Cliente clie;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        pedNe = new PedidoNe();
        usuar = 

        // Excluir das bases todos os clientes usados nos testes, para o caso de
        // algum existir ainda de testes anteriores.
        pedNe.excluir(pedNe.obterClienteNome("Incluir Cliente Tal").getClieId());
        pedNe.excluir(pedNe.obterClienteNome("Alterar Cliente Tal").getClieId());
        pedNe.excluir(pedNe.obterClienteNome("Alterar Cliente Tal para Isso").getClieId());
        pedNe.excluir(pedNe.obterClienteNome("Excluir Cliente Tal").getClieId());
    }

    @Test
    public void testIncluirPedido() {

        Usuario user = null;
        Cliente clie = null;
        ArrayList<ItemPedido> itens = new ArrayList<ItemPedido>();
        
        assertTrue(pedNe.incluir(user, clie, Date data, String horaDe, String horaAte, itens));

        Cliente cliente = pedNe.obterClienteNome("Incluir Cliente Tal");

        assertEquals("Incluir Cliente Tal", cliente.getNome());
        assertEquals("12345678912", cliente.getTelefone1());
        assertEquals("Rua Irlanda Creusa, 1754", cliente.getEndereco());

        assertFalse(pedNe.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(pedNe.excluir(cliente.getClieId()));

    }

    @Test
    public void testAlterarCliente() {

        assertTrue(pedNe.incluirCliente("Alterar Cliente Tal", "12345678912", "", "",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(pedNe.alterarCliente(pedNe.obterClienteNome("Alterar Cliente Tal").getClieId(),
                "Alterar Cliente Tal para Isso", "87654987654", "123456789", "alterar@alterar.com",
                "Rua Irlanda Creusa, 8498"));

        Cliente cliente = pedNe.obterClienteNome("Alterar Cliente Tal para Isso");

        assertEquals("Alterar Cliente Tal para Isso", cliente.getNome());
        assertEquals("87654987654", cliente.getTelefone1());
        assertEquals("123456789", cliente.getTelefone2());
        assertEquals("alterar@alterar.com", cliente.getEmail());
        assertEquals("Rua Irlanda Creusa, 8498", cliente.getEndereco());

        assertTrue(pedNe.excluir(cliente.getClieId()));

    }

    @Test
    public void testExcluirId() {

        pedNe.incluirCliente("Excluir Cliente Teste", "21654987789", "", "", "Rua Irlanda Creusa, 1754");

        Cliente clie = pedNe.obterClienteNome("Excluir Cliente Teste");

        assertTrue(pedNe.excluir(clie.getClieId()));

        assertEquals(null, pedNe.obterClienteNome("Excluir Cliente Teste"));
    }

}
