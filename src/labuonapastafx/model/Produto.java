package labuonapastafx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private IntegerProperty prodId;
	private StringProperty nome;
	private ObjectProperty<UnidadeEnum> unidade;
	private ObjectProperty<BigDecimal> valor;
	private ObjectProperty<ProdutoEnum> tipo;
	private ObjectProperty<Byte> ativo;

	public Produto() {
		this(0, "", UnidadeEnum.UNIDADE, BigDecimal.valueOf(0.00), ProdutoEnum.DIVERSOS, (byte) 1);
	}

	public Produto(int prodId, String nome, UnidadeEnum unidade, BigDecimal valor, ProdutoEnum tipo,
			Byte ativo) {
		this.prodId = new SimpleIntegerProperty(prodId);
		this.nome = new SimpleStringProperty(nome);
		this.unidade = new SimpleObjectProperty<UnidadeEnum>(unidade);
		this.valor = new SimpleObjectProperty<BigDecimal>(valor);
		this.tipo = new SimpleObjectProperty<ProdutoEnum>(tipo);
		this.ativo = new SimpleObjectProperty<Byte>(ativo);
	}

	public final IntegerProperty prodIdProperty() {
		return this.prodId;
	}

	public final int getProdId() {
		return this.prodIdProperty().get();
	}

	public final void setProdId(final int prodID) {
		this.prodIdProperty().set(prodID);
	}

	public final StringProperty nomeProperty() {
		return this.nome;
	}

	public final String getNome() {
		return this.nomeProperty().get();
	}

	public final void setNome(final String nome) {
		this.nomeProperty().set(nome);
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

	public final ObjectProperty<BigDecimal> valorProperty() {
		return this.valor;
	}

	public final BigDecimal getValor() {
		return this.valorProperty().get();
	}

	public final void setValor(final BigDecimal valor) {
		this.valorProperty().set(valor);
	}

	public final ObjectProperty<Byte> ativoProperty() {
		return this.ativo;
	}

	public final byte getAtivo() {
		return this.ativoProperty().get();
	}

	public final void setAtivo(final Byte tipoAcesso) {
		this.ativoProperty().set(tipoAcesso);
	}

	public final boolean isAtivo() {
		return getAtivo() == 1;
	}

	@Override
	public String toString() {
		return "O produto " + getNome() + "eh cobrado por " + getUnidade() + ". E o seu valor eh "
				+ getValor() + ".";
	}

	@Override
	public boolean equals(Object o) {
		// se o objeto recebido for diferente de null e a sua classe for um
		// Produto verifica
		// igualdade no codigo do usuario.
		if (o != null && o.getClass() == this.getClass()) {
			Produto prod = (Produto) o;
			if (prod.getProdId() == this.getProdId()) {
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
