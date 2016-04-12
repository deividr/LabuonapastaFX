package labuonapastafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty pedId;
    private ObjectProperty<Cliente> clie;
    private ObjectProperty<Usuario> usuar;
    private ObjectProperty<LocalDate> dtPedido;
    private ObjectProperty<LocalDate> dtRetirada;
    private StringProperty geladeira, horaDe, horaAte;
    private ListProperty<ItemPedido> itens;
    private StringProperty observacao;
    private ObjectProperty<Byte> retirado;

    public Pedido() {
		this(0, null, null, LocalDate.now(), LocalDate.now(), "", "", "", null, "",
                (byte)0);
    }

    public Pedido(Integer pedId, Usuario usuar, Cliente clie, LocalDate dtPedido, 
            LocalDate dtRetirada, String horaDe, String horaAte, String geladeira,
            List<ItemPedido> itens, String observacao, Byte retirado) {
        this.pedId = new SimpleIntegerProperty(pedId);
        this.clie = new SimpleObjectProperty<>(clie);
        this.usuar = new SimpleObjectProperty<>(usuar);
        this.dtPedido = new SimpleObjectProperty<>(dtPedido);
        this.dtRetirada = new SimpleObjectProperty<>(dtRetirada);
        this.horaDe = new SimpleStringProperty(horaDe);
        this.horaAte = new SimpleStringProperty(horaAte);
        this.geladeira = new SimpleStringProperty(geladeira);

        ObservableList<ItemPedido> itensObs = FXCollections.observableArrayList(itens);

        this.itens = new SimpleListProperty<>(itensObs);
        this.observacao = new SimpleStringProperty(observacao);
        this.retirado = new SimpleObjectProperty<>(retirado);
    }

    public final IntegerProperty pedIdProperty() {
        return this.pedId;
    }

    public final Integer getPedId() {
        return this.pedIdProperty().get();
    }

    public final void setPedId(final int pedId) {
        this.pedIdProperty().set(pedId);
    }

    public final StringProperty horaDeProperty() {
        return this.horaDe;
    }

    public final String getHoraDe() {
        return this.horaDeProperty().getValue();
    }

    public final void setHoraDe(final String horaDe) {
        this.horaDeProperty().set(horaDe);
    }

    public final StringProperty horaAteProperty() {
        return this.horaAte;
    }

    public final String getHoraAte() {
        return this.horaAteProperty().getValue();
    }

    public final void setHoraAte(final String horaAte) {
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

    public final StringProperty geladeiraProperty() {
        return this.geladeira;
    }

    public final String getGeladeira() {
        return this.geladeiraProperty().get();
    }

    public final void setGeladeira(final String geladeira) {
        this.geladeiraProperty().set(geladeira);
    }
}
