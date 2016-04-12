package labuonapastafx.test;

import labuonapastafx.controller.ClienteNe;
import labuonapastafx.controller.PedidoNe;
import labuonapastafx.controller.ProdutoNe;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;

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
    private Pedido pedido;
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

        // Incluir produtos que serão utilizados nos testes.
        prodNe.incluirProduto("Produto Massa", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.MASSA);
        prodNe.incluirProduto("Produto Molho", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.MOLHO);

        prodNe.incluirProduto("Produto Bebida", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.BEBIDA);

        prodNe.incluirProduto("Produto Carne", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.CARNE);

        prodNe.incluirProduto("Produto Salada", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.SALADA);

        prodNe.incluirProduto("Produto Diversos", UnidadeEnum.UNIDADE, BigDecimal.valueOf(10.00),
                ProdutoEnum.DIVERSOS);

        // Gerar uma lista de itens para utilizar nos testes:
        itens = new ArrayList<>();

        itens.add(new ItemPedido(1, prodNe.obterProduto("Produto Massa"),
                prodNe.obterProduto("Produto Molho"), BigDecimal.valueOf(1.300)));

        itens.add(new ItemPedido(2, prodNe.obterProduto("Produto Massa"),
                prodNe.obterProduto("Produto Molho"), BigDecimal.valueOf(2.000)));

        itens.add(new ItemPedido(3, prodNe.obterProduto("Produto Salada"), new Produto(),
                BigDecimal.valueOf(0.750)));

        itens.add(new ItemPedido(4, prodNe.obterProduto("Produto Bebida"), new Produto(),
                BigDecimal.valueOf(1.000)));

    }

    @After
    public void setEnd() throws Exception {

        pedNe.excluirPorCliente(clie);

        // Excluir massa de testes utilizada.
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

        pedido = pedNe.incluir(usuar, clie, dtRetirada, "09:00", "10:00", "010", itens,
                "Algumas observacoes quaisquer", (byte) 0);

        assertEquals("Teste Pedido", pedido.getUsuario().getNomeCompleto());

        assertEquals("Teste Pedido", pedido.getClie().getNome());

        assertEquals(LocalDate.now(), pedido.getDtPedido());

        assertEquals(dtRetirada, pedido.getDtRetirada());

        assertEquals("09:00", pedido.getHoraDe());

        assertEquals("10:00", pedido.getHoraAte());

        assertEquals("010", pedido.getGeladeira());

        assertEquals("Algumas observacoes quaisquer", pedido.getObsercao());

        assertFalse(pedido.isRetirado());

        assertEquals(4, pedido.getItens().size());

        assertEquals(itens.get(0).getProduto().getProdId(),
                pedido.getItens().get(0).getProduto().getProdId());

        assertEquals(itens.get(0).getMolho().getProdId(),
                pedido.getItens().get(0).getMolho().getProdId());

        assertEquals(itens.get(0).getProduto().getValor(),
                pedido.getItens().get(0).getProduto().getValor());

        assertEquals(itens.get(1).getProduto().getProdId(),
                pedido.getItens().get(1).getProduto().getProdId());

        assertEquals(itens.get(1).getMolho().getProdId(),
                pedido.getItens().get(1).getMolho().getProdId());

        assertEquals(itens.get(1).getProduto().getValor(),
                pedido.getItens().get(1).getProduto().getValor());

        assertEquals(itens.get(2).getProduto().getProdId(),
                pedido.getItens().get(2).getProduto().getProdId());

        assertEquals(itens.get(2).getMolho().getProdId(),
                pedido.getItens().get(2).getMolho().getProdId());

        assertEquals(itens.get(2).getProduto().getValor(),
                pedido.getItens().get(2).getProduto().getValor());

        assertEquals(itens.get(3).getProduto().getProdId(),
                pedido.getItens().get(3).getProduto().getProdId());

        assertEquals(itens.get(3).getMolho().getProdId(),
                pedido.getItens().get(3).getMolho().getProdId());

        assertEquals(itens.get(3).getProduto().getValor(),
                pedido.getItens().get(3).getProduto().getValor());

    }

    @Test
    public void testAlterarPedido() {

        testIncluirPedido();

        Pedido ped = pedido;

        ped.setUsuar(usuarNe.obterUsuario("deivid"));

        ped.setDtRetirada(LocalDate.now().plusDays(4));
        ped.setHoraDe("11:00");
        ped.setHoraAte("12:00");
        ped.setGeladeira("020");

        itens.remove(0);
        itens.remove(2);

        itens.get(0).setCodigo(1);
        itens.get(1).setCodigo(2);

        ped.setItens(FXCollections.observableArrayList(itens));

        assertTrue(pedNe.alterar(ped));

        pedido = pedNe.obterPedidos(clie).get(0);

        assertEquals("Deivid", pedido.getUsuario().getNomeCompleto());

        assertEquals("Teste Pedido", pedido.getClie().getNome());

        assertEquals(LocalDate.now(), pedido.getDtPedido());

        assertEquals(LocalDate.now().plusDays(4), pedido.getDtRetirada());

        assertEquals("11:00", pedido.getHoraDe());

        assertEquals("12:00", pedido.getHoraAte());

        assertEquals(2, pedido.getItens().size());

        assertEquals(itens.get(0).getCodigo(), pedido.getItens().get(0).getCodigo());

        assertEquals(itens.get(0).getProduto().getProdId(),
                pedido.getItens().get(0).getProduto().getProdId());

        assertEquals(itens.get(0).getMolho().getProdId(),
                pedido.getItens().get(0).getMolho().getProdId());

        assertEquals(itens.get(0).getProduto().getValor(),
                pedido.getItens().get(0).getProduto().getValor());

        assertEquals(itens.get(1).getCodigo(), pedido.getItens().get(1).getCodigo());

        assertEquals(itens.get(1).getProduto().getProdId(),
                pedido.getItens().get(1).getProduto().getProdId());

        assertEquals(itens.get(1).getMolho().getProdId(),
                pedido.getItens().get(1).getMolho().getProdId());

        assertEquals(itens.get(1).getProduto().getValor(),
                pedido.getItens().get(1).getProduto().getValor());

    }

    @Test
    public void testExcluirPedidoPorCliente() {

        testIncluirPedido();

        pedNe.excluirPorCliente(clie);

        assertEquals(0, pedNe.obterPedidos(clie).size());

    }

    @Test
    public void testExcluirPedido() {

        testIncluirPedido();

        pedNe.excluirPedido(pedido);

        assertEquals(0, pedNe.obterPedidos(clie).size());

    }

}
