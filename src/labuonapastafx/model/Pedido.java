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
    private IntegerProperty numeroPedido;
    private ObjectProperty<Cliente> cliente;
    private ObjectProperty<Usuario> usuario;
    private ObjectProperty<LocalDate> dtPedido;
    private ObjectProperty<LocalDate> dtRetirada;
    private StringProperty geladeira, horaDe, horaAte;
    private ListProperty<ItemPedido> itens;
    private StringProperty observacao;
    private ObjectProperty<Byte> retirado;
/*
    public Pedido() {
        this(null, null, LocalDate.now(), LocalDate.now(), null);
    }
*/
    public Pedido(Usuario usuario, Cliente cliente, LocalDate dtPedido,
                  LocalDate dtRetirada, List<ItemPedido> itens) {
        this.pedId = new SimpleIntegerProperty();
        this.numeroPedido = new SimpleIntegerProperty();
        this.cliente = new SimpleObjectProperty<>(cliente);
        this.usuario = new SimpleObjectProperty<>(usuario);
        this.dtPedido = new SimpleObjectProperty<>(dtPedido);
        this.dtRetirada = new SimpleObjectProperty<>(dtRetirada);
        this.horaDe = new SimpleStringProperty();
        this.horaAte = new SimpleStringProperty();
        this.geladeira = new SimpleStringProperty();

        ObservableList<ItemPedido> itensObs = null;

        if (itens != null && !itens.isEmpty()) {
            //Se a lista de pedidos for diferente de null e vazia carrega o atributo da classe.
            itensObs = FXCollections.observableArrayList(itens);
        }

        this.itens = new SimpleListProperty<>(itensObs);
        this.observacao = new SimpleStringProperty();
        this.retirado = new SimpleObjectProperty<>((byte)0);
    }

    public Pedido(int pedId, Usuario usuario, Cliente cliente, LocalDate dtPedido,
                  LocalDate dtRetirada, List<ItemPedido> itens) {
        this(usuario, cliente, dtPedido, dtRetirada, itens);

        pedIdProperty().setValue(pedId);
    }

    public final IntegerProperty pedIdProperty() {
        return this.pedId;
    }

    public final IntegerProperty numeroPedidoProperty() {
        return this.numeroPedido;
    }

    public final ObjectProperty<Cliente> clienteProperty() {
        return this.cliente;
    }

    public final ObjectProperty<Usuario> usuarioProperty() {
        return this.usuario;
    }

    public final ObjectProperty<LocalDate> dtPedidoProperty() {
        return this.dtPedido;
    }

    public final ObjectProperty<LocalDate> dtRetiradaProperty() {
        return this.dtRetirada;
    }

    public final StringProperty horaDeProperty() {
        return this.horaDe;
    }

    public final StringProperty horaAteProperty() {
        return this.horaAte;
    }

    public final StringProperty observacaoProperty() {
        return this.observacao;
    }

    public final ObjectProperty<Byte> retiradoProperty() {
        return this.retirado;
    }

    public final StringProperty geladeiraProperty() {
        return this.geladeira;
    }

    public final ListProperty<ItemPedido> itensProperty() {
        return this.itens;
    }

    public final Integer getPedId() {
        return this.pedIdProperty().get();
    }

    public final Integer getNumeroPedido() {
        return this.numeroPedidoProperty().get();
    }

    public final String getHoraDe() {
        return this.horaDeProperty().getValue();
    }

    public final String getHoraAte() {
        return this.horaAteProperty().getValue();
    }

    public final String getGeladeira() {
        return this.geladeiraProperty().get();
    }

    public final Cliente getCliente() {
        return this.clienteProperty().get();
    }

    public final Usuario getUsuario() {
        return this.usuarioProperty().get();
    }

    public final LocalDate getDtPedido() {
        return this.dtPedidoProperty().get();
    }

    public final LocalDate getDtRetirada() {
        return this.dtRetiradaProperty().get();
    }

    public final ObservableList<ItemPedido> getItens() {
        return this.itensProperty().get();
    }

    public final String getObsercao() {
        return this.observacaoProperty().get();
    }

    public final byte getRetirado() {
        return this.retiradoProperty().get();
    }

    public final Pedido setPedId(final int pedId) {
        this.pedIdProperty().set(pedId);
        return this;
    }

    public final Pedido setUsuario(final Usuario usuario) {
        this.usuarioProperty().set(usuario);
        return this;
    }

    public final Pedido setNumeroPedido(final int numeroPedido) {
        this.numeroPedidoProperty().set(numeroPedido);
        return this;
    }

    public final Pedido setHoraDe(final String horaDe) {
        this.horaDeProperty().set(horaDe);
        return this;
    }

    public final Pedido setHoraAte(final String horaAte) {
        this.horaAteProperty().set(horaAte);
        return this;
    }

    public final Pedido setDtRetirada(final LocalDate dtRetirada) {
        this.dtRetiradaProperty().set(dtRetirada);
        return this;
    }

    public final Pedido setItens(final ObservableList<ItemPedido> itens) {
        this.itensProperty().set(itens);
        return this;
    }

    public final Pedido setObservacao(final String observacao) {
        this.observacaoProperty().set(observacao);
        return this;
    }

    public final Pedido setRetirado(final Byte entregueAcesso) {
        this.retiradoProperty().set(entregueAcesso);
        return this;
    }

    public final Pedido setGeladeira(final String geladeira) {
        this.geladeiraProperty().set(geladeira);
        return this;
    }

    public final boolean isRetirado() {
        return getRetirado() == 1;
    }

}
