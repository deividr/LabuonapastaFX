package labuonapastafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import labuonapastafx.model.Controllable;
import labuonapastafx.persistencia.ConfigXML;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe controladora do panel de manutencao dos usuarios do sistema. Fornece
 * ao usuario master ou administrador a possibilidade de incluir outros usuarios
 * e atribuir a eles funcoes especificas.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class NumeroPedidoController extends StackPane implements Initializable, Controllable {

    @FXML
    private TextField txtNumero;

    // Variaveis de controle geral
    private MenuController menuControl;

    /**
     * Procedimento a serem tomados quando pressionado o botao <code>alterar</>.
     *
     * @param event Evento startado pelo usuário
     */
    @FXML
    public void botaoAlterarListener(ActionEvent event) {
        if (validarInformacoes()) {
            ConfigXML.getInstance().alterarXMLTextByElement("inicial", txtNumero.getText());
            ConfigXML.getInstance().commitAlteracoes();
            showAlert("Alterar Número do pedido.", "Alteração efetuada com sucesso.");
            txtNumero.requestFocus();
        }
    }

    @FXML
    public void botaoSairListener() {
        menuControl.unloadView();
    }

    /**
     * Validar as informações passadas pelo usuário
     */
    private boolean validarInformacoes() {

        if (txtNumero.getText().equals("")) {
            showAlert("Informação Inválida", "Informe o número inicial do pedido");
            txtNumero.requestFocus();
            return false;
        }

        return true;

    }

    /**
     * Mostrar tela de mensagem para usuário.
     *
     * @param msg Mensagem que se deseja passar para o usuário.
     */
    private void showAlert(String header, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Número de Pedido");
        alert.setHeaderText(header);
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
        txtNumero.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 5) {
                txtNumero.setText(oldValue);
            } else {
                String value = txtNumero.getText();
                //Permitir apenas a inclusão de números.
                value = value.replaceAll("[^0-9]", "");
                txtNumero.setText(value);
            }
        });

        txtNumero.setText(ConfigXML.getInstance().obterXMLTextByElement("inicial"));
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
