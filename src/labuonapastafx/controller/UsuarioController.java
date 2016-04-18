package labuonapastafx.controller;

import java.net.URL;
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
import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.LimitedTextListener;
import labuonapastafx.model.Usuario;

/**
 * Classe controladora do panel de manutencao dos usuarios do sistema. Fornece
 * ao usuario master ou administrador a possibilidade de incluir outros usuarios
 * e atribuir a eles funcoes especificas.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class UsuarioController extends StackPane implements Initializable {

    @FXML
    private TitledPane titledPane;
    @FXML
    private GridPane gridForm;
    @FXML
    private TextField txtCodUsuario;
    @FXML
    private TextField txtLogin;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtSenha;
    @FXML
    private TextField txtConfirmSenha;
    @FXML
    private ChoiceBox<AcessoEnum> cbxAcesso;
    @FXML
    private TableView<Usuario> tblUsuario;
    @FXML
    private TableColumn<Usuario, String> tblcolLogin;
    @FXML
    private TableColumn<Usuario, String> tblcolNome;
    @FXML
    private TableColumn<Usuario, AcessoEnum> tblcolAcesso;
    @FXML
    private TableColumn<Usuario, String> tblcolAtivo;
    @FXML
    private Button btnIncluir, btnAlterar, btnExcluir;

    // Variaveis de controle geral
    private MenuController menuControl;
    private UsuarioNe usuarioNe;
    private ObservableList<Usuario> users;

    // Variaveis de controle do formulário da tela
    private int cdUsuario;
    private String login;
    private String nome;
    private String senha;
    private String confirmSenha;
    private AcessoEnum acesso;

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Incluir}
     *
     * @param event Objeto do evento que foi executado
     */
    @FXML
    public void botaoIncluirListener(ActionEvent event) {

		// Efetuar a inclusão somente se as informações passadas estiverem
        // corretas.
        if (validarInformacoes()) {
			// Se o retorno da inclusão do usuário for true significa que a
            // inclusão foi ok
            if (usuarioNe.incluirUsuario(login, nome, acesso, senha)) {
                showAlert("Inclusão de usuário efetuada com sucesso");
                txtLogin.requestFocus();
                limparCampos();
            } else {
				// Inclusão nao foi efetuada porque o usário ja existe na base
                // de dados
                showAlert("Usuário já existe na base de dados");
                txtLogin.requestFocus();
            }

        }

    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Alterar}
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoAlterarListener(ActionEvent event) {

		// Efetuar a alteração somente se as informações passadas estiverem
        // corretas
        if (validarInformacoes()) {
            // Se o retorno da inclusao do usuario for true significa que a
            // inclusao foi ok
            if (usuarioNe.alterarUsuario(cdUsuario, login, nome, acesso, senha)) {
                showAlert("Alteracao de usuario efetuada com sucesso");
                limparCampos();
            } else {
                // Inclusão não foi efetuada porque o usario ja existe na base de dados
                showAlert("Usuário não existe, ou o login alterado já pertence a outro Usuário.");
            }
        }
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Excluir}
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoExcluirListener(ActionEvent event) {
        getValueFields();

        new Alert(AlertType.CONFIRMATION, "Confirma a exclusao do usuário").showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (usuarioNe.exclusaoLogica(this.cdUsuario)) {
                    showAlert("Exclusão efetuada com sucesso");
                    limparCampos();
                } else {
                    showAlert("Usuário não encontrado na base");
                }
            }
            txtLogin.requestFocus();
        });
    }

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Consultar}
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoConsultarListener(ActionEvent event) {
        getValueFields();

        Usuario usuario = usuarioNe.obterUsuario(this.login);

        if (usuario == null) {
            showAlert("Usuario não encontrado");
            return;
        }

        setValueFields(usuario);

    }

    /**
     * Procedimento a serem tomados quando pressionado uma linha da
     * {@code Tabela de Usuarios}
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    private void tabelaUsuarioListener(MouseEvent event) {
		// Se foi clicado duas vezes e o item não está nulo então processa a
        // consulta
        if (event.getClickCount() == 2 && tblUsuario.getSelectionModel().getSelectedItem() != null) {

            Usuario usuario = tblUsuario.getSelectionModel().getSelectedItem();

            setValueFields(usuario);
        }
    }

    /**
     * Sair da tela de cadastro de Usuário.
     */
    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Limpar os campos do fomulario de usuarios
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

        txtLogin.requestFocus();

        reiniciarListaUsuario();
    }

    /**
     * Procedimento a serem tomados quando o usuario digitar algum texto no
     * campo {@code Login}
     */
    @FXML
    private void txtLoginListener(KeyEvent event) {
        reiniciarListaUsuario();
    }

    /**
     * Obter o texto digitado no {@code Login} e atualizar a lista de usuarios
     * que possuam os caracteres informados
     */
    private void reiniciarListaUsuario() {
        users.setAll(usuarioNe.listarUsuarios(txtLogin.getText()));
    }

    /**
     * Obter os valores dos componentes do formulario de usuarios
     */
    private void getValueFields() {
        this.cdUsuario = Integer.parseInt(txtCodUsuario.getText());
        this.login = txtLogin.getText();
        this.nome = txtNome.getText();
        this.acesso = cbxAcesso.getValue();
        this.senha = txtSenha.getText();
        this.confirmSenha = txtConfirmSenha.getText();
    }

    /**
     * Valorizar os componentes do formuario conforme variaveis internas da
     * classe UsuarioGUI
     */
    private void setValueFields(Usuario user) {
        txtCodUsuario.setText(Integer.toString(user.getUserId()));
        txtLogin.setText(user.getLogin());
        txtNome.setText(user.getNomeCompleto());
        cbxAcesso.setValue(user.getTipoAcesso());
        txtSenha.setText(user.getSenha());
        txtConfirmSenha.setText(user.getSenha());
    }

    /**
     * Validar as informações passadas pelo usuário
     */
    private boolean validarInformacoes() {

        getValueFields();

        if (login.equals("")) {
            showAlert("Informar o login");
            txtLogin.requestFocus();
            return false;
        } else if (nome.equals("")) {
            showAlert("Informar o nome do usuario");
            txtNome.requestFocus();
            return false;
        } else if (senha.equals("")) {
            showAlert("Informar senha");
            txtSenha.requestFocus();
            return false;
        } else if (confirmSenha.equals("")) {
            showAlert("Confirmar senha");
            txtConfirmSenha.requestFocus();
            return false;
        } else if (!senha.equals(confirmSenha)) {
            showAlert("Senhas não confirmam");
            txtSenha.requestFocus();
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
    	
    	usuarioNe = new UsuarioNe();

        txtCodUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            //Só vai permitir alterar ou excluir quando o usuário selecionar um Usuário.
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

        txtLogin.textProperty().addListener(new LimitedTextListener(txtLogin, 15));
        txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 50));
        txtSenha.textProperty().addListener(new LimitedTextListener(txtSenha, 20));
        txtConfirmSenha.textProperty().addListener(new LimitedTextListener(txtConfirmSenha, 20));

        cbxAcesso.getItems().addAll(AcessoEnum.values());
        cbxAcesso.setValue(AcessoEnum.CADASTRO);

        // Obter a lista inicial dos usuários cadastrados na base de dados.
        users = FXCollections.observableArrayList(usuarioNe.listarUsuarios());

        // Formatar a TableView com as informações dos usuários obtidos.
        tblcolLogin.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        tblcolNome.setCellValueFactory(cellData -> cellData.getValue().nomeCompletoProperty());
        tblcolAcesso.setCellValueFactory(cellData -> cellData.getValue().tipoAcessoProperty());

        tblcolAtivo.setCellValueFactory(cellData -> {
            if (cellData.getValue().isAtivo()) {
                return new SimpleStringProperty("Sim");
            } else {
                return new SimpleStringProperty("Não");
            }
        });

        tblUsuario.setRowFactory((TableView<Usuario> table) -> new TableRow<Usuario>() {
            @Override
            protected void updateItem(Usuario user, boolean empty) {
				// Sempre chamar o metodo herdado para que a atualiação das
                // linhas ocorra com sucesso:
                super.updateItem(user, empty);

                if (!empty) {
                    if (!user.isAtivo()) {
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

        tblUsuario.setItems(users);

        Platform.runLater(() -> txtLogin.requestFocus());

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
