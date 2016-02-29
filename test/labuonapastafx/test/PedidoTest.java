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
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PedidoTest {

    private PedidoNe pedNe;
    private UsuarioNe usuarNe;
    private ClienteNe clieNe;
    private ProdutoNe prodNe;
    private Usuario usuar;
    private Cliente clie;
    private ArrayList<Pedido> pedidos;
    private ArrayList<ItemPedido> itens;

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

        //Gerar uma lista de itens para utilizar nos testes:
        itens = new ArrayList<ItemPedido>();

        itens.add(new ItemPedido(1, prodNe.obterProduto("Produto Massa"),
                prodNe.obterProduto("Produto Molho"), BigDecimal.valueOf(1.300)));

        itens.add(new ItemPedido(2, prodNe.obterProduto("Produto Massa"),
                prodNe.obterProduto("Produto Molho"), BigDecimal.valueOf(2.000)));

        itens.add(new ItemPedido(3, prodNe.obterProduto("Produto Salada"),
                new Produto(), BigDecimal.valueOf(0.750)));

        itens.add(new ItemPedido(4, prodNe.obterProduto("Produto Bebida"),
                new Produto(), BigDecimal.valueOf(1.000)));

    }

    @After
    public void setEnd() throws Exception {

        pedNe.excluirPorCliente(clie);

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

        LocalDate dtRetirada = LocalDate.now().plusDays(2);

        assertTrue(pedNe.incluir(usuar, clie, dtRetirada, 900, 1000, itens));

        pedidos = pedNe.obterPedidoCliente(clie.getClieId());

        assertEquals("Teste Pedido", pedidos.get(0).getUsuar().getNomeCompleto());

        assertEquals("Teste Pedido", pedidos.get(0).getClie().getNome());

        assertEquals(LocalDate.now(), pedidos.get(0).getDtPedido());

        assertEquals(dtRetirada, pedidos.get(0).getDtRetirada());

        assertEquals(900, pedidos.get(0).getHoraDe());

        assertEquals(1000, pedidos.get(0).getHoraAte());

        assertEquals(4, pedidos.get(0).getItens().size());

        assertEquals(itens.get(0).getProduto().getProdId(), pedidos.get(0).getItens().get(0).getProduto().getProdId());

        assertEquals(itens.get(0).getMolho().getProdId(), pedidos.get(0).getItens().get(0).getMolho().getProdId());

        assertEquals(itens.get(0).getProduto().getValor(), pedidos.get(0).getItens().get(0).getProduto().getValor());

        assertEquals(itens.get(1).getProduto().getProdId(), pedidos.get(0).getItens().get(1).getProduto().getProdId());

        assertEquals(itens.get(1).getMolho().getProdId(), pedidos.get(0).getItens().get(1).getMolho().getProdId());

        assertEquals(itens.get(1).getProduto().getValor(), pedidos.get(0).getItens().get(1).getProduto().getValor());

        assertEquals(itens.get(2).getProduto().getProdId(), pedidos.get(0).getItens().get(2).getProduto().getProdId());

        assertEquals(itens.get(2).getMolho().getProdId(), pedidos.get(0).getItens().get(2).getMolho().getProdId());

        assertEquals(itens.get(2).getProduto().getValor(), pedidos.get(0).getItens().get(2).getProduto().getValor());

        assertEquals(itens.get(3).getProduto().getProdId(), pedidos.get(0).getItens().get(3).getProduto().getProdId());

        assertEquals(itens.get(3).getMolho().getProdId(), pedidos.get(0).getItens().get(3).getMolho().getProdId());

        assertEquals(itens.get(3).getProduto().getValor(), pedidos.get(0).getItens().get(3).getProduto().getValor());

    }

    @Test
    public void testAlterarPedido() {

        testIncluirPedido();

        Pedido ped = pedidos.get(0);

        ped.setDtRetirada(LocalDate.now().plusDays(4));
        ped.setHoraDe(1100);
        ped.setHoraAte(1200);

        ped.getItens().remove(0);
        ped.getItens().remove(2);

        ped.getItens().get(0).setCodigo(1);
        ped.getItens().get(1).setCodigo(2);

        assertTrue(pedNe.alterar(pedidos.get(0)));


    }
    /*
     @Test
     public void testExcluirId() {

     pedNe.incluirCliente("Excluir Cliente Teste", "21654987789", "", "", "Rua Irlanda Creusa, 1754");

     Cliente clie = pedNe.obterClienteNome("Excluir Cliente Teste");

     assertTrue(pedNe.excluir(clie.getClieId()));

     assertEquals(null, pedNe.obterClienteNome("Excluir Cliente Teste"));
     }
     */
}
