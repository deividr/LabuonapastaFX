package labuonapastafx.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import labuonapastafx.model.LimitedTextListener;
import labuonapastafx.model.Cliente;


/**
 * Classe controladora do panel de manutenção dos Clientes do sistema.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ClienteController extends StackPane implements Initializable {

    @FXML
    private GridPane gridForm;
    @FXML
    private TextField txtClieId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TableView<Cliente> tblCliente;
    @FXML
    private TableColumn<Cliente, String> tblcolCliente;
    @FXML
    private TableColumn<Cliente, String> tblcolTelefone;
    @FXML
    private TableColumn<Cliente, Date> tblcolData;

    // Variáveis de controle geral
    private MenuController menuControl;
    private ClienteNe clienteNe;
    private ObservableList<Cliente> clies;

    // Variáveis de controle do formulário da tela
    private String nome, telefone, endereco;
    private int clieId;

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
            if (clienteNe.incluirCliente(nome, telefone, endereco, new Date())) {
                showAlert("Inclusão de cliente efetuada com sucesso");
                txtNome.requestFocus();
                limparCampos();
                reiniciarListaCliente();
            } else {
                // Inclusão nao foi efetuada porque o cliente ja existe na base de dados
                showAlert("Cliente já existe na base de dados");
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

            Cliente Cliente = clienteNe.obterCliente(this.nome);

            if (Cliente != null) {
                // Se o retorno da inclusão do Cliente for true significa que a inclusão foi ok.
                if (clienteNe.alterarCliente(clieId, nome, telefone, endereco)) {
                    showAlert("Alteração de Cliente efetuada com sucesso");
                    limparCampos();
                    reiniciarListaCliente();
                } else {
                    // Inclusão nao foi efetuada porque o Cliente já existe na base de dados.
                    showAlert("Cliente não existe");
                }
            } else {
                showAlert("Cliente não existe");
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
            new Alert(AlertType.CONFIRMATION, "Confirma a exclusão do Cliente").showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (clienteNe.excluirNome(this.nome)) {
                        showAlert("Exclusão efetuada com sucesso");
                        limparCampos();
                        reiniciarListaCliente();
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

        Cliente Cliente = clienteNe.obterCliente(this.nome);

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
        if (event.getClickCount() == 2 && tblCliente.getSelectionModel().getSelectedItem() != null) {

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
    private void limparCampos() {

        gridForm.getChildren().stream().forEach((c) -> {
            if (c instanceof TextField) {
                ((TextField) c).setText("");
            } else if (c instanceof ChoiceBox) {
                ((ChoiceBox<?>) c).getSelectionModel().select(1);
            }
        });

        txtNome.requestFocus();
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
     * Obter o texto digitado no {@code Nome} e atualizar a lista de
     * {@code Cliente} que possuam os caracteres informados.
     */
    private void reiniciarListaCliente() {
        clies.setAll(clienteNe.listarClientes(txtNome.getText()));
    }

    /**
     * Obter os valores dos componentes do formulário de {@code Cliente}.
     */
    private void getValueFields() {
    	this.clieId = Integer.parseInt(txtClieId.getText());
        this.nome = txtNome.getText();
        this.telefone = txtTelefone.getText();
        this.endereco = txtEndereco.getText();
    }

    /**
     * Valorizar os componentes do formulário conforme variáveis internas da
     * classe ClienteController
     */
    private void setValueFields(Cliente clie) {
        txtClieId.setText(Integer.toString(clie.getClieId()));
        txtNome.setText(clie.getNome());
        txtTelefone.setText(clie.getTelefone());
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
        } else if (telefone.equals("")) {
        	showAlert("Informar o telefone do Cliente");
        	txtTelefone.requestFocus();
        	return false;
        } else if (endereco.equals("")) {
        	showAlert("Informar o endereço do Cliente");
        	txtEndereco.requestFocus();
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
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
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

        txtNome.textProperty().addListener(new LimitedTextListener<>(txtNome, 40));
        txtTelefone.textProperty().addListener(new LimitedTextListener<>(txtTelefone, 11));
        txtTelefone.setAlignment(Pos.BASELINE_RIGHT);
        txtEndereco.textProperty().addListener(new LimitedTextListener<>(txtEndereco, 50));

        // Obter a lista inicial dos clientes cadastrados na base de dados.
        clies = FXCollections.observableArrayList(clienteNe.listarClientes());

        // Formatar a TableView com as informações dos clientes obtidos.
        tblcolCliente.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        tblcolTelefone.setCellValueFactory(cellData -> cellData.getValue().telefoneProperty());
        tblcolData.setCellValueFactory(cellData -> cellData.getValue().dataCriacaoProperty());

        tblCliente.setItems(clies);

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
