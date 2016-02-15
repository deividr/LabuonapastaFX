package labuonapastafx.model;

import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe que irá modelar as informações que estão contidas em um item de pedido.
 */
public class ItemPedido {


    private ObjectProperty<Produto> produto;

    private ObjectProperty<BigDecimal> qtde;

    private ObjectProperty<Produto> molho;
    
    public ItemPedido() {
    	this(null, BigDecimal.valueOf(0.000), null);
    }
    
    public ItemPedido(Produto produto, BigDecimal qtde, Produto molho) {
    	this.produto = new SimpleObjectProperty<Produto>(produto);
    	this.qtde = new SimpleObjectProperty<BigDecimal>(qtde);
    	this.molho = new SimpleObjectProperty<Produto>(molho);
    }

	public final ObjectProperty<Produto> produtoProperty() {
		return this.produto;
	}
	

	public final Produto getProduto() {
		return this.produtoProperty().get();
	}
	

	public final void setProduto(final Produto produto) {
		this.produtoProperty().set(produto);
	}
	

	public final ObjectProperty<BigDecimal> qtdeProperty() {
		return this.qtde;
	}
	

	public final BigDecimal getQtde() {
		return this.qtdeProperty().get();
	}
	

	public final void setQtde(final BigDecimal qtde) {
		this.qtdeProperty().set(qtde);
	}
	

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
