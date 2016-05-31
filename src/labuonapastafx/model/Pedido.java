package labuonapastafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty pedId = new SimpleIntegerProperty(0);
    private IntegerProperty numeroPedido = new SimpleIntegerProperty(0);
    private ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>();
    private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> dataPedido = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> dataRetirada = new SimpleObjectProperty<>();
    private StringProperty geladeira = new SimpleStringProperty("");
    private StringProperty horaDe = new SimpleStringProperty("");
    private StringProperty horaAte = new SimpleStringProperty("");
    private ListProperty<ItemPedido> itens = new SimpleListProperty<>();
    private StringProperty observacao = new SimpleStringProperty("");
    private BooleanProperty retirado = new SimpleBooleanProperty(false);

    public Pedido() {

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

    public final ObjectProperty<LocalDate> dataPedidoProperty() {
        return this.dataPedido;
    }

    public final ObjectProperty<LocalDate> dataRetiradaProperty() {
        return this.dataRetirada;
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

    public final BooleanProperty retiradoProperty() {
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

    public final LocalDate getDataPedido() {
        return this.dataPedidoProperty().get();
    }

    public final LocalDate getDataRetirada() {
        return this.dataRetiradaProperty().get();
    }

    public final ObservableList<ItemPedido> getItens() {
        return this.itensProperty().get();
    }

    public final String getObsercao() {
        return this.observacaoProperty().get();
    }

    public final Pedido setPedId(final Integer pedId) {
        this.pedIdProperty().setValue(pedId);
        return this;
    }

    public final Pedido setNumeroPedido(final Integer numeroPedido) {

        if (numeroPedido != null) {
            this.numeroPedidoProperty().set(numeroPedido);
        } else {
            throw new IllegalArgumentException("Número do Pedido Inválido");
        }

        return this;
    }

    public Pedido setUsuario(final Usuario usuario) {

        if (usuario != null) {
            this.usuarioProperty().setValue(usuario);
        } else {
            throw new IllegalArgumentException("Usuário inválido");
        }

        return this;
    }

    public Pedido setCliente(Cliente cliente) {

        if (cliente != null) {
            this.clienteProperty().setValue(cliente);
        } else {
            throw new IllegalArgumentException("Cliente inválido");
        }

        return this;

    }

    public Pedido setDataPedido(final LocalDate dataPedido) {

        if (dataPedido != null) {
            this.dataPedidoProperty().set(dataPedido);
        } else {
            throw new DateTimeException("Data do pedido inválida");
        }

        return this;

    }

    public final Pedido setDataRetirada(final LocalDate dataRetirada) {

        if (dataRetirada != null) {
            this.dataRetiradaProperty().set(dataRetirada);
        } else {
            throw new DateTimeException("Data da retirada inválida");
        }

        return this;
    }

    public final Pedido setHoraDe(final String horaDe) {

        if (!horaDe.equals("")) {
            //Verificar se o formato da data está correto:
            if (!horaDe.matches("[0-9]{2}:[0-9]{2}")) {
                throw new DateTimeException("Hora De Inválida");
            }

            LocalTime.of(Integer.parseInt(horaDe.substring(0, 2)),
                    Integer.parseInt(horaDe.substring(3, 5)));

            this.horaDeProperty().set(horaDe);
        }

        return this;
    }

    public final Pedido setHoraAte(final String horaAte) {

        if (!horaAte.equals("")) {
            //Verificar se o formato da data está correto:
            if (!horaAte.matches("[0-9]{2}:[0-9]{2}")) {
                throw new DateTimeException("Hora Até Inválida");
            }

            LocalTime.of(Integer.parseInt(horaAte.substring(0, 2)),
                    Integer.parseInt(horaAte.substring(3, 5)));

            this.horaAteProperty().set(horaAte);
        }

        return this;
    }

    public final Pedido setItens(final List<ItemPedido> itens) {
        ObservableList<ItemPedido> itensObs;

        if (itens != null && !itens.isEmpty()) {
            //Se a lista de pedidos for diferente de null e vazia carrega o atributo da classe.
            itensObs = FXCollections.observableArrayList(itens);
        } else {
            throw new IllegalArgumentException("Lista de itens está vazia");
        }

        this.itensProperty().set(itensObs);

        return this;
    }

    public final Pedido setObservacao(final String observacao) {
        this.observacaoProperty().set(observacao);
        return this;
    }

    /**
     * Alterar o conteúdo do campo retirado, essa informação indica se o pedido foi ou não retirado.
     *
     * @param retirado Se informado 0 = Pedido não retirado, se informado 1 = Pedido Retirado.
     * @return Retorna o próprio objeto.
     * @throws IllegalArgumentException Caso o conteúdo informado for difente de 0 e 1.
     */
    public final Pedido setRetirado(final boolean retirado) {
        this.retiradoProperty().set(retirado);
        return this;
    }

    public final Pedido setGeladeira(final String geladeira) {
        this.geladeiraProperty().set(geladeira);
        return this;
    }

    public final boolean isRetirado() {
        return retiradoProperty().getValue();
    }

}
