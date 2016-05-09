package labuonapastafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cadastro de produtos e pedidos.
 *
 * @author Deivid
 */
public class LabuonapastaFX extends Application {

    public static final String VIEW_LOGIN = "view/Login.fxml";
    public static final String VIEW_MENU = "view/Menu.fxml";
    public static final String VIEW_USUARIO = "view/Usuario.fxml";
    public static final String VIEW_ALTERAR_SENHA = "view/Senha.fxml";
    public static final String VIEW_PRODUTO = "view/Produto.fxml";
    public static final String VIEW_CLIENTE = "view/Cliente.fxml";
    public static final String VIEW_PEDIDO = "view/Pedido.fxml";
    public static final String VIEW_NUM_PEDIDO = "view/NumeroPedido.fxml";

    private static LabuonapastaFX instance;
    private static Stage stage;
    public static Usuario user;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        stage = primaryStage;
        user = new UsuarioNe().obterUsuario("deivid");
        goToMenu();
    }

    public static Stage getStage() {
        return stage;
    }

    public static LabuonapastaFX getInstance() {
        return instance;
    }

    /**
     * Setar o Usuário que está logado no sistema.
     * 
     * @param user Usuário que está logado no sistema.
     */
    public static void setUsuario(Usuario user) {
    	LabuonapastaFX.user = user;    	
    }
    
    /**
     * Efetuar a construção da tela de Login.
     */
    public void goToLogin() {

        stage.close();

        stage = new Stage();

        try {
            replaceSceneContent(VIEW_LOGIN);
        } catch (Exception ex) {
            Logger.getLogger(LabuonapastaFX.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Efetuar a construção da tela de Menu.
     *
     */
    public void goToMenu() {

        stage.close();

        stage = new Stage();

        try {
            replaceSceneContent(VIEW_MENU);
        } catch (Exception ex) {
            Logger.getLogger(LabuonapastaFX.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.setMaximized(true);

    }

    /**
     * Efetuar a transição de telas remodulando o conteúdo.
     *
     * @param fxml Caminho e nome do FXML que será carregado.
     * @return Objeto Initializable referente a tela a ser carregada
     * @throws Exception
     */
    private Initializable replaceSceneContent(String fxml) throws Exception {

        FXMLLoader loader = new FXMLLoader();

        Parent page;

        try (InputStream in = LabuonapastaFX.class.getResourceAsStream(fxml)) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(LabuonapastaFX.class.getResource(fxml));
            page = loader.load(in);
        }

        Scene scene = new Scene(page);

        stage.setScene(scene);
        stage.setTitle("Labuonapasta Produtos Alimenticios");
        stage.getIcons().add(new Image(getClass()
                .getResource("view/imagens/brasao_menu.png").toString()));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

        return (Initializable) loader.getController();

    }

    /**
     * Sair do sistema.
     */
    public static void exit() {
        stage.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
