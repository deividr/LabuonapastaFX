package labuonapastafx.test;

import static org.junit.Assert.*;
import labuonapastafx.controller.ProdutoNE;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;

import org.junit.Before;
import org.junit.Test;

public class ProdutoTest {

    private ProdutoNE prodt;

    @Before
    public void setUp() throws Exception {
        prodt = new ProdutoNE();
    }

    @Test
    public void testIncluirProduto() {

        assertTrue(prodt.incluirProduto("Incluir Produto Tal", "UN", 123.45, ProdutoEnum.MASSA));

        Produto produto = prodt.obterProduto("Incluir Produto Tal");

        assertEquals("Incluir Produto Tal", produto.getNome());
        assertEquals("UN", produto.getUnidade());
        assertEquals(123.45, produto.getValor(), 0);
        assertEquals(ProdutoEnum.MASSA, produto.getTipo());

        assertFalse(prodt.incluirProduto("Incluir Produto Tal", "UN", 123.45, ProdutoEnum.MOLHO));

        prodt.excluirProduto("Incluir Produto Tal");

    }

    /*
     @Test
     public void testAlterarProduto() {

     prodt.incluirProduto("Alterar Produto Tal", "UN", 123,45, ProdutoEnum.MASSA);
		
     assertTrue(prodt.alterarProduto("Alterar Produto Tal como foi incluido", "KG", 678,90, ProdutoEnum.MOLHO));
		
     Produto produto = prodt.obterProduto("Alterar Produto Tal");
		
     assertEquals("Alterar Produto Tal como foi incluido", produto.getNome());
     assertEquals("KG", produto.getUnidade());
     assertEquals(678,90, produto.getValor());
     assertEquals(ProdutoEnum.MOLHO, produto.getTipoProduto());
		
     prodt.excluirProduto("Alterar Produto Tal");
		
     }

     @Test
     public void testExcluirProduto() {

     prodt.incluirProduto("Excluir Produto Tal conforme alteracao", "UN", 123,45, ProdutoEnum.MASSA);
		
     assertTrue(prodt.excluirProduto("Excluir Produto Tal"));
		
     assertEquals(null, prodt.obterProduto("Excluir Produto Tal"));
     }
     */
}
