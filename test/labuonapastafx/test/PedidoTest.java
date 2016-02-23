package labuonapastafx.test;

import labuonapastafx.controller.ClienteNe;
import labuonapastafx.controller.PedidoNe;
import labuonapastafx.controller.ProdutoNe;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class PedidoTest {

    private PedidoNe pedNe;
    private UsuarioNe usuarNe;
    private ClienteNe clieNe;
    private ProdutoNe prodNe;
    private Usuario usuar;
    private Cliente clie;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        pedNe = new PedidoNe();
        usuarNe = new UsuarioNe();
        clieNe = new ClienteNe();
        prodNe = new ProdutoNe();

        clieNe.incluirCliente("Teste Pedido", "11369874565", "", "", "");
        clie = clieNe.obterClienteNome("Teste Pedido");

        usuarNe.incluirUsuario("testeped", "Teste Pedido", AcessoEnum.ADMINISTRADOR, "teste");
        usuar = usuarNe.obterUsuario("testeped");

        //Incluir produtos que serão utilizados nos testes.
        prodNe.incluirProduto("Produto Massa", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00), ProdutoEnum.MASSA);
        prodNe.incluirProduto("Produto Molho", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00), ProdutoEnum.MOLHO);

        prodNe.incluirProduto("Produto Bebida", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.BEBIDA);

        prodNe.incluirProduto("Produto Carne", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00), ProdutoEnum.CARNE);

        prodNe.incluirProduto("Produto Salada", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.SALADA);

        prodNe.incluirProduto("Produto Diversos", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.DIVERSOS);

    }

    @After
    public void setEnd() throws  Exception {

        //Excluir massa de testes utilizada.
        usuarNe.excluirUsuario(usuarNe.obterUsuario("testeped").getUserId());

        clieNe.excluir(clieNe.obterClienteNome("Teste Pedido").getClieId());

        prodNe.excluirProduto(prodNe.obterProduto("Produto Massa").getProdId());
        prodNe.excluirProduto(prodNe.obterProduto("Produto Molho").getProdId());
        prodNe.excluirProduto(prodNe.obterProduto("Produto Bebida").getProdId());
        prodNe.excluirProduto(prodNe.obterProduto("Produto Carne").getProdId());
        prodNe.excluirProduto(prodNe.obterProduto("Produto Salada").getProdId());
        prodNe.excluirProduto(prodNe.obterProduto("Produto Diversos").getProdId());

    }

    @Test
    public void testIncluirPedido() {

        ArrayList<ItemPedido> itens = new ArrayList<ItemPedido>();

        itens.add(new ItemPedido(prodNe.obterProduto("Produto Massa"), BigDecimal.valueOf(1.300),
                prodNe.obterProduto("Produto Molho")));

        itens.add(new ItemPedido(prodNe.obterProduto("Produto Massa"), BigDecimal.valueOf(2.000),
                prodNe.obterProduto("Produto Molho")));

        itens.add(new ItemPedido(prodNe.obterProduto("Produto Salada"), BigDecimal.valueOf(0.750), null));

        itens.add(new ItemPedido(prodNe.obterProduto("Produto Bebida"), BigDecimal.valueOf(1.000), null));

        Calendar c = Calendar.getInstance();
        c.set(2016, 02, 22);

        assertTrue(pedNe.incluir(usuar, clie, c.getTime(), "0900", "1000", itens));

        Cliente cliente = pedNe.obterClienteNome("Incluir Cliente Tal");

        assertEquals("Incluir Cliente Tal", cliente.getNome());
        assertEquals("12345678912", cliente.getTelefone1());
        assertEquals("Rua Irlanda Creusa, 1754", cliente.getEndereco());

        assertFalse(pedNe.incluirCliente("Incluir Cliente Tal", "12345678912", "", "incluir@incluir.com",
                "Rua Irlanda Creusa, 1754"));

        assertTrue(pedNe.excluir(cliente.getClieId()));

    }

    /*
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
    */

}
