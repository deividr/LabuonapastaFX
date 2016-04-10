package labuonapastafx.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
    private TableColumn<Pedido, String> tblcolCliente;
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
    private ClienteNe clieNe;
    private PedidoNe pedidoNe;
    private ObservableList<Pedido> pedidos;
    private ArrayList<ItemPedido> itens;
    private Map<String, Produto> mapProdutos;
    
    //Variáveis para controle do formulário da tela.
    private String telefone, nome, geladeira;
    private LocalDate dtRetirada;
    private Integer horaDe, horaAte;
    private Produto produto, molho;
    private BigDecimal qtde;

    /**
     * Incluir um novo item na lista de itens, esse evento será acionado pelao botão "+".
     *
     * @param event
     */
    @FXML
    void incluirItemListener(ActionEvent event) {

        getValuesItemPedido();

        if (produto == null) {
            showAlert("Informar um produto para incluir.");
            cbxProduto.requestFocus();
        } else if (qtde.doubleValue() == 0.0) {
            showAlert("Informar a quantidade do produto.");
            txtQtde.requestFocus();
        } else {

            int codigo;

            if (itens.isEmpty()) {
                codigo = 1;
            } else {
                codigo = itens.get(itens.size() - 1).getCodigo() + 1;
            }

            ItemPedido item = new ItemPedido(codigo, produto, molho, qtde);

            addItemList(item);

            itens.add(item);

            cbxProduto.getSelectionModel().clearSelection();

            cbxMolho.getSelectionModel().clearSelection();

            txtQtde.setText("");

            cbxProduto.requestFocus();
        }
    }

    /**
     * Excluir um item da lista de pedidos do cliente.
     *
     * @param event
     */
    @FXML
    void excluirItemListener(ActionEvent event) {

        int index = listItens.getSelectionModel().getSelectedIndex();

        itens.remove(index);

        listItens.getItems().clear();

        int codigo = 1;

        for (ItemPedido item : itens) {
            item.setCodigo(codigo++);
            addItemList(item);
        }

    }

    /**
     * Formatar e incluir um novo item na lista de itens do pedido.
     *
     * @param item
     */
    private void addItemList(ItemPedido item) {

        String strFormat = String.format("%3s  %7s %s  %s %s", item.getCodigo(),
                item.getQtde().toString(),
                item.getProduto().getUnidade().getCodigo().toLowerCase(),
                item.getProduto().getNome(),
                item.getMolho() != null ? item.getMolho().getNome() : "");

        listItens.getItems().add(strFormat);

    }

    /**
     * Incluir o pedido na base quando o botão Incluir for pressionado.
     *
     * @param event Evento disparado para esse método, no caso, acionado o botão Incluir.
     */
    @FXML
    void botaoIncluirListener(ActionEvent event) {
        //Se as informações foram preenchidas corretamente, faz a inclusão na base de pedidos.
        if (validateFields()) {

            Cliente clie = clieNe.obterClienteTelefone(telefone);

            //Se não existe o cliente cadastrado na base será feito a inclusão dos dados básicos.
            if (clie == null) {
                clieNe.incluirCliente(nome, telefone, "", "", "");
                clie = clieNe.obterClienteTelefone(telefone);
            }

            Usuario usuar = menuControl.user;

            Pedido ped = pedidoNe.incluir(usuar, clie, dtRetirada, horaDe, horaAte, geladeira,
                    itens, telefone, (byte) 0);

            showAlert("Inclusão do pedido efetuada com sucesso, número do pedido é: "
                    + ped.getPedId());
            
            txtTelefone.requestFocus();

            limparCampos(event);

        }
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
        getValuesPedido();

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

        if (itens.isEmpty()) {
            showAlert("Incluir ao menos um item para o pedido.");
            cbxProduto.requestFocus();
            return false;
        }

        return true;

    }

    /**
     * Método para obter os valores dos campos referentes ao Pedido.
     */
    private void getValuesPedido() {
        this.telefone = txtTelefone.getText().replaceAll("[^0-9]", "");

        this.nome = txtNome.getText();

        this.dtRetirada = dtpickRetirada.getValue();

        if (!txtHoraDe.getText().equals("")) {
            this.horaDe = Integer.parseInt(txtHoraDe.getText().replaceAll("[^0-9]", ""));
        }

        if (!txtHoraAte.getText().equals("")) {
            this.horaAte = Integer.parseInt(txtHoraAte.getText().replaceAll("[^0-9]", ""));
        }

        this.geladeira = txtGeladeira.getText();

    }

    /**
     * Método para obter os valores dos campos referentes aos Itens do Pedido.
     */
    private void getValuesItemPedido() {
        this.produto = cbxProduto.getValue();

        this.molho = cbxMolho.getValue();

        if (!txtQtde.getText().equals("")) {
            this.qtde = new BigDecimal(txtQtde.getText().replace(",", "."));
        } else {
            this.qtde = BigDecimal.valueOf(0.0);
        }
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
        pedidoNe = new PedidoNe();
        itens = new ArrayList<>();

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

                getValuesPedido();

                //Se telefone foi informado, consultar se está cadastrado para algum cliente.
                if (!this.telefone.equals("")) {
                    Cliente clie = clieNe.obterClienteTelefone(telefone);
                    txtNome.setText(clie.getNome());
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
        produtos.forEach(prod -> {
            if (prod.getTipo() == ProdutoEnum.MOLHO) {
                molhos.add(prod);
            }
            //Criar Map para consultar e sortear produtos no Combo de Produtos.
            mapProdutos.put(prod.getNome(), prod);
        });

        cbxProduto.getItems().setAll(produtos);

        //Criar um tipo de produto null para o caso de não ser selecionado nenhum molho.
        cbxMolho.getItems().add(new Produto());
        //Adciona todos os produtos do tipo molho ao ComboBox de Molhos.
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
        
        pedidos = FXCollections.observableArrayList(pedidoNe.obterPedidos(
                LocalDate.of(2016, Month.APRIL, 9)));
        
        // Formatar a TableView com as informações dos produtos obtidos.
        tblcolCliente.setCellValueFactory(cellData -> cellData.getValue().getClie().nomeProperty());
        
        tblPedido.setItems(pedidos);

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
