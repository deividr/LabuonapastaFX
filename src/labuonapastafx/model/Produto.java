package labuonapastafx.model;

import java.io.Serializable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IntegerProperty prodId;
	private StringProperty produto;
	private ObjectProperty<UnidadeEnum> unidade;
	private DoubleProperty valor;
	private ObjectProperty<ProdutoEnum> tipo;

	public Produto() {
		this(0, "", UnidadeEnum.UNIDADE, 0.0, ProdutoEnum.DIVERSOS);
	}
	
	public Produto(int prodID, String nome, UnidadeEnum unidade, double valor, ProdutoEnum tipo) {
		this.prodId = new SimpleIntegerProperty(prodID);
		this.produto = new SimpleStringProperty(nome);
		this.unidade = new SimpleObjectProperty<UnidadeEnum>(unidade);
		this.valor = new SimpleDoubleProperty(valor);
		this.tipo = new SimpleObjectProperty<ProdutoEnum>(tipo);
	}	

	public final IntegerProperty prodIDProperty() {
		return this.prodId;
	}
	

	public final int getProdID() {
		return this.prodIDProperty().get();
	}
	

	public final void setProdID(final int prodID) {
		this.prodIDProperty().set(prodID);
	}

	public final StringProperty produtoProperty() {
		return this.produto;
	}
	

	public final String getProduto() {
		return this.produtoProperty().get();
	}
	

	public final void setProduto(final String produto) {
		this.produtoProperty().set(produto);
	}
	

	public final ObjectProperty<ProdutoEnum> tipoProperty() {
		return this.tipo;
	}
	

	public final ProdutoEnum getTipo() {
		return this.tipoProperty().get();
	}
	

	public final void setTipo(final ProdutoEnum tipo) {
		this.tipoProperty().set(tipo);
	}
	

	public final ObjectProperty<UnidadeEnum> unidadeProperty() {
		return this.unidade;
	}
	

	public final UnidadeEnum getUnidade() {
		return this.unidadeProperty().get();
	}
	

	public final void setUnidade(final UnidadeEnum unidade) {
		this.unidadeProperty().set(unidade);
	}
	

	public final DoubleProperty valorProperty() {
		return this.valor;
	}
	

	public final double getValor() {
		return this.valorProperty().get();
	}
	

	public final void setValor(final double valor) {
		this.valorProperty().set(valor);
	}


	@Override
	public String toString() {
		return "O produto " + getProduto() + "eh cobrado por " + getUnidade() +
				". E o seu valor eh " + getValor() + ".";
	}
	
	@Override
	public boolean equals(Object o) {
		//se o objeto recebido for diferente de null e a sua classe for um Produto verifica
		//igualdade no codigo do usuario.
		if (o != null && o.getClass() == this.getClass()) {
			Produto prod = (Produto) o;
			if (prod.getProdID() == this.getProdID()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return getProduto().hashCode();
	}

}
