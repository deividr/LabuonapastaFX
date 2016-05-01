package labuonapastafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.Controllable;
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
public class SenhaController extends StackPane implements Initializable, Controllable {

    @FXML
    private TextField txtSenha;
    @FXML
    private TextField txtNovaSenha;
    @FXML
    private TextField txtConfirmSenha;
    @FXML
    private VBox vboxPane;

    // Variaveis de controle geral
    private MenuController menuControl;
    private UsuarioNe usuarioNe;

    // Variaveis de controle do formulário da tela
    private String senha;
    private String novaSenha;
    private String confirmSenha;

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Alterar}
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoAlterarListener(ActionEvent event) {

        //Se as informações passadas pelo usuário forem válidas, efetuar a alteração da senha
        if (validarInformacoes()) {
        	
        	Usuario user = LabuonapastaFX.user;
        	
            // Se o retorno da alteracao do usuario for true significa que a
            // alteracao foi ok
            if (usuarioNe.alterarUsuario(user.getUserId(), user.getLogin(),
            		user.getNomeCompleto(), user.getTipoAcesso(), novaSenha)) {
                showAlert("Alteracao de senha efetuada com sucesso");
                limparCampos();
            } else {
                // alteracao nao foi efetuada porque o usario nao existe na base
                // de dados
                showAlert("Usuario nao existe");
            }

        }

    }

    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Limpar os campos do fomulario de usuarios
     */
    private void limparCampos() {

        vboxPane.getChildren().stream().forEach((c) -> {
            if (c instanceof TextField)
                ((TextField) c).setText("");
        });

        txtSenha.requestFocus();
    }

    /**
     * Obter os valores dos componentes do formulario de usuarios
     */
    private void getValueFields() {
        this.senha = txtSenha.getText();
        this.novaSenha = txtNovaSenha.getText();
        this.confirmSenha = txtConfirmSenha.getText();
    }

    /**
     * Validar as informações passadas pelo usuário
     */
    private boolean validarInformacoes() {

        getValueFields();

        if (senha.equals("")) {
            showAlert("Informar a senha atual");
            txtSenha.requestFocus();
            return false;
        } else if (!senha.equals(LabuonapastaFX.user.getSenha())) {
            showAlert("Senha atual inválida");
            txtSenha.requestFocus();
            return false;
        } else if (novaSenha.equals("")) {
            showAlert("Informar senha nova");
            txtNovaSenha.requestFocus();
            return false;
        } else if (confirmSenha.equals("")) {
            showAlert("Confirmar senha nova");
            txtConfirmSenha.requestFocus();
            return false;
        } else if (!senha.equals(confirmSenha)) {
            showAlert("Senhas não confirmam");
            txtNovaSenha.requestFocus();
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

        txtSenha.textProperty().addListener(new LimitedTextListener(txtSenha, 20));
        txtNovaSenha.textProperty().addListener(new LimitedTextListener(txtNovaSenha, 20));
        txtConfirmSenha.textProperty().addListener(new LimitedTextListener(txtConfirmSenha, 20));

        Platform.runLater(() -> {
            txtSenha.requestFocus();
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
