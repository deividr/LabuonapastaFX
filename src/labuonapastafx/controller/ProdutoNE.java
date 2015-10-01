package labuonapastafx.controller;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;
import labuonapastafx.persistencia.ProdutoDAO;

public class ProdutoNE {

	private ProdutoDAO produtoDAO;

	/**
	 * Obter o produto que corresponde ao nome informado.
	 * 
	 * @param nome a qual se deseja pesquisar
	 * @return retorna objeto produto correspondente ao nome informado
	 */
	public Produto obterProduto(String nome) {

		return getProdutoDAO().ler(nome);
		
	}
	
	/**
	 * Incluir um usuário.
	 * 
	 * @param nome
	 * @param unidade
	 * @param valor
	 * @param tipo
	 * @return true se a inclusão ocorreu com sucesso, e false se ocorreu algum erro 
	 * (Ex.: Produto já existe).
	 */
	public boolean incluirProduto(String nome, UnidadeEnum unidade, double valor, ProdutoEnum tipo) {
		
		//Se o produto nao existir faca a inclusao
		if (obterProduto(nome) == null) {
			Produto produto = new Produto(0, nome, unidade, valor, tipo);
			getProdutoDAO().incluir(produto);
			return true;
		} else {
			//Se o produto jah existir o retorno serah false
			return false;
		}

	}

	/**
	 * Atualizar as informações do Produto que foi passado como parâmetro.
	 * 
	 * @param nome
	 * @param unidade
	 * @param valor
	 * @param tipo
	 * @return true se a alteração ocorreu com sucesso, e false se ocorreu algum erro 
	 * (Ex.: Produto não existe).
	 */
	public boolean alterarProduto(String nome, UnidadeEnum unidade, double valor, ProdutoEnum tipo) {

		Produto prod = obterProduto(nome);

        // se o usuario existir atualiza, senão retorna false para o chamador
        if (prod != null) {
            prod.setProduto(nome);
            prod.setUnidade(unidade);
            prod.setValor(valor);
            prod.setTipo(tipo);

            getProdutoDAO().atualizar(prod);

            return true; //retorna que a atualizacao foi ok
        } else {
            return false;
        }
	}

	/**
	 * Excluir o produto cujo nome seja igual ao informado com parametro
	 * 
	 * @param nome do produto que se deseja excluir
	 */
	public boolean excluirProduto(String nome) {
		
		if (obterProduto(nome) != null) {
			getProdutoDAO().excluir(nome);
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
     * Irá retornar um objeto da classe de persistência ProdutoDAO.
     * Esse método tem por objetivo evitar a criação de diversas instâncias dessa
     * classe que pode ocorrer durante o uso do sistema.
	 * 
	 * @return
	 */
	private ProdutoDAO getProdutoDAO() {
		
		if (produtoDAO == null)
			produtoDAO = new ProdutoDAO();
		
		return produtoDAO;
		
	}

}