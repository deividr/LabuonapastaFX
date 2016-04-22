package labuonapastafx;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import labuonapastafx.controller.LoginController;
import labuonapastafx.controller.MenuController;
import labuonapastafx.controller.UsuarioNe;
import labuonapastafx.model.Usuario;

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

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        goToMenu(new UsuarioNe().obterUsuario("deivid"));
        stage.show();
    }

    /**
     * Efetuar a construção da tela de Login.
     */
    public void goToLogin() {

        LoginController login;

        try {
            login = (LoginController) replaceSceneContent(VIEW_LOGIN);
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(LabuonapastaFX.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.setResizable(false);
    }

    /**
     * Efetuar a construção da tela de Menu.
     *
     * @param user
     */
    public void goToMenu(Usuario user) {

        MenuController menuControl;

        try {
            menuControl = (MenuController) replaceSceneContent(VIEW_MENU);
            menuControl.setApp(this, user);
        } catch (Exception ex) {
            Logger.getLogger(LabuonapastaFX.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.setResizable(true);
    }

    /**
     * Efetuar a transição de telas remodulando o conteúdo.
     *
     * @param fxml
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
        stage.getIcons().add(new Image(getClass().getResource("view/imagens/brasao_menu.png").toString()));
        stage.centerOnScreen();

        return (Initializable) loader.getController();

    }

    /**
     * Sair do sistema.
     */
    public void exit() {
        stage.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
