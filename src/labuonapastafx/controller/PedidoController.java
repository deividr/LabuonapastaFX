package labuonapastafx.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.util.StringConverter;
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
    private Cliente cliente;
    private ClienteNe clieNe;
    private ArrayList<ItemPedido> itens;
    private Map<String, Produto> mapProdutos;
    private String telefone, nome;
    private LocalDate dtRetirada;
    private Integer horaDe, horaAte, geladeira;
    private Produto produto, molho;
    private BigDecimal qtde;

    /**
     * Incluir um novo item na lista de itens, esse evento será acionado pelao botão "+".
     *
     * @param event
     */
    @FXML
    void incluirItemListener(ActionEvent event) {
        if (validateFields()) {
            if (cbxProduto.getSelectionModel().isEmpty()) {
                showAlert("Informar um produto para incluir.");
                cbxProduto.requestFocus();
            } else if (txtQtde.getText().equals("")) {
                showAlert("Informar a quantidade do produto.");
                cbxProduto.requestFocus();
            } else {

                int codigo;

                if (itens.isEmpty()) {
                    codigo = 1;
                } else {
                    codigo = itens.get(itens.size()).getCodigo() + 1;
                }

                NumberFormat nf = NumberFormat.getInstance();

                BigDecimal qtde;

                try {
                    qtde = BigDecimal.valueOf(nf.parse(txtQtde.getText()).doubleValue());
                } catch (ParseException e) {
                    qtde = BigDecimal.valueOf(0.0);
                }

                ItemPedido item = new ItemPedido(codigo, cbxProduto.getValue(), cbxMolho.getValue(), qtde);

                listItens.getItems().add(item.getCodigo() + "  " + item.getQtde() + "  " + item.getProduto().getNome()
                        + " " + item.getMolho().getNome());

                itens.add(item);

                cbxProduto.getSelectionModel().clearSelection();

                cbxMolho.getSelectionModel().clearSelection();

                txtQtde.setText("");

                cbxProduto.requestFocus();
            }
        }
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
        menuControl.unloadView();
    }

    @FXML
    void tabelaPedidoListener(ActionEvent event) {

    }

    /**
     * Método para validar os campos da tela.
     */
    private boolean validateFields() {
        getValueFields();

        if (telefone.equals("")) {
            showAlert("Informar o telefone do Cliente");
            txtTelefone.requestFocus();
            return false;
        }

        if (nome.equals("")) {
            showAlert("Informar o nome do Cliente");
            txtNome.requestFocus();
            return false;
        }

        if (dtRetirada.isBefore(LocalDate.now())) {
            showAlert("Data inválida. Favor informar data de hoje ou posterior.");
            dtpickRetirada.requestFocus();
            return false;
        }

        return true;

    }

    /**
     * Método para obter os valores do campos que foram modificados pelo usuário.
     */
    private void getValueFields() {
        this.telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
        this.nome = txtNome.getText();
        this.dtRetirada = dtpickRetirada.getValue();

        if (!txtHoraDe.getText().equals("")) {
            this.horaDe = Integer.parseInt(txtHoraDe.getText().replaceAll("[^0-9]", ""));
        }

        if (!txtHoraAte.getText().equals("")) {
            this.horaAte = Integer.parseInt(txtHoraAte.getText().replaceAll("[^0-9]", ""));
        }

        if (!txtGeladeira.getText().equals("")) {
            this.geladeira = Integer.parseInt(txtGeladeira.getText());
        }

        this.produto = cbxProduto.getValue();
        this.molho = cbxMolho.getValue();
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

        clieNe = new ClienteNe();

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

        txtTelefone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
                if (!telefone.equals("")) {
                    cliente = clieNe.obterClienteTelefone(telefone);
                    txtNome.setText(cliente.getNome());
                }
            }
        });

        txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 40));
        dtpickRetirada.setValue(LocalDate.now());

        txtHoraAte.textProperty().addListener(new HoraFieldListener(txtHoraAte));
        txtHoraDe.textProperty().addListener(new HoraFieldListener(txtHoraDe));
        txtGeladeira.textProperty().addListener(new LimitedTextListener(txtGeladeira, 3));

        //Limitar a 200 caracteres as informações do campo Observação:
        txtObservacoes.textProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.length() > 200) {
                txtObservacoes.setText(oldValue1);
            }
        });

        ProdutoNe prodNe = new ProdutoNe();

        ArrayList<Produto> produtos = prodNe.listarProdutos();
        ObservableList<Produto> molhos = FXCollections.observableArrayList();

        mapProdutos = new HashMap<>();

        //Separa da lista da tabela todos os que são do tipo Molho.
        produtos.forEach(produto -> {
            if (produto.getTipo() == ProdutoEnum.MOLHO) {
                molhos.add(produto);
            }
            //Criar Map para consultar e sortear produtos no Combo de Produtos.
            mapProdutos.put(produto.getNome(), produto);
        });

        cbxProduto.getItems().setAll(produtos);
        cbxMolho.getItems().add(new Produto());
        cbxMolho.getItems().addAll(molhos);

        txtQtde.textProperty().addListener(new QuantityFieldListener(txtQtde));

        cbxProduto.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null
                    && newValue.getTipo() != ProdutoEnum.MASSA) {
                cbxMolho.setValue(null);
                cbxMolho.setDisable(true);
            } else {
                cbxMolho.setDisable(false);
            }
        });

        cbxProduto.setConverter(new StringConverter<Produto>() {

            @Override
            public String toString(Produto produto) {
                if (produto == null) {
                    return "";
                }
                String str = produto.getNome();
                return str;
            }

            @Override
            public Produto fromString(String string) {
                if (!mapProdutos.containsKey(string)) {
                    cbxProduto.setValue(null);
                    return null;
                }

                return mapProdutos.get(string);
            }
        });

        FxUtil.autoCompleteComboBox(cbxProduto, FxUtil.AutoCompleteMode.STARTS_WITH);

        itens = new ArrayList<>();

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
