package labuonapastafx.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;
import labuonapastafx.persistencia.ProdutoDao;

public class ProdutoNe {

    private ProdutoDao produtoDao;
    
    public ProdutoNe() {
    	produtoDao = new ProdutoDao();
    }

    /**
     * Obter o {@code Produto} que corresponde ao nome informado.
     *
     * @param nome a qual se deseja pesquisar
     * @return retorna objeto {@code Produto} correspondente ao nome informado
     */
    public Produto obterProduto(String nome) {

        return produtoDao.ler(nome);

    }

    /**
     * Incluir um {@code Produto}.
     *
     * @param nome
     * @param unidade
     * @param valor
     * @param tipo
     * @return true se a inclusão ocorreu com sucesso, e false se ocorreu algum
     * erro (Ex.: Produto já existe).
     */
    public boolean incluirProduto(String nome, UnidadeEnum unidade, BigDecimal valor, ProdutoEnum tipo) {

        //Se o produto nao existir faca a inclusao
        if (obterProduto(nome) == null) {
            Produto produto = new Produto(0, nome, unidade, valor, tipo, (byte) 1);
            produtoDao.incluir(produto);
            return true;
        } else {
            //Se o produto jah existir o retorno serah false
            return false;
        }

    }

    /**
     * Atualizar as informações do {@code Produto} que foi passado como
     * parâmetro.
     *
     * @param nome
     * @param unidade
     * @param valor
     * @param tipo
     * @return true se a alteração ocorreu com sucesso, e false se ocorreu algum
     * erro (Ex.: Produto não existe).
     */
    public boolean alterarProduto(String nome, UnidadeEnum unidade, BigDecimal valor, ProdutoEnum tipo) {

        Produto prod = obterProduto(nome);

        // se o usuario existir atualiza, senão retorna false para o chamador
        if (prod != null) {
            prod.setNome(nome);
            prod.setUnidade(unidade);
            prod.setValor(valor);
            prod.setTipo(tipo);

            produtoDao.atualizar(prod);

            return true; //retorna que a atualizacao foi ok
        } else {
            return false;
        }
    }

    /**
     * Excluir o {@code Produto} logicamente, essa exclusão não irá eliminar o
     * {@code Produto} da tabela, apenas torna-lo inativo. Isso é útil para o
     * histórico do sistema.
     *
     * @param nome
     * @return
     */
    public boolean exclusaoLogica(String nome) {

        if (obterProduto(nome) != null) {
            produtoDao.exclusaoLogica(nome);
            return true;
        } else {
            //Produto não existe
            return false;
        }

    }

    /**
     * Excluir o {@code Produto} fisicamente das bases de dados. CUIDADO:Essa exclusão pode
     * compromenter as informações de históricos do sistema
     *
     * @param nome do {@code Produto} que se deseja excluir
     * @return True se exclusão foi ok, ou False se ocorreu algum erro
     */
    public boolean excluirProduto(String nome) {

        if (obterProduto(nome) != null) {
            produtoDao.excluir(nome);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Retorna uma lista dos produtos cadastrados na base de dados.
     *
     * @return
     */
    public ArrayList<Produto> listarProdutos() {

        return produtoDao.listar();

    }

    /**
     * Retorna uma lista das produtos que começam com a informação passa por
     * parâmetro.
     *
     * @param nome
     * @return
     */
    public ArrayList<Produto> listarProdutos(String nome) {

        return produtoDao.listar(nome);

    }

}
