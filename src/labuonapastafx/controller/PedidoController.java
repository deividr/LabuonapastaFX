package labuonapastafx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
    private TextField txtGeladeira;
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
    private TableColumn<Pedido, Integer> tblcolGeladeira;
    @FXML
    private TableColumn<Pedido, LocalDate> tblcolSolicitado;

    // Variáveis de controle geral
    private MenuController menuControl;

    /**
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

        txtHoraAte.textProperty().addListener(new HoraFieldListener(txtHoraAte));
        txtHoraDe.textProperty().addListener(new HoraFieldListener(txtHoraDe));
        txtGeladeira.textProperty().addListener(new LimitedTextListener(txtGeladeira, 3));

        ProdutoNe prodNe = new ProdutoNe();

        ArrayList<Produto> produtos = prodNe.listarProdutos();
        ObservableList<Produto> molhos = FXCollections.observableArrayList();

        //Separa da lista da tabela todos os que são do tipo Molho.
        produtos.forEach(produto -> {
            if (produto.getTipo() == ProdutoEnum.MOLHO) {
                molhos.add(produto);
            }
        });

        cbxProduto.getItems().setAll(produtos);
        cbxMolho.getItems().setAll(molhos);

        txtQtde.textProperty().addListener(new QuantityFieldListener(txtQtde));

        cbxProduto.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getTipo() != ProdutoEnum.MASSA) {
                cbxMolho.setValue(null);
                cbxMolho.setDisable(true);
            } else {
                cbxMolho.setDisable(false);
            }
        });

        Platform.runLater(() -> {
            txtTelefone.requestFocus();
        });
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
