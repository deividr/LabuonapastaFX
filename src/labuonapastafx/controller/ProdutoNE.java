package labuonapastafx.controller;

import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.persistencia.ProdutoDAO;

public class ProdutoNE {

	private ProdutoDAO produtoDAO;

	/**
	 * Obter o produto que corresponde ao nome informado
	 * @param nome a qual se deseja pesquisar
	 * @return retorna objeto produto correspondente ao nome informado
	 */
	public Produto obterProduto(String nome) {

		return getProdutoDAO().ler(nome);
		
	}
	
	public boolean incluirProduto(String nome, String unidade, double valor, ProdutoEnum tipo) {
		
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
	
	private ProdutoDAO getProdutoDAO() {
		
		if (produtoDAO == null)
			produtoDAO = new ProdutoDAO();
		
		return produtoDAO;
		
	}

}