package labuonapastafx.test;

import static org.junit.Assert.*;

import labuonapastafx.controller.ProdutoNe;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ProdutoTest {

    private ProdutoNe prodt;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        prodt = new ProdutoNe();

        // Excluir das bases todos os produtos usados nos testes, para o caso de
        // algum existir ainda de testes anteriores.
        try {
            prodt.excluirProduto(prodt.obterProduto("Incluir Produto Tal").getProdId());
            prodt.excluirProduto(prodt.obterProduto("Alterar Produto Tal").getProdId());
            prodt.excluirProduto(prodt.obterProduto("Excluir Produto Tal").getProdId());
            prodt.excluirProduto(prodt.obterProduto("exclusaoLogica").getProdId());
        } catch (NullPointerException ne) {

        }
    }

    @Test
    public void testIncluirProduto() {

        assertTrue(prodt.incluirProduto("Incluir Produto Tal", UnidadeEnum.UNIDADE, BigDecimal.valueOf(123.45), ProdutoEnum.MASSA));

        Produto produto = prodt.obterProduto("Incluir Produto Tal");

        assertEquals("Incluir Produto Tal", produto.getNome());
        assertEquals(UnidadeEnum.UNIDADE, produto.getUnidade());
        assertEquals(123.45, produto.getValor().doubleValue(), 0);
        assertEquals(ProdutoEnum.MASSA, produto.getTipo());

        assertFalse(prodt.incluirProduto("Incluir Produto Tal", UnidadeEnum.UNIDADE, BigDecimal.valueOf(123.45), ProdutoEnum.MOLHO));

        prodt.excluirProduto(produto.getProdId());

    }

    @Test
    public void testAlterarProduto() {

        prodt.incluirProduto("Alterar Produto Tal", UnidadeEnum.KILOGRAMA, BigDecimal.valueOf(123.45), ProdutoEnum.MASSA);

        Produto produto = prodt.obterProduto("Alterar Produto Tal");

        assertTrue(prodt.alterarProduto(produto.getProdId(), "Alterar Produto Tal",
                UnidadeEnum.LITROS, BigDecimal.valueOf(678.90), ProdutoEnum.MOLHO));

        produto = prodt.obterProduto("Alterar Produto Tal");

        assertEquals(UnidadeEnum.LITROS, produto.getUnidade());
        assertEquals(678.90, produto.getValor().doubleValue(), 0);
        assertEquals(ProdutoEnum.MOLHO, produto.getTipo());

        prodt.excluirProduto(produto.getProdId());

    }

    @Test
    public void testExcluirLogicamente() {
        ProdutoNe prod = new ProdutoNe();

        prod.incluirProduto("exclusaoLogica", UnidadeEnum.KILOGRAMA, BigDecimal.valueOf(0.0), ProdutoEnum.DIVERSOS);

        Produto produto = prod.obterProduto("exclusaoLogica");

        //verificar se o Produto foi incluido como ativo
        assertTrue(produto.isAtivo());

        //efetuar a exclusao logica, atualizando o Produto para inativo
        assertTrue(prod.exclusaoLogica(produto.getProdId()));

        produto = prod.obterProduto("exclusaoLogica");

        //verificar se o Produto estah inativo
        assertFalse(produto.isAtivo());

        //tentar excluir logicamente um Produto que já está excluído.
        assertTrue(prod.excluirProduto(produto.getProdId()));

    }

    @Test
    public void testExcluirProduto() {

        prodt.incluirProduto("Excluir Produto Tal", UnidadeEnum.LITROS, BigDecimal.valueOf(106.26), ProdutoEnum.SALADA);

        assertTrue(prodt.excluirProduto(prodt.obterProduto("Excluir Produto Tal").getProdId()));

        assertEquals(null, prodt.obterProduto("Excluir Produto Tal"));
    }

}
