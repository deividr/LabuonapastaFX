package labuonapastafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.control.Hyperlink;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.Controllable;

/**
 * Classe para controlar o objeto FXML da tela de Login.
 *
 * @author Deivid
 */
public class LoginController extends AnchorPane implements Initializable {

    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblMensagem;
    @FXML
    private Hyperlink lnkSair;

    private UsuarioNe usuarioNe;

    /**
     * Procedimento a serem tomados quando pressionado o botao {@code Ok}
     *
     * @param event Evento disparado ao clickar no botão OK.
     */
    @FXML
    public void botaoOKListener(ActionEvent event) {

        if (txtLogin.getText().equals("")) {
            sendMessage("Informe o nome do usuario");
        } else if (String.valueOf(txtSenha.getText()).equals("")) {
            sendMessage("Informe a senha do usuario");
        } else {

            // Verificar se a senha digitada e valida
            boolean usuarioSenhaValida = usuarioNe.validarSenha(txtLogin.getText(),
                    String.valueOf(txtSenha.getText()));

            if (usuarioSenhaValida) {
                LabuonapastaFX.getInstance().goToMenu();
            } else {
                sendMessage("Usuario ou senha invalida");
            }

        }
    }

    /**
     * Procedimentos efetuados quando o usuario clickar no link Sair
     *
     * @param event Evento disparado ao clickar no botão Sair.
     */
    @FXML
    public void linkSairListener(ActionEvent event) {
        LabuonapastaFX.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioNe = new UsuarioNe();
    }

    public void sendMessage(String mensagem) {
        lblMensagem.setText(mensagem);
        animateMessage();
    }

    public void animateMessage() {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), lblMensagem);
        ft.setFromValue(0.0);
        ft.setToValue(1);
        ft.play();
    }

}
