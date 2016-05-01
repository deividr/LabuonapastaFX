package labuonapastafx.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.AcessoEnum;
import labuonapastafx.model.Controllable;
import labuonapastafx.model.Usuario;

/**
 * FXML Controller class
 *
 * @author Deivid
 */
public class MenuController extends VBox implements Initializable {

    @FXML
    private MenuItem mntmLogout;
    @FXML
    private MenuItem mntmSair;
    @FXML
    private Menu mnCadastro;
    @FXML
    private MenuItem mntmCadUsuario;
    @FXML
    private MenuItem mntmCadProduto;
    @FXML
    private MenuItem mntmCadCliente;
    @FXML
    private MenuItem mntmAltSenha;
    @FXML
    private MenuItem mntmCadPedido;
    @FXML
    private Menu mnConfiguracoes;
    @FXML
    private MenuItem mntmNumeroPedido;
    @FXML
    private StackPane pnCentral;
    @FXML
    private Label lblUsuario;

    /**
     * Mostrar a tela de alteração de senha.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmSenhaListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_ALTERAR_SENHA).setApp(this);
    }

    /**
     * Mostrar a tela de cadastro de {@code Usuario}.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmCadUsuarioListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_USUARIO).setApp(this);
    }

    /**
     * Mostrar a tela de cadastro de {@code Produto}.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmCadProdutoListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_PRODUTO).setApp(this);
    }

    /**
     * Mostrar a tela de cadastro de {@code Cliente}.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmCadClienteListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_CLIENTE).setApp(this);
    }

    /**
     * Mostrar a tela de cadastro de {@code Pedido}.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmCadPedidoListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_PEDIDO).setApp(this);
    }

    /**
     * Mostrar a tela de cadastro de {@code Pedido}.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmNumeroPedidoListener(ActionEvent event) {
        loadView(LabuonapastaFX.VIEW_NUM_PEDIDO).setApp(this);
    }
    /**
     * Efetuar a saída do sistema.
     *
     * @param event Informações do evento que foi disparado.
     */
    public void mntmSairListener(ActionEvent event) {
        exit();
    }

    /**
     * Efetuar o carga da tela referente a opção do menu solicitada.
     *
     * @param fxml
     * @return
     */
    private Controllable loadView(String fxml) {

        StackPane pane;

        FXMLLoader loader = new FXMLLoader();

        try (InputStream in = LabuonapastaFX.class.getResourceAsStream(fxml)) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(LabuonapastaFX.class.getResource(fxml));
            pane = loader.load(in);
            pnCentral.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return loader.getController();
    }

    /**
     * Efetuar o fechamento do panel da opção ativa, limpando assim o conteudo
     * central do sistema.
     */
    public void unloadView() {
        pnCentral.getChildren().clear();
    }

    /**
     * Efetuar o logout do usuário conectado, voltando a solicitar o login.
     *
     * @param event
     */
    public void efetuarLogout(ActionEvent event) {
        LabuonapastaFX.getInstance().goToLogin();
    }

    /**
     * Efetuar o fechamento completo do sistema.
     */
    private void exit() {
        LabuonapastaFX.exit();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	lblUsuario.setText(LabuonapastaFX.user.getNomeCompleto());

        Usuario user = LabuonapastaFX.user;

        if (user.getTipoAcesso() == AcessoEnum.PEDIDO) {
            mnCadastro.setVisible(false);
            mnConfiguracoes.setVisible(false);
        } else if (user.getTipoAcesso() == AcessoEnum.CADASTRO) {
            mntmCadUsuario.setVisible(false);
            mnConfiguracoes.setVisible(false);
        }

    }

}