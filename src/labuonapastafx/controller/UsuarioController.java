package labuonapastafx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import labuonapastafx.model.AcessoEnum;
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
    private TextField txtID;
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

    // Variaveis de controle geral
    private MenuController menuControl;
    private UsuarioNE usuarioNE;
    private ObservableList<Usuario> users;

    // Variaveis de controle do formulário da tela
    private int userID;
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
    public void botaoIncluirListener(ActionEvent event) {
        // Setar os valores dos campos inseridos pelo usuario
        getValueFields();

        if (login.equals("")) {
            showAlert("Informar o login");
            txtLogin.requestFocus();
        } else if (nome.equals("")) {
            showAlert("Informar o nome do usuario");
            txtLogin.requestFocus();
        } else if (senha.equals("")) {
            showAlert("Informar senha");
            txtLogin.requestFocus();
        } else if (confirmSenha.equals("")) {
            showAlert("Confirmar senha");
            txtLogin.requestFocus();
        } else if (!senha.equals(confirmSenha)) {
            showAlert("Senhas não confirmam");
            txtLogin.requestFocus();
        } else {
            // Se o retorno da inclusao do usuario for true significa que a inclusao foi ok
            if (getUsuarioNE().incluirUsuario(login, nome, acesso, senha)) {
                showAlert("Inclusão de usuário efetuada com sucesso");
                txtLogin.requestFocus();
                limparCampos();
                reiniciarListaUsuario();
            } else {
                //inclusao nao foi efetuada porque o usario ja existe na base de dados
                showAlert("Usuário já existe na base de dados");
                txtLogin.requestFocus();
            }
        }

    }

    @FXML
    public void botaoAlterarListener(ActionEvent event) {

    }

    @FXML
    public void botaoExcluirListener(ActionEvent event) {

    }

    @FXML
    public void botaoConsultarListener(ActionEvent event) {

    }

    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Limpar os campos do fomulario de usuarios
     */
    private void limparCampos() {

        gridForm.getChildren().stream().forEach((c) -> {
            if (c instanceof TextField) {
                ((TextField) c).setText("");
            } else if (c instanceof ChoiceBox) {
                ((ChoiceBox<?>) c).getSelectionModel().select(1);
            }
        });

        txtLogin.requestFocus();
    }

    /**
     * Obter o texto digitado no {@code login} e atualizar a lista de usuarios
     * que possuam os caracteres informados
     */
    private void reiniciarListaUsuario() {
        users.setAll(getUsuarioNE().listarUsuarios(txtLogin.getText()));
    }

    /**
     * Obter os valores dos componentes do formulario de usuarios
     */
    private void getValueFields() {
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
    private void setValueFields() {
        txtID.setText(Integer.toString(userID));
        txtLogin.setText(this.login);
        txtNome.setText(this.nome);
        cbxAcesso.setValue(this.acesso);
        txtSenha.setText(this.senha);
        txtConfirmSenha.setText(this.confirmSenha);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Informação Inválida");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Retorna o objeto instaciado da classe UsuarioNE
     *
     * @return usuarioNE Eh uma instancia da clase UsuarioNE
     */
    private UsuarioNE getUsuarioNE() {
        if (usuarioNE == null) {
            usuarioNE = new UsuarioNE();
        }

        return this.usuarioNE;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbxAcesso.getItems().addAll(AcessoEnum.values());
        cbxAcesso.setValue(AcessoEnum.CADASTRO);

        //Obter a lista inicial dos usuários cadastrados na base de dados.
        users = FXCollections.observableArrayList(getUsuarioNE().listarUsuarios());

        //Formatar a TableView com as informações dos usuários obtidos.
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

        tblcolLogin.getCellFactory().call(new TableColumn<Usuario, String>() {
        	
        	public TableCell<Usuario, String> call(TableColumn<Usuario, String> table) {
        		return new TableCell<Usuario, String> () {
        			
        		};
        	}
        });
        
        /*
        tblcolLogin.setCellFactory(col -> {
            
            public TableCell<Usuario, String> call(TableColumn<Usuario, String> login) {
	        	return new TableCell<Usuario, String>() {
                    public void updateItem(final Boolean item, final boolean empty) {

                    }
                };
            }
        });
        */

        /*
         tblUsuario.getColumns().forEach((col) -> {
         col.setCellFactory(column -> {
         return new TableCell<Usuario, Object>() {
        			
         };
         });
         });
         */
        tblUsuario.setItems(users);
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
