package labuonapastafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty pedId, horaDe, horaAte;
    private ObjectProperty<Cliente> clie;
    private ObjectProperty<Usuario> usuar;
    private ObjectProperty<LocalDate> dtPedido;
    private ObjectProperty<LocalDate> dtRetirada;
    private ListProperty<ItemPedido> itens;
    private StringProperty observacao;
    private ObjectProperty<Byte> retirado;

    public Pedido() {
		this(0, null, null, LocalDate.now(), LocalDate.now(), 0, 0, null, "", (byte) 0);
    }

    public Pedido(Integer pedId, Usuario usuar, Cliente clie, LocalDate dtPedido, LocalDate dtRetirada,
                  Integer horaDe, Integer horaAte, List<ItemPedido> itens, String observacao, Byte retirado) {
        this.pedId = new SimpleIntegerProperty(pedId);
        this.clie = new SimpleObjectProperty<>(clie);
        this.usuar = new SimpleObjectProperty<>(usuar);
        this.dtPedido = new SimpleObjectProperty<>(dtPedido);
        this.dtRetirada = new SimpleObjectProperty<>(dtRetirada);
        this.horaDe = new SimpleIntegerProperty(horaDe);
        this.horaAte = new SimpleIntegerProperty(horaAte);

        ObservableList<ItemPedido> itensObs = FXCollections.observableArrayList(itens);

        this.itens = new SimpleListProperty<ItemPedido>(itensObs);
        this.observacao = new SimpleStringProperty(observacao);
        this.retirado = new SimpleObjectProperty<Byte>(retirado);
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

    public final Usuario getUsuario() {
        return this.usuarProperty().get();
    }

    public final void setUsuar(final labuonapastafx.model.Usuario usuar) {
        this.usuarProperty().set(usuar);
    }

    public final ObjectProperty<LocalDate> dtPedidoProperty() {
        return this.dtPedido;
    }

    public final LocalDate getDtPedido() {
        return this.dtPedidoProperty().get();
    }

    public final void setDtPedido(final LocalDate dtPedido) {
        this.dtPedidoProperty().set(dtPedido);
    }

    public final ObjectProperty<LocalDate> dtRetiradaProperty() {
        return this.dtRetirada;
    }

    public final LocalDate getDtRetirada() {
        return this.dtRetiradaProperty().get();
    }

    public final void setDtRetirada(final LocalDate dtRetirada) {
        this.dtRetiradaProperty().set(dtRetirada);
    }

    public final ListProperty<ItemPedido> itensProperty() {
        return this.itens;
    }

    public final ObservableList<ItemPedido> getItens() {
        return this.itensProperty().get();
    }

    public final void setItens(final ObservableList<ItemPedido> itens) {
        this.itensProperty().set(itens);
    }

    public final StringProperty observacaoProperty() {
        return this.observacao;
    }

    public final String getObsercao() {
        return this.observacaoProperty().get();
    }

    public final void setObservacao(final String observacao) {
        this.observacaoProperty().set(observacao);
    }

    public final ObjectProperty<Byte> retiradoProperty() {
        return this.retirado;
    }

    public final byte getRetirado() {
        return this.retiradoProperty().get();
    }

    public final void setRetirado(final Byte entregueAcesso) {
        this.retiradoProperty().set(entregueAcesso);
    }

    public final boolean isRetirado() {
        return getRetirado() == 1;
    }

}
