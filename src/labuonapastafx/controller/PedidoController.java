package labuonapastafx.controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import labuonapastafx.model.Cliente;
import labuonapastafx.model.Pedido;
import labuonapastafx.model.Produto;

public class PedidoController {

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

    @FXML
    void txtTelefoneListener(ActionEvent event) {

    }

    @FXML
    void txtClienteListener(ActionEvent event) {

    }

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
    void tabelaClienteListener(ActionEvent event) {

    }

}
