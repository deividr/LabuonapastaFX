package labuonapastafx.controller;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import labuonapastafx.model.*;

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
    private TextField txtNumPed;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtNome;
    @FXML
    private DatePicker dtpickRetirada;
    @FXML
    private TextField txtHoraDe;
    @FXML
    private TextField txtHoraAte;
    @FXML
    private ComboBox<Produto> cbxProduto;
    @FXML
    private ComboBox<Produto> cbxMolho;
    @FXML
    private TextField txtQtde;
    @FXML
    private TextArea txtObservacoes;
    @FXML
    private ListView<String> listItens;
    @FXML
    private Button btnAdicionarItem;
    @FXML
    private Button btnExcluirItem;
    @FXML
    private Button btnIncluir;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnExcluir;
    @FXML
    private TableView<Pedido> tblPedido;
    @FXML
    private TableColumn<Pedido, Integer> tblcolNumero;
    @FXML
    private TableColumn<Cliente, String> tblcolCliente;
    @FXML
    private TableColumn<Cliente, String> tblcolTelefone;
    @FXML
    private TableColumn<Pedido, LocalDate> tblcolRetirada;
    @FXML
    private TableColumn<Pedido, Integer> tblcolDe;
    @FXML
    private TableColumn<Pedido, Integer> tblcolAte;
    @FXML
    private TableColumn<Pedido, LocalDate> tblcolSolicitado;

    // Variáveis de controle geral
    private MenuController menuControl;

    /**
     *
     * @param event
     */
    @FXML
    void incluirItemListener(ActionEvent event) {

    }

    @FXML
    void excluirItemListener(ActionEvent event) {

    }

    @FXML
    void botaoIncluirListener(ActionEvent event) {

    }

    @FXML
    void botaoAlterarListener(ActionEvent event) {

    }

    @FXML
    void botaoExcluirListener(ActionEvent event) {

    }

    @FXML
    void limparCampos(ActionEvent event) {

    }

    @FXML
    void botaoSairListener(ActionEvent event) {

    }

    @FXML
    void tabelaPedidoListener(ActionEvent event) {

    }

    /**
     * Mostrar tela de mensagem para apontar erro ao usuário.
     *
     * @param msg Mensagem que se deseja passar para o usuário.
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Informação Inválida");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
        alert.showAndWait();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txtNumPed.textProperty().addListener((observable, oldValue, newValue) -> {
            //Só vai permitir alterar ou excluir quando o usuário selecionar um pedido.
            if (newValue.equals("")) {
                btnAlterar.setDisable(true);
                btnExcluir.setDisable(true);
                btnIncluir.setDisable(false);
            } else {
                btnAlterar.setDisable(false);
                btnExcluir.setDisable(false);
                btnIncluir.setDisable(true);
            }
        });

        txtTelefone.textProperty().addListener(new FoneFieldListener(txtTelefone));
        txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 40));
        dtpickRetirada.setValue(LocalDate.now());

        ProdutoNe prodNe = new ProdutoNe();

        ArrayList<Produto> produtos = prodNe.listarProdutos();
        ObservableList<Produto> molhos = FXCollections.observableArrayList();

        produtos.forEach(produto -> {
            if (produto.getTipo() == ProdutoEnum.MOLHO) {
                molhos.add(produto);
            }
        });

        cbxProduto.getItems().setAll(produtos);
        cbxMolho.getItems().setAll(molhos);

        //cbxProduto.setCellFactory(new CellComboProduto());
        //cbxMolho.setCellFactory(new CellComboProduto());

        /*
         // Obter a lista inicial dos clientes cadastrados na base de dados.
         clies = FXCollections.observableArrayList(clienteNe.listarClientes());

         // Formatar a TableView com as informações dos clientes obtidos.
         tblcolCliente.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());

         tblcolTelefone1.setCellValueFactory(cellData -> {
         String value = cellData.getValue().getTelefone1();
         value = value.replaceAll("([0-9]{2})([0-9]{1,11})$", "($1)$2");
         value = value.replaceAll("([0-9]{4,5})([0-9]{4})", "$1-$2");
         return new SimpleStringProperty(value);
         });

         tblcolTelefone2.setCellValueFactory(cellData -> {
         String value = cellData.getValue().getTelefone2();
         if (value !=null) {
         value = value.replaceAll("([0-9]{2})([0-9]{1,11})$", "($1)$2");
         value = value.replaceAll("([0-9]{4,5})([0-9]{4})", "$1-$2");
         return new SimpleStringProperty(value);
         } else {
         return new SimpleStringProperty("");
         }

         });

         tblcolEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
         tblcolData.setCellValueFactory(cellData -> cellData.getValue().dataCriacaoProperty());

         tblCliente.setItems(clies);

         Platform.runLater(() -> {
         txtNome.requestFocus();;
         });
         */
    }

    /**
     * Efetuar configurações iniciais para o controlador.
     *
     * @param menuControl Objeto Controller do Menu principal
     */
    public void setApp(MenuController menuControl) {
        this.menuControl = menuControl;
    }

    private class CellComboProduto implements Callback<ListView<Produto>, ListCell<Produto>> {

        public CellComboProduto() {
        }

        @Override
        public ListCell<Produto> call(ListView<Produto> param) {
            final ListCell<Produto> cell = new ListCell<Produto>() {
                
                @Override
                public void updateItem(Produto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getNome());
                    }
                }
            };
            return cell;
        }
    }

}
