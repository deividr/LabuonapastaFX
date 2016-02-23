package labuonapastafx.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty pedId, horaDe, horaAte;
	private ObjectProperty<Cliente> clie;
	private ObjectProperty<Usuario> usuar;
	private ObjectProperty<Date> dtPedido;
	private ObjectProperty<Date> dtRetirada;
	private ObjectProperty<ArrayList<ItemPedido>> itens;

    public Pedido() {
        this(0, null, null, new Date(), new Date(), 0, 0, null);
    }

    public Pedido(Integer pedId, Usuario usuar, Cliente clie, Date dtPedido, Date dtRetirada,
				  Integer horaDe, Integer horaAte, ArrayList<ItemPedido> itens) {
        this.pedId = new SimpleIntegerProperty(pedId);
		this.clie = new SimpleObjectProperty<Cliente>(clie);
		this.usuar = new SimpleObjectProperty<Usuario>(usuar);
		this.dtPedido = new SimpleObjectProperty<Date>(dtPedido);
		this.dtRetirada = new SimpleObjectProperty<Date>(dtRetirada);
		this.horaDe = new SimpleIntegerProperty(horaDe);
		this.horaAte = new SimpleIntegerProperty(horaAte);
		this.itens = new SimpleObjectProperty<ArrayList<ItemPedido>>(itens);
	}

}
