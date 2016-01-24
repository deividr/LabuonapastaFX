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

    private ProdutoNe prodtNe;

    @Before
    public void setUp() throws Exception {
        // Criar o objeto do Negócio.
        prodtNe = new ProdutoNe();

        // Excluir das bases todos os produtos usados nos testes, para o caso de
        // algum existir ainda de testes anteriores.
        try {
            prodtNe.excluirProduto(prodtNe.obterProduto("Incluir Produto Tal").getProdId());
            prodtNe.excluirProduto(prodtNe.obterProduto("Alterar Produto Tal").getProdId());
            prodtNe.excluirProduto(prodtNe.obterProduto("Excluir Produto Tal").getProdId());
            prodtNe.excluirProduto(prodtNe.obterProduto("exclusaoLogica").getProdId());
        } catch (NullPointerException ne) {

        }
    }

    @Test
    public void testIncluirProduto() {

        assertTrue(prodtNe.incluirProduto("Incluir Produto Tal", UnidadeEnum.UNIDADE, BigDecimal.valueOf(123.45), ProdutoEnum.MASSA));

        Produto produto = prodtNe.obterProduto("Incluir Produto Tal");

        assertEquals("Incluir Produto Tal", produto.getNome());
        assertEquals(UnidadeEnum.UNIDADE, produto.getUnidade());
        assertEquals(123.45, produto.getValor().doubleValue(), 0);
        assertEquals(ProdutoEnum.MASSA, produto.getTipo());

        assertFalse(prodtNe.incluirProduto("Incluir Produto Tal", UnidadeEnum.UNIDADE, BigDecimal.valueOf(123.45), ProdutoEnum.MOLHO));

        prodtNe.excluirProduto(produto.getProdId());

    }

    @Test
    public void testAlterarProduto() {

        prodtNe.incluirProduto("Alterar Produto Tal", UnidadeEnum.KILOGRAMA, BigDecimal.valueOf(123.45), ProdutoEnum.MASSA);

        Produto produto = prodtNe.obterProduto("Alterar Produto Tal");

        assertTrue(prodtNe.alterarProduto(produto.getProdId(), "Alterar Produto Tal",
                UnidadeEnum.LITROS, BigDecimal.valueOf(678.90), ProdutoEnum.MOLHO));

        produto = prodtNe.obterProduto("Alterar Produto Tal");

        assertEquals(UnidadeEnum.LITROS, produto.getUnidade());
        assertEquals(678.90, produto.getValor().doubleValue(), 0);
        assertEquals(ProdutoEnum.MOLHO, produto.getTipo());

        prodtNe.excluirProduto(produto.getProdId());

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

        prodtNe.incluirProduto("Excluir Produto Tal", UnidadeEnum.LITROS, BigDecimal.valueOf(106.26), ProdutoEnum.SALADA);

        assertTrue(prodtNe.excluirProduto(prodtNe.obterProduto("Excluir Produto Tal").getProdId()));

        assertEquals(null, prodtNe.obterProduto("Excluir Produto Tal"));
    }

}
