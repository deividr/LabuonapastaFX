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
        prodt.excluirProduto("Incluir Produto Tal");
        prodt.excluirProduto("Alterar Produto Tal");
        prodt.excluirProduto("Excluir Produto Tal");
        prodt.excluirProduto("exclusaoLogica");
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

        prodt.excluirProduto("Incluir Produto Tal");

    }

    @Test
    public void testAlterarProduto() {

        prodt.incluirProduto("Alterar Produto Tal", UnidadeEnum.KILOGRAMA, BigDecimal.valueOf(123.45), ProdutoEnum.MASSA);

        assertTrue(prodt.alterarProduto("Alterar Produto Tal", UnidadeEnum.LITROS, BigDecimal.valueOf(678.90), ProdutoEnum.MOLHO));

        Produto produto = prodt.obterProduto("Alterar Produto Tal");

        assertEquals(UnidadeEnum.LITROS, produto.getUnidade());
        assertEquals(678.90, produto.getValor().doubleValue(), 0);
        assertEquals(ProdutoEnum.MOLHO, produto.getTipo());

        prodt.excluirProduto("Alterar Produto Tal");

    }

    @Test
    public void testExcluirLogicamente() {
        ProdutoNe prod = new ProdutoNe();

        prod.incluirProduto("exclusaoLogica", UnidadeEnum.KILOGRAMA, BigDecimal.valueOf(0.0), ProdutoEnum.DIVERSOS);

        //verificar se o Produto foi incluido como ativo
        assertTrue(prod.obterProduto("exclusaoLogica").isAtivo());

        //efetuar a exclusao logica, atualizando o Produto para inativo
        assertTrue(prod.exclusaoLogica("exclusaoLogica"));

        //verificar se o Produto estah inativo
        assertFalse(prod.obterProduto("exclusaoLogica").isAtivo());

        //tentar excluir logicamente um Produto que nao existe
        assertFalse(prod.excluirProduto("exclusaoIlogica"));

        prod.excluirProduto("exclusaoLogica");

    }

    @Test
    public void testExcluirProduto() {

        prodt.incluirProduto("Excluir Produto Tal", UnidadeEnum.LITROS, BigDecimal.valueOf(106.26), ProdutoEnum.SALADA);

        assertTrue(prodt.excluirProduto("Excluir Produto Tal"));

        assertEquals(null, prodt.obterProduto("Excluir Produto Tal"));
    }

}
