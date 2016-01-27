package labuonapastafx.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import labuonapastafx.model.LimitedTextListener;
import labuonapastafx.model.MoneyFieldListener;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;

/**
 * Classe controladora do panel de manutenção dos Produtos do sistema. Fornece
 * ao Produto master ou administrador a possibilidade de incluir outros Produtos
 * e atribuir a eles funções especificas.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ProdutoController extends StackPane implements Initializable {

    @FXML
    private GridPane gridForm;
    @FXML
    private TextField txtProdId;
    @FXML
    private TextField txtProduto;
    @FXML
    private ChoiceBox<ProdutoEnum> cbxTipo;
    @FXML
    private ChoiceBox<UnidadeEnum> cbxUnidade;
    @FXML
    private TextField txtValor;
    @FXML
    private TableView<Produto> tblProduto;
    @FXML
    private TableColumn<Produto, String> tblcolProduto;
    @FXML
    private TableColumn<Produto, ProdutoEnum> tblcolTipo;
    @FXML
    private TableColumn<Produto, UnidadeEnum> tblcolUnidade;
    @FXML
    private TableColumn<Produto, String> tblcolValor;
    @FXML
    private TableColumn<Produto, String> tblcolAtivo;
    @FXML
    private Button btnIncluir;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnExcluir;

    // Variáveis de controle geral
    private MenuController menuControl;
    private ProdutoNe produtoNe;
    private ObservableList<Produto> prodts;

    // Variáveis de controle do formulário da tela
    private int cdProduto;
    private String nome;
    private ProdutoEnum tipo;
    private UnidadeEnum unidade;
    private BigDecimal valor;

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Incluir}.
     *
     * @param event Objeto do evento que foi executado
     */
    @FXML
    public void botaoIncluirListener(ActionEvent event) {

        // Efetuar a inclusão somente se as informações passadas estiverem
        // corretas.
        if (validarInformacoes()) {
            // Se o retorno da inclusão do produto for true significa que a
            // inclusão foi ok
            if (produtoNe.incluirProduto(nome, unidade, valor, tipo)) {
                showAlert("Inclusão de produto efetuada com sucesso");
                txtProduto.requestFocus();
                limparCampos();
            } else {
                // Inclusão nao foi efetuada porque o produto ja existe na base
                // de dados
                showAlert("Produto já existe na base de dados");
                txtProduto.requestFocus();
            }

        }
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Alterar}.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoAlterarListener(ActionEvent event) {

        // Efetuar a alteração somente se as informações passadas estiverem corretas.
        if (validarInformacoes()) {

            // Se o retorno da alteração do Produto for true significa que a alteração foi ok.
            if (produtoNe.alterarProduto(cdProduto, nome, unidade, valor, tipo)) {
                showAlert("Alteração de Produto efetuada com sucesso.");
                limparCampos();
            } else {
                // Alteração nao foi efetuada porque o Produto não existe na base de dados.
                showAlert("Produto não existe, ou o nome alterado já pertence a outro Produto.");
            }
        }
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Excluir}.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoExcluirListener(ActionEvent event) {
        getValueFields();

        new Alert(AlertType.CONFIRMATION, "Confirma a exclusão do Produto").showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (produtoNe.exclusaoLogica(this.cdProduto)) {
                            showAlert("Exclusão efetuada com sucesso");
                            limparCampos();
                        } else {
                            showAlert("Produto não encontrado na base");
                        }
                    }
                    txtProduto.requestFocus();
                });
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Consultar}
     * .
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoConsultarListener(ActionEvent event) {
        getValueFields();

        Produto Produto = produtoNe.obterProduto(this.nome);

        if (Produto == null) {
            showAlert("Produto não encontrado");
            return;
        }

        setValueFields(Produto);

    }

    /**
     * Procedimento a serem tomados quando pressionado uma linha da
     * {@code Tabela de Produtos}.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    private void tabelaProdutoListener(MouseEvent event) {
        // Se foi clicado duas vezes e o item não está nulo então processa a
        // consulta
        if (event.getClickCount() == 2
                && tblProduto.getSelectionModel().getSelectedItem() != null) {

            Produto prodt = tblProduto.getSelectionModel().getSelectedItem();

            setValueFields(prodt);
        }
    }

    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Limpar os campos do fomulário de Produtos
     */
    @FXML
    private void limparCampos() {

        gridForm.getChildren().stream().forEach((c) -> {
            if (c instanceof TextField) {
                ((TextField) c).setText("");
            } else if (c instanceof ChoiceBox) {
                ((ChoiceBox<?>) c).getSelectionModel().select(1);
            }
        });

        txtProduto.requestFocus();
        reiniciarListaProduto();
    }

    /**
     * Procedimento a serem tomados quando o usuário digitar algum texto no
     * campo {@code Produto}
     */
    @FXML
    private void txtProdutoListener(KeyEvent event) {
        reiniciarListaProduto();
    }

    /**
     * Obter o texto digitado no {@code Nome} e atualizar a lista de
     * {@code Produto} que possuam os caracteres informados.
     */
    private void reiniciarListaProduto() {
        prodts.setAll(produtoNe.listarProdutos(txtProduto.getText()));
    }

    /**
     * Obter os valores dos componentes do formulário de {@code Produto}.
     */
    private void getValueFields() {

        if (!txtProdId.getText().equals("")) {
            this.cdProduto = Integer.parseInt(txtProdId.getText());
        } else {
            this.cdProduto = 0;
        }

        this.nome = txtProduto.getText();
        this.tipo = cbxTipo.getValue();
        this.unidade = cbxUnidade.getValue();

        try {
            NumberFormat nf = NumberFormat.getInstance();
            this.valor = BigDecimal.valueOf(nf.parse(txtValor.getText()).doubleValue());
        } catch (ParseException e) {
            this.valor = BigDecimal.valueOf(0.0);
        }

    }

    /**
     * Valorizar os componentes do formulário conforme variáveis internas da
     * classe ProdutoController
     */
    private void setValueFields(Produto prodt) {
        txtProdId.setText(Integer.toString(prodt.getProdId()));
        txtProduto.setText(prodt.getNome());
        cbxTipo.setValue(prodt.getTipo());
        cbxUnidade.setValue(prodt.getUnidade());
        txtValor.setText(String.valueOf(prodt.getValor()));
    }

    /**
     * Validar as informações passadas pelo usuário
     */
    private boolean validarInformacoes() {

        getValueFields();

        if (nome.equals("")) {
            showAlert("Informar o nome do Produto.");
            txtProduto.requestFocus();
            return false;
        } else if (valor.doubleValue() == 0.0) {
            showAlert("Valor não pode ser zerado.");
            txtValor.clear();
            txtValor.requestFocus();
            return false;
        }

        return true;
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

        produtoNe = new ProdutoNe();

        txtProdId.textProperty().addListener((observable, oldValue, newValue) -> {
            //Só vai permitir alterar ou excluir quando o usuário selecionar um produto.
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

        txtProduto.textProperty().addListener(new LimitedTextListener(txtProduto, 50));
        txtValor.textProperty().addListener(new MoneyFieldListener(txtValor));
        cbxTipo.getItems().addAll(ProdutoEnum.values());
        cbxTipo.setValue(ProdutoEnum.DIVERSOS);

        cbxUnidade.getItems().addAll(UnidadeEnum.values());
        cbxUnidade.setValue(UnidadeEnum.KILOGRAMA);

        // Obter a lista inicial dos produtos cadastrados na base de dados.
        prodts = FXCollections.observableArrayList(produtoNe.listarProdutos());

        // Formatar a TableView com as informações dos produtos obtidos.
        tblcolProduto.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        tblcolTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        tblcolUnidade.setCellValueFactory(cellData -> cellData.getValue().unidadeProperty());

        tblcolValor.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                    new DecimalFormat("##,###,##0.00").format(cellData.getValue().getValor()));
        });

        // Alinhando a direita o conteúdo da coluna valor.
        tblcolValor.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcolAtivo.setCellValueFactory(cellData -> {
            if (cellData.getValue().isAtivo()) {
                return new SimpleStringProperty("Sim");
            } else {
                return new SimpleStringProperty("Não");
            }
        });

        tblProduto.setRowFactory((TableView<Produto> table) -> new TableRow<Produto>() {
            @Override
            protected void updateItem(Produto prodt, boolean empty) {
                // Sempre chamar o metodo herdado para que a atualiação das
                // linhas ocorra com sucesso:
                super.updateItem(prodt, empty);

                if (!empty) {
                    if (!prodt.isAtivo()) {
                        getStyleClass().add("item-inativo");
                    } else {
                        getStyleClass().removeAll(Collections.singleton("item-inativo"));
                    }
                } else {
                    this.setVisible(false);
                    getStyleClass().removeAll(Collections.singleton("item-inativo"));
                }
            }
        });

        tblProduto.setItems(prodts);

        Platform.runLater(() -> {
            txtProduto.requestFocus();
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
