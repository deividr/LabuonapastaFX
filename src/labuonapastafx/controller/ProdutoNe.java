package labuonapastafx.controller;

import java.math.BigDecimal;
import java.util.List;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;
import labuonapastafx.persistencia.ProdutoDao;

public class ProdutoNe {

    private final ProdutoDao produtoDao;

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
     * Obter o {@code Produto} que corresponde ao código informado.
     *
     * @param cdProduto Código do produto a qual se deseja pesquisar
     * @return retorna objeto {@code Produto} correspondente ao código informado.
     */
    public Produto obterCodProduto(int cdProduto) {

        return produtoDao.lerCodProduto(cdProduto);

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
     * Atualizar as informações do {@code Produto} que foi passado como parâmetro.
     *
     * @param cdProduto
     * @param nome
     * @param unidade
     * @param valor
     * @param tipo
     * @return true se a alteração ocorreu com sucesso, e false se ocorreu algum
     * erro (Ex.: Produto não existe).
     */
    public boolean alterarProduto(int cdProduto, String nome, UnidadeEnum unidade,
                                  BigDecimal valor, ProdutoEnum tipo) {

        Produto prod = obterCodProduto(cdProduto);
        // Se o usuario existir atualiza, senao retorna false para o chamador
        if (prod != null) {

            // Se o login foi alterado, verificar se não pertence a outro Usuário.
            if (!prod.getNome().equals(nome)) {

                Produto prod2 = obterProduto(nome);

                if (prod2 != null)
                    // Se o login do Usuário alterado pertence a outro Usuário, invalida a alteração.
                    if (prod.getProdId() != prod2.getProdId())
                        return false;
            }
            prod.setNome(nome);
            prod.setUnidade(unidade);
            prod.setValor(valor);
            prod.setTipo(tipo);

            produtoDao.atualizar(prod);

            //retorna que a atualização foi ok
            return true;
        } else {
            return false;
        }
    }

    /**
     * Excluir o {@code Produto} logicamente, essa exclusão não irá eliminar o
     * {@code Produto} da tabela, apenas torna-lo inativo. Isso é útil para o
     * histórico do sistema.
     *
     * @param cdProduto
     * @return
     */
    public boolean exclusaoLogica(int cdProduto) {

        if (obterCodProduto(cdProduto) != null) {
            produtoDao.exclusaoLogica(cdProduto);
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
     * @param cdProduto Código do {@code Produto} que se deseja excluir
     * @return True se exclusão foi ok, ou False se ocorreu algum erro
     */
    public boolean excluirProduto(int cdProduto) {

        if (obterCodProduto(cdProduto) != null) {
            produtoDao.excluir(cdProduto);
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
    public List<Produto> listarProdutos() {

        return produtoDao.listar();

    }

    /**
     * Retorna uma lista das produtos que começam com a informação passa por
     * parâmetro.
     *
     * @param nome
     * @return
     */
    public List<Produto> listarProdutos(String nome) {

        return produtoDao.listar(nome);

    }

}
