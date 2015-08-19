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
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import labuonapastafx.LabuonapastaFX;
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
    private MenuItem mntmCadUsuario;
    @FXML
    private MenuItem mntmAltSenha;
    @FXML
    private StackPane pnCentral;
    @FXML
    private Label lblUsuario;
    
    LabuonapastaFX app;
    
    /**
     * Inicializações principais para o funcionamento da classe.
     * 
     * @param app instancia da class principal LabuonapastaFX
     * @param user objeto usuário conetado ao sistema
     */
    public void setApp(LabuonapastaFX app, Usuario user) {
        this.app = app;
        lblUsuario.setText(user.getNomeCompleto());
    }
    
    /**
     * Mostrar a tela de alteração de senha.
     * @param event 
     */
    public void mntmAltSenhaListener(ActionEvent event) {

    }
    
    /**
     * Mostrar a tela de cadastro de usuário.
     * 
     * @param event 
     */
    public void mntmCadUsuarioListener(ActionEvent event) {
        UsuarioController userControl;
        
        userControl = (UsuarioController) loadView(LabuonapastaFX.VIEW_USUARIO);
        
        userControl.setApp(this);
    }
    
    /**
     * Efetuar a saída do sistema.
     * @param event 
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
    private Initializable loadView(String fxml) {
        
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
     * @param event 
     */
    public void efetuarLogout(ActionEvent event) {
        app.goToLogin();
    }
    
    /**
     * Efetuar o fechamento completo do sistema.
     */
    private void exit() {
        app.exit();
    }
  
    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}