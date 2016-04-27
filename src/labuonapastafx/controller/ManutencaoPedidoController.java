package labuonapastafx.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Classe controladora do painel de manutenção dos Pedidos.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class ManutencaoPedidoController extends StackPane implements Initializable {

    private static ManutencaoPedidoController manutencaoPedidoController;
    private static Stage window;

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
    private Label lblTotal;

    // Variáveis de controle geral
    private PedidoController pedidoController;
    private ClienteNe clieNe;
    private PedidoNe pedidoNe;
    private ProdutoNe prodNe;
    private List<ItemPedido> itens;
    public Map<String, Produto> mapProdutos;
    public List<Produto> produtos;
    public List<Produto> molhos;

    //Variáveis para controle do formulário da tela.
    private String telefone, nome, horaDe, horaAte, geladeira, observacao;
    private LocalDate dtRetirada;
    private Produto produto, molho;
    private BigDecimal qtde, total = BigDecimal.valueOf(0);

    /**
     * Construtor privado para garantir apenas uma instancia desse objeto.
     */
    public static ManutencaoPedidoController getInstance(PedidoController pedidoController,
                                                         String fxml) {

        if (manutencaoPedidoController != null)  {
            return manutencaoPedidoController;
        }

        window = new Stage();

        FXMLLoader loader = new FXMLLoader();

        StackPane page;

        try (InputStream in = LabuonapastaFX.class.getResourceAsStream(fxml)) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(LabuonapastaFX.class.getResource(fxml));

            page = loader.load(in);

            Scene scene = new Scene(page);

            window.setScene(scene);
            window.initModality(Modality.WINDOW_MODAL);
            window.setResizable(false);
            window.initOwner(LabuonapastaFX.getStage());
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        manutencaoPedidoController = loader.getController();

        manutencaoPedidoController.pedidoController = pedidoController;

        return manutencaoPedidoController;
    }

    /**
     * Obter o Stage desse objeto.
     *
     * @return Stage referente ao objeto.
     */
    public Stage getStage() {
        return window;
    }

    /**
     * Incluir um novo item na lista de itens, esse evento será acionado pelao botão "+".
     *
     * @param event
     */
    @FXML
    void incluirItemListener(ActionEvent event) {

        getValuesItemPedido();

        if (produto == null) {
            showAlertWarning("Informar um produto para incluir.");
            cbxProduto.requestFocus();
        } else if (qtde.doubleValue() == 0.0) {
            showAlertWarning("Informar a quantidade do produto.");
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

            //Obter valor do item multiplicando a quantidade pelo seu valor unitário cadastrado.
            BigDecimal valor = item.getProduto().getValor().multiply(item.getQtde());

            total = total.add(valor);

            setLblTotal();

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

        //Obter valor do item multiplicando a quantidade pelo seu valor unitário cadastrado.
        BigDecimal valor = itens.get(index).getProduto().getValor()
                .multiply(itens.get(index).getQtde());

        total = total.subtract(valor);

        setLblTotal();

        itens.remove(index);

        listItens.getItems().clear();

        int codigo = 1;

        for (ItemPedido item : itens) {
            item.setCodigo(codigo++);
            addItemList(item);
        }

    }

    /**
     * Formatar o campo de total.
     */
    public void setLblTotal() {
        lblTotal.setText(String.format("%8.2f", total.floatValue()));
    }

    /**
     * Formatar e incluir um novo item na lista de itens do pedido.
     *
     * @param item
     */
    private void addItemList(ItemPedido item) {

        String massaMaisMolho = String.format("%s %s", item.getProduto().getNome(),
                item.getMolho() != null ? item.getMolho().getNome() : "");

        /*
         * Formato da string:
         *
         * ITEM   QTDE  UN  DESCRICAO                         VALOR
         * 999  999.999 kg  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  9999,99
         *
         */
        String strFormat = String.format("%3s  %7s %s  %-30s  %7.2f", item.getCodigo(),
                item.getQtde().toString(),
                item.getProduto().getUnidade().getCodigo().toLowerCase(),
                massaMaisMolho,
                item.getProduto().getValor().multiply(item.getQtde()));

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

            Pedido pedido = new Pedido();

            pedido.setUsuar(pedidoController.menuControl.user);
            pedido.setClie(clie);
            pedido.setDtRetirada(dtRetirada);
            pedido.setHoraDe(horaDe);
            pedido.setHoraDe(horaAte);
            pedido.setGeladeira(geladeira);
            pedido.setItens(FXCollections.observableArrayList(itens));
            pedido.setObservacao(observacao);

            pedido = pedidoNe.incluir(pedido);

            //Informar a inclusão do pedido e o seu número para o usuário.
            showAlertInformation("Pedido " + pedido.getPedId());

            txtTelefone.requestFocus();

            //Adiciona o novo pedido a tabela de pedidos.
            pedidoController.addPedidos(pedido);

            limparCampos(event);

        }
    }

    private void imprimirCupom() {

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

        lblTotal.setText("0,00");

        txtTelefone.requestFocus();

    }

    @FXML
    void botaoSairListener(ActionEvent event) {
        window.close();
    }

    /**
     * Método para validar os campos da tela.
     */
    private boolean validateFields() {
        getValuesPedido();

        if (telefone.equals("")) {
            showAlertWarning("Informar o telefone do Cliente");
            txtTelefone.requestFocus();
            return false;
        } else if (telefone.length() < 10) {
            showAlertWarning("Número de telefone inválido");
            txtTelefone.requestFocus();
            return false;
        }

        if (nome.equals("")) {
            showAlertWarning("Informar o nome do Cliente");
            txtNome.requestFocus();
            return false;
        }

        if (dtRetirada.isBefore(LocalDate.now())) {
            showAlertWarning("Data inválida. Favor informar data de hoje ou posterior.");
            dtpickRetirada.requestFocus();
            return false;
        }

        if (!horaDe.equals("")) {
            if (!horaDe.matches("[0-9]{2}:[0-9]{2}")) {
                showAlertWarning("Hora De inválida.");
                txtHoraDe.requestFocus();
                return false;
            }

            try {
                LocalTime time = LocalTime.of(Integer.parseInt(horaDe.substring(0, 2)),
                        Integer.parseInt(horaDe.substring(3, 5)));
            } catch (DateTimeException e) {
                showAlertWarning("Hora De inválida.");
                txtHoraDe.requestFocus();
                return false;
            }
        }

        if (!horaAte.equals("")) {
            if (!horaAte.matches("[0-9]{2}:[0-9]{2}")) {
                showAlertWarning("Hora Até inválida.");
                txtHoraAte.requestFocus();
                return false;
            }

            try {
                LocalTime time = LocalTime.of(Integer.parseInt(horaAte.substring(0, 2)),
                        Integer.parseInt(horaAte.substring(3, 5)));
            } catch (DateTimeException e) {
                showAlertWarning("Hora Até inválida.");
                txtHoraAte.requestFocus();
                return false;
            }
        }

        if (itens.isEmpty()) {
            showAlertWarning("Incluir ao menos um item para o pedido.");
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
        this.horaDe = txtHoraDe.getText();
        this.horaAte = txtHoraAte.getText();
        this.geladeira = txtGeladeira.getText();
        this.observacao = txtObservacoes.getText();
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
     * Mostrar tela de mensagem para apontar erro ao <code>usuário</>.
     *
     * @param msg Mensagem que se deseja passar para o usuário.
     */
    private void showAlertInformation(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cadastro de Pedido");
        alert.setHeaderText("Inclusão efetuada com sucesso!");

        Label lblNumeroPedido = new Label(msg);

        lblNumeroPedido.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold");
        lblNumeroPedido.setAlignment(Pos.CENTER);

        alert.getDialogPane().setContent(lblNumeroPedido);

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

        clieNe = new ClienteNe();

        pedidoNe = new PedidoNe();

        itens = new ArrayList<>();

        dtpickRetirada.setValue(LocalDate.now());

        carregarCombosProdutos();

        carregarListeners();

        Platform.runLater(() -> {
            txtTelefone.requestFocus();
        });

    }

    /**
     * Preparar os combos de Produtos e Molhos.
     */
    private void carregarCombosProdutos() {

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

        cbxProduto.getItems().setAll(produtos);

        //Criar um tipo de produto null para o caso de não ser selecionado nenhum molho.
        cbxMolho.getItems().add(new Produto());

        //Adciona todos os produtos do tipo molho ao ComboBox de Molhos.
        cbxMolho.getItems().addAll(molhos);

        //Converter a String escrita em um objeto produto da lista.
        cbxProduto.setConverter(new StringConverter<Produto>() {

            @Override
            public String toString(Produto produto) {
                if (produto == null) {
                    return "";
                }
                return produto.getNome();
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

    }

    /**
     * Método responsável por fazer o carregamento de todos os Listener's de controle da tela.
     */
    private void carregarListeners() {

        txtTelefone.textProperty().addListener(new FoneFieldListener(txtTelefone));

        txtTelefone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {

                getValuesPedido();

                //Se telefone foi informado, consultar se está cadastrado para algum cliente.
                if (!this.telefone.equals("")) {
                    Cliente clie = clieNe.obterClienteTelefone(telefone);
                    if (clie != null) {
                        txtNome.setText(clie.getNome());
                    } else {
                        txtNome.setText("");
                    }
                }
            }
        });

        txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 40));
        txtHoraAte.textProperty().addListener(new HoraFieldListener(txtHoraAte));
        txtHoraDe.textProperty().addListener(new HoraFieldListener(txtHoraDe));
        txtGeladeira.textProperty().addListener(new LimitedTextListener(txtGeladeira, 3));

        //Limitar a 200 caracteres as informações do campo Observação:
        txtObservacoes.textProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.length() > 200) {
                txtObservacoes.setText(oldValue1);
            }
        });

        txtQtde.textProperty().addListener(new QuantityFieldListener(txtQtde));

        cbxProduto.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Se o produto selecionado não for uma massa será inibido o combo de molho, pois molho
            //só pode ser selecionado quando o produto principal for uma massa.
            if (newValue != null
                    && newValue.getTipo() != ProdutoEnum.MASSA) {
                cbxMolho.setValue(null);
                cbxMolho.setDisable(true);
            } else {
                cbxMolho.setDisable(false);
            }
        });

    }

    /**
     * Efetuar configurações iniciais para o controlador.
     *
     * @param pedidoController Objeto Controller da tela Pedido.
     */
    public void setApp(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

}
