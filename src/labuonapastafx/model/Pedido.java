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
    private ListProperty<ItemPedido> itens;

    public Pedido() {
        this(0, null, null, new Date(), new Date(), 0, 0, null);
    }

    public Pedido(Integer pedId, Usuario usuar, Cliente clie, Date dtPedido, Date dtRetirada,
            Integer horaDe, Integer horaAte, ArrayList<ItemPedido> itens) {
        this.pedId = new SimpleIntegerProperty(pedId);
        this.clie = new SimpleObjectProperty<>(clie);
        this.usuar = new SimpleObjectProperty<>(usuar);
        this.dtPedido = new SimpleObjectProperty<>(dtPedido);
        this.dtRetirada = new SimpleObjectProperty<>(dtRetirada);
        this.horaDe = new SimpleIntegerProperty(horaDe);
        this.horaAte = new SimpleIntegerProperty(horaAte);
        this.itens = new SimpleListProperty<>(itens, "Itens do Pedido");
    }

}
