package labuonapastafx.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty pedId, horaDe, horaAte;
    private ObjectProperty<Cliente> clie;
    private ObjectProperty<Usuario> usuar;
    private ObjectProperty<Date> dtPedido;
    private ObjectProperty<Date> dtRetirada;
    private ListProperty<ItemPedido> itens;

    public Pedido() {
        this(0, null, null, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
                0, 0, null);
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

    public final IntegerProperty pedIdProperty() {
        return this.pedId;
    }

    public final int getPedId() {
        return this.pedIdProperty().get();
    }

    public final void setPedId(final int pedId) {
        this.pedIdProperty().set(pedId);
    }

    public final IntegerProperty horaDeProperty() {
        return this.horaDe;
    }

    public final int getHoraDe() {
        return this.horaDeProperty().get();
    }

    public final void setHoraDe(final int horaDe) {
        this.horaDeProperty().set(horaDe);
    }

    public final IntegerProperty horaAteProperty() {
        return this.horaAte;
    }

    public final int getHoraAte() {
        return this.horaAteProperty().get();
    }

    public final void setHoraAte(final int horaAte) {
        this.horaAteProperty().set(horaAte);
    }

    public final ObjectProperty<Cliente> clieProperty() {
        return this.clie;
    }

    public final labuonapastafx.model.Cliente getClie() {
        return this.clieProperty().get();
    }

    public final void setClie(final Cliente clie) {
        this.clieProperty().set(clie);
    }

    public final ObjectProperty<Usuario> usuarProperty() {
        return this.usuar;
    }

    public final Usuario getUsuar() {
        return this.usuarProperty().get();
    }

    public final void setUsuar(final labuonapastafx.model.Usuario usuar) {
        this.usuarProperty().set(usuar);
    }

    public final ObjectProperty<Date> dtPedidoProperty() {
        return this.dtPedido;
    }

    public final java.util.Date getDtPedido() {
        return this.dtPedidoProperty().get();
    }

    public final void setDtPedido(final java.util.Date dtPedido) {
        this.dtPedidoProperty().set(dtPedido);
    }

    public final ObjectProperty<Date> dtRetiradaProperty() {
        return this.dtRetirada;
    }

    public final java.util.Date getDtRetirada() {
        return this.dtRetiradaProperty().get();
    }

    public final void setDtRetirada(final java.util.Date dtRetirada) {
        this.dtRetiradaProperty().set(dtRetirada);
    }

    public final ListProperty<ItemPedido> itensProperty() {
        return this.itens;
    }

    public final javafx.collections.ObservableList<labuonapastafx.model.ItemPedido> getItens() {
        return this.itensProperty().get();
    }

    public final void setItens(
            final javafx.collections.ObservableList<labuonapastafx.model.ItemPedido> itens) {
        this.itensProperty().set(itens);
    }

}
