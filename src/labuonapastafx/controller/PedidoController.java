package labuonapastafx.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.Pedido;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;

import java.io.IOException;
import java.io.InputStream;
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
public class PedidoController extends StackPane implements Initializable {

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
    private ProdutoNe prodNe;
    private Pedido pedidoSel;
    private ObservableList<Pedido> pedidos;
    private FilteredList<Pedido> filteredList;
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

        Stage incluirStage = new Stage();

        String fxml = "view/IncluirPedido.fxml";

        FXMLLoader loader = new FXMLLoader();

        Parent page;

        try (InputStream in = LabuonapastaFX.class.getResourceAsStream(fxml)) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());

            loader.setLocation(LabuonapastaFX.class.getResource(fxml));

            page = loader.load(in);

            Scene scene = new Scene(page);

            incluirStage.setScene(scene);
            incluirStage.initStyle(StageStyle.UNDECORATED);
            incluirStage.centerOnScreen();

            ((IncluirPedidoController) loader.getController()).setApp(this, incluirStage);

            incluirStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                ((ListView) c).getItems().clear();
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
            //idxItemSel = tblPedido.getSelectionModel().getSelectedIndex();
        }
    }

    /**
     * Mostrar tela de mensagem para apontar erro ao <code>usuário</>.
     *
     * @param msg Mensagem que se deseja passar para o usuário.
     */
    private void showAlertWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cadastro de Pedido");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
        alert.showAndWait();
    }

    /**
     * Inicializar a classe de controle.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        prodNe = new ProdutoNe();

        pedidoNe = new PedidoNe();

        dtpickRetirada.setValue(LocalDate.now());

        carregarCombosProdutos();

        carregarTabelaPedidos();

        carregarListeners();

        Platform.runLater(() -> {
            txtPesquisar.requestFocus();
        });

    }

    /**
     * Preparar os combos de Produtos e Molhos.
     */
    private void carregarCombosProdutos() {

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

    //Adicionar o novo Pedido na tabela de pedidos.
    public void addPedidos(Pedido pedido) {
        this.pedidos.add(pedido);
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
