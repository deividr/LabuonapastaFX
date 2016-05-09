package labuonapastafx.controller;

import java.net.URL;
import java.util.Date;
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
import labuonapastafx.model.Controllable;
import labuonapastafx.util.FoneFieldListener;
import labuonapastafx.util.LimitedTextListener;
import labuonapastafx.model.Cliente;

/**
 * Classe controladora do panel de manutenção dos Clientes do sistema.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ClienteController extends StackPane implements Initializable, Controllable {

    @FXML
    private GridPane gridForm;
    @FXML
    private TextField txtClieId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtTelefone1;
    @FXML
    private TextField txtTelefone2;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView<Cliente> tblCliente;
    @FXML
    private TableColumn<Cliente, String> tblcolCliente;
    @FXML
    private TableColumn<Cliente, String> tblcolTelefone1;
    @FXML
    private TableColumn<Cliente, String> tblcolTelefone2;
    @FXML
    private TableColumn<Cliente, String> tblcolEmail;
    @FXML
    private TableColumn<Cliente, Date> tblcolData;
    @FXML
    private Button btnIncluir;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnExcluir;

    // Variáveis de controle geral
    private MenuController menuControl;
    private ClienteNe clienteNe;
    private ObservableList<Cliente> clies;

    // Variáveis de controle do formulário da tela
    private String nome, endereco, telefone1, telefone2, email;
    private int cdCliente;

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Incluir}.
     *
     * @param event Objeto do evento que foi executado
     */
    @FXML
    public void botaoIncluirListener(ActionEvent event) {
        // Efetuar a inclusão somente se as informações passadas estiverem corretas.
        if (validarInformacoes()) {
            // Se o retorno da inclusão do cliente for true significa que a inclusão foi ok
            if (clienteNe.incluirCliente(nome, telefone1, telefone2, email, endereco)) {
                showAlert("Inclusão de cliente efetuada com sucesso");
                limparCampos();
            } else {
                // Inclusão nao foi efetuada porque o cliente ja existe na base de dados
                showAlert("Cliente já existe na base de dados ou o telefone está cadastrado para " +
                        "outro Cliente");
                txtNome.requestFocus();
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

            Cliente Cliente = clienteNe.obterCodCliente(this.cdCliente);

            if (Cliente != null) {
                // Se o retorno da inclusão do Cliente for true significa que a inclusão foi ok.
                if (clienteNe.alterarCliente(cdCliente, nome, telefone1, telefone2,
                        email, endereco)) {
                    showAlert("Alteração de Cliente efetuada com sucesso");
                    limparCampos();
                } else {
                    // Inclusão nao foi efetuada porque o Cliente já existe na base de dados.
                    showAlert("Cliente não existe, ou número de telefone já cadastrado para " +
                            "outro Cliente");
                }
            } else {
                showAlert("Cliente não existe, ou número de telefone já cadastrado para " +
                        "outro Cliente");
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

        if (nome.equals("")) {
            showAlert("Informar o cliente");
        } else {
            new Alert(AlertType.CONFIRMATION, "Confirma a exclusão do Cliente").showAndWait()
                    .ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (clienteNe.excluir(this.cdCliente)) {
                                showAlert("Exclusão efetuada com sucesso");
                                limparCampos();
                            } else {
                                showAlert("Cliente não encontrado na base");
                            }
                        }
                        txtNome.requestFocus();
                    });
        }
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao
     * {@code Consultar}.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoConsultarListener(ActionEvent event) {
        getValueFields();

        Cliente Cliente = clienteNe.obterClienteNome(this.nome);

        if (Cliente == null) {
            showAlert("Cliente não encontrado");
            return;
        }

        setValueFields(Cliente);

    }

    /**
     * Procedimento a serem tomados quando pressionado uma linha da
     * {@code Tabela de Clientes}.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    private void tabelaClienteListener(MouseEvent event) {
        // Se foi clicado duas vezes e o item não está nulo então processa a consulta
        if (event.getClickCount() == 2 && tblCliente.getSelectionModel()
                .getSelectedItem() != null) {

            Cliente prodt = tblCliente.getSelectionModel().getSelectedItem();

            setValueFields(prodt);
        }
    }

    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Limpar os campos do fomulário de Clientes
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

        txtNome.requestFocus();
        reiniciarListaCliente();
    }

    /**
     * Procedimento a serem tomados quando o usuário digitar algum texto no
     * campo {@code Cliente}
     */
    @FXML
    private void txtClienteListener(KeyEvent event) {
        reiniciarListaCliente();
    }

    /**
     * Procedimento a serem tomados quando o usuário digitar algum texto no
     * campo {@code Cliente}
     */
    @FXML
    private void txtTelefoneListener(KeyEvent event) {
        reiniciarListaTelefone();
    }

    /**
     * Obter o texto digitado no {@code Nome} e atualizar a lista de
     * {@code Cliente} que possuam os caracteres informados.
     */
    private void reiniciarListaCliente() {
        clies.setAll(clienteNe.listarClientes(txtNome.getText()));
    }

    /**
     * Obter o texto digitado no {@code Telefone} e atualizar a lista de {@code Cliente} que possuam
     * os caracteres
     * informados.
     */
    private void reiniciarListaTelefone() {
        if (txtTelefone1.isFocused()) {
            clies.setAll(clienteNe.listarClientesTelefone(txtTelefone1.getText()
                    .replaceAll("[^0-9]", "")));
        } else {
            clies.setAll(clienteNe.listarClientesTelefone(txtTelefone2.getText()
                    .replaceAll("[^0-9]", "")));
        }
    }

    /**
     * Obter os valores dos componentes do formulário de {@code Cliente}.
     */
    private void getValueFields() {

        if (!txtClieId.getText().equals(""))
            this.cdCliente = Integer.parseInt(txtClieId.getText());

        this.nome = txtNome.getText();
        this.telefone1 = txtTelefone1.getText().replaceAll("[^0-9]", "");
        this.telefone2 = txtTelefone2.getText().replaceAll("[^0-9]", "");
        this.email = txtEmail.getText();
        this.endereco = txtEndereco.getText();
    }

    /**
     * Valorizar os componentes do formulário conforme variáveis internas da
     * classe ClienteController
     */
    private void setValueFields(Cliente clie) {
        txtClieId.setText(Integer.toString(clie.getClieId()));
        txtNome.setText(clie.getNome());
        txtTelefone1.setText(clie.getTelefone1());
        txtTelefone2.setText(clie.getTelefone2());
        txtEmail.setText(clie.getEmail());
        txtEndereco.setText(clie.getEndereco());
    }

    /**
     * Validar as informações passadas pelo usuário
     */
    private boolean validarInformacoes() {

        getValueFields();

        if (nome.equals("")) {
            showAlert("Informar o nome do Cliente");
            txtNome.requestFocus();
            return false;
        } else if (telefone1.equals("")) {
            showAlert("Informar o telefone principal do Cliente");
            txtTelefone1.requestFocus();
            return false;
        } else if (!telefone1.equals("") &&
                !telefone1.matches("([0-9]{10,11})")) {
            showAlert("Telefone principal inválido.");
            txtTelefone1.requestFocus();
            return false;
        } else if (!telefone2.equals("") &&
                !telefone2.matches("([0-9]{10,11})")) {
            showAlert("Telefone secundário inválido");
            txtTelefone2.requestFocus();
            return false;
        } else if (!email.equals("") &&
                !email.matches("[a-zA-Z0-9]{4,}+([-_.][a-zA-Z0-9]{1,}@|@)+" +
                        "[a-zA-Z0-9]{2,}\\.([a-zA-Z]{2,}|[a-zA-Z]{2,}\\.[a-zA-Z]{2,})")) {
            showAlert("Endereço de email inválido");
            txtEmail.requestFocus();
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

        clienteNe = new ClienteNe();

        txtClieId.textProperty().addListener((observable, oldValue, newValue) -> {
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

        txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 40));
        txtTelefone1.textProperty().addListener(new FoneFieldListener(txtTelefone1));
        txtTelefone2.textProperty().addListener(new FoneFieldListener(txtTelefone2));
        txtEndereco.textProperty().addListener(new LimitedTextListener(txtEndereco, 50));

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
            if (value != null) {
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
            txtNome.requestFocus();
            ;
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
