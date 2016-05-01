package labuonapastafx.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import labuonapastafx.model.Controllable;
import labuonapastafx.model.Pedido;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Classe controladora do painel de manutenção dos Pedidos.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class PedidoController extends StackPane implements Initializable, Controllable {

    @FXML
    private GridPane gridForm;
    @FXML
    private DatePicker dtpickRetirada;
    @FXML
    private TextField txtPesquisar;
    @FXML
    private TableView<Pedido> tblPedido;
    @FXML
    private TableColumn<Pedido, Number> tblcolNumero;
    @FXML
    private TableColumn<Pedido, String> tblcolCliente;
    @FXML
    private TableColumn<Pedido, String> tblcolTelefone;
    @FXML
    private TableColumn<Pedido, String> tblcolRetirada;
    @FXML
    private TableColumn<Pedido, String> tblcolDe;
    @FXML
    private TableColumn<Pedido, String> tblcolAte;
    @FXML
    private TableColumn<Pedido, String> tblcolGeladeira;
    @FXML
    private TableColumn<Pedido, String> tblcolSolicitado;

    // Variáveis de controle geral
    public MenuController menuControl;
    private PedidoNe pedidoNe;
    private Pedido pedidoSel;
    private int idxItemSel;
    private ObservableList<Pedido> pedidos;
    private FilteredList<Pedido> filteredList;
    private ProdutoNe prodNe;
    public Map<String, Produto> mapProdutos;
    public List<Produto> produtos;
    public List<Produto> molhos;

    /**
     * Incluir o pedido na base quando o botão Incluir for pressionado.
     *
     * @param event Evento disparado para esse método, no caso, acionado o botão Incluir.
     */
    @FXML
    void botaoIncluirListener(ActionEvent event) {

        //Senão carregar os componentes da tela de incluir.
        ManutencaoPedidoController.getInstance(this, ManutencaoPedidoController.VIEW_INCLUIR);

    }

    @FXML
    void limparCampos(ActionEvent event) {

        gridForm.getChildren().stream().forEach((c) -> {
            if (c instanceof TextField) {
                ((TextField) c).setText("");
            } else if (c instanceof ChoiceBox) {
                ((ChoiceBox<?>) c).getSelectionModel().clearSelection();
            } else if (c instanceof DatePicker) {
                ((DatePicker) c).setValue(LocalDate.now());
            } else if (c instanceof TextArea) {
                ((TextArea) c).clear();
            } else if (c instanceof ListView) {
                ((ListView<?>) c).getItems().clear();
            }
        });

        txtPesquisar.requestFocus();

    }

    @FXML
    void botaoSairListener(ActionEvent event) {
        menuControl.unloadView();
    }

    @FXML
    void tabelaPedidoListener(MouseEvent event) {
        // Se foi clicado duas vezes e o item não está nulo então processa a
        // consulta
        if (event.getClickCount() == 2
                && tblPedido.getSelectionModel().getSelectedItem() != null) {

            pedidoSel = tblPedido.getSelectionModel().getSelectedItem();
            idxItemSel = tblPedido.getSelectionModel().getSelectedIndex();

            //Senão carregar os componentes da tela de alteração ou exclusão.
            ManutencaoPedidoController.getInstance(this, ManutencaoPedidoController.VIEW_ALTERAR);
        }

    }

    /**
     * Inicializar a classe de controle.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        pedidoNe = new PedidoNe();

        dtpickRetirada.setValue(LocalDate.now());

        prodNe = new ProdutoNe();

        produtos = prodNe.listarProdutos();
        molhos = new ArrayList<>();
        mapProdutos = new HashMap<>();

        //Separa da lista da tabela todos os que são do tipo Molho.
        produtos.forEach(prod -> {
            if (prod.getTipo() == ProdutoEnum.MOLHO) {
                molhos.add(prod);
            }
            //Criar Map para consultar e sortear produtos no Combo de Produtos.
            mapProdutos.put(prod.getNome(), prod);
        });

        carregarTabelaPedidos();

        carregarListeners();

        Platform.runLater(() -> {
            txtPesquisar.requestFocus();
        });

    }

    /**
     * Carregar e configurar a tabela de Pedidos.
     */
    private void carregarTabelaPedidos() {
        // Formatar a TableView com as informações dos produtos obtidos.
        tblcolNumero.setCellValueFactory(cellData -> cellData.getValue().pedIdProperty());

        tblcolCliente.setCellValueFactory(cellData -> cellData.getValue().getClie()
                .nomeProperty());

        tblcolTelefone.setCellValueFactory(cellData -> {

            String tel = cellData.getValue().getClie().getTelefone1();

            tel = tel.replaceAll("([0-9]{2})([0-9]{1,11})$", "($1)$2");
            tel = tel.replaceAll("([0-9]{4,5})([0-9]{4})", "$1-$2");

            return new SimpleStringProperty(tel);

        });

        tblcolRetirada.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDtRetirada();
            String txtDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
            return new SimpleStringProperty(txtDate);
        });

        tblcolDe.setCellValueFactory(cellData -> cellData.getValue().horaDeProperty());
        tblcolAte.setCellValueFactory(cellData -> cellData.getValue().horaAteProperty());
        tblcolGeladeira.setCellValueFactory(cellData -> cellData.getValue().geladeiraProperty());

        tblcolSolicitado.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDtPedido();
            String txtDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
            return new SimpleStringProperty(txtDate);
        });

        pedidos = FXCollections.observableArrayList(pedidoNe.obterPedidos(LocalDate.now()));

        //Criar lista para efetuar filtro conforme argumentos de pesquisa do usuário.
        filteredList = new FilteredList<>(pedidos, p -> true);

        tblPedido.setItems(filteredList);

    }

    /**
     * Método responsável por fazer o carregamento de todos os Listener's de controle da tela.
     */
    private void carregarListeners() {

        dtpickRetirada.valueProperty().addListener((observable2, oldDate, newDate) -> {
            if (!newDate.isAfter(LocalDate.now())) {
                pedidos = FXCollections.observableArrayList(pedidoNe.obterPedidos(newDate));
                filteredList = new FilteredList<>(pedidos, p -> true);
                tblPedido.setItems(filteredList);
            }
        });

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredList.setPredicate(pedido -> {

                if (newValue == null || newValue.equals("")) {
                    return true;
                }

                //Pesquisar no número do pedido.
                if (pedido.getPedId().toString().contains(newValue)) {
                    return true;
                }

                //Pesquisar no nome do cliente.
                if (pedido.getClie().getNome().toLowerCase().contains(newValue)) {
                    return true;
                }

                //Pesquisar no número do telefone principal.
                if (pedido.getClie().getTelefone1().contains(newValue)) {
                    return true;
                }

                //Pesquisar no número do telefone secundário.
                if (pedido.getClie().getTelefone2().contains(newValue)) {
                    return true;
                }

                return false;

            });

        });

    }

    /**
     * Adicionar o novo Pedido na tabela de pedidos.
     */
    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

    /**
     * Atualizar o Pedido que foi selecionado na tabela de Pedidos.
     */
    public void atualizarPedido() {
        pedidos.set(idxItemSel, pedidoSel);
    }

    /**
     * Remover o Pedido selecionado na tabela de Pedidos.
     */
    public void removerPedido() {
        pedidos.remove(idxItemSel);
    }

    /**
     * Retorna o item Pedido que foi selecinado na tabela.
     *
     * @return Retorna o Pedido que foi selecionado.
     */
    public Pedido getPedidoSel() {
        return pedidoSel;
    }

    /**
     * Obter a lista de produtos.
     *
     * @return List com todos os produtos cadastrados no sistema.
     */
    public List<Produto> getProdutos() {
        return produtos;
    }

    /**
     * Obter a lista de molhos.
     *
     * @return List com todos os molhos cadastrados no sistema.
     */
    public List<Produto> getMolhos() {
        return molhos;
    }

    /**
     * Obter uma lista de produtos como Map.
     * 
     * @return Map com todos os produtos cadastrados no sistema.
     */
    public Map<String, Produto> getMapProdutos() {
        return mapProdutos;
    }


    /**
     * Efetuar configurações iniciais para o controlador.
     *
     * @param menuControl Objeto Controller do Menu principal
     */
    public void setApp(MenuController menuControl) {
        this.menuControl = menuControl;
    }

}
