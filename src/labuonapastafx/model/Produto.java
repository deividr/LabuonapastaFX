package labuonapastafx.model;

import java.io.Serializable;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigo;
	private String nome, unidade;
	private double valor;
	private ProdutoEnum tipo;

	public Produto() {
		setCodigo(0);
		setNome("");
		setUnidade("");
		setValor(0);
		setTipo(ProdutoEnum.MASSA);
	}
	
	public Produto(int codigo, String nome, String unidade, double valor, ProdutoEnum tipo) {
		setCodigo(codigo);
		setNome(nome);
		setUnidade(unidade);
		setValor(valor);
		setTipo(tipo);
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public ProdutoEnum getTipo() {
		return tipo;
	}

	public void setTipo(ProdutoEnum tipo) {
		this.tipo = tipo;
	}
	
	public String toString() {
		return "O produto " + getNome() + "eh cobrado por " + getUnidade() +
				". E o seu valor eh " + getValor() + ".";
	}
	
	@Override
	public boolean equals(Object o) {
		//se o objeto recebido for diferente de null e a sua classe for um Usuario verifica
		//igualdade no codigo do usuario.
		if (o != null && o.getClass() == this.getClass()) {
			Produto prod = (Produto) o;
			if (prod.getCodigo() == this.codigo) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return getNome().hashCode();
	}

}
