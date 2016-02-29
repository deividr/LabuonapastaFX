package labuonapastafx.model;

import java.math.BigDecimal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Classe que irá modelar as informações que estão contidas em um item de pedido.
 */
public class ItemPedido {

	private IntegerProperty codigo;

    private ObjectProperty<Produto> produto;

	private ObjectProperty<Produto> molho;

    private ObjectProperty<BigDecimal> qtde;
    
    public ItemPedido() {
    	this(0, null, null, BigDecimal.valueOf(0.000));
    }
    
    public ItemPedido(Integer codigo, Produto produto, Produto molho, BigDecimal qtde) {
		this.codigo = new SimpleIntegerProperty(codigo);
    	this.produto = new SimpleObjectProperty<Produto>(produto);
    	this.qtde = new SimpleObjectProperty<BigDecimal>(qtde);
    	this.molho = new SimpleObjectProperty<Produto>(molho);
    }

	@Contract(pure = true)
	public final IntegerProperty codigoProperty() {
		return this.codigo;
	}

	@NotNull
	public final Integer getCodigo() {
		return codigoProperty().get();
	}

	public final void setCodigo(Integer codigo) {
		codigoProperty().set(codigo);
	}

	@Contract(pure = true)
	public final ObjectProperty<Produto> produtoProperty() {
		return this.produto;
	}
	

	public final Produto getProduto() {
		return this.produtoProperty().get();
	}
	

	public final void setProduto(final Produto produto) {
		this.produtoProperty().set(produto);
	}
	

	@Contract(pure = true)
	public final ObjectProperty<BigDecimal> qtdeProperty() {
		return this.qtde;
	}
	

	public final BigDecimal getQtde() {
		return this.qtdeProperty().get();
	}
	

	public final void setQtde(final BigDecimal qtde) {
		this.qtdeProperty().set(qtde);
	}
	

	@Contract(pure = true)
	public final ObjectProperty<Produto> molhoProperty() {
		return this.molho;
	}
	

	public final Produto getMolho() {
		return this.molhoProperty().get();
	}
	

	public final void setMolho(final Produto molho) {
		this.molhoProperty().set(molho);
	}

}
