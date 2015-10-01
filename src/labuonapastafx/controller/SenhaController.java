package labuonapastafx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import labuonapastafx.model.LimitedTextListener;

/**
 * Classe controladora do panel de manutencao dos usuarios do sistema. Fornece
 * ao usuario master ou administrador a possibilidade de incluir outros usuarios
 * e atribuir a eles funcoes especificas.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class SenhaController extends StackPane implements Initializable {

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
	private UsuarioNE usuarioNE;

	// Variaveis de controle do formulário da tela
	private String senha;
	private String novaSenha;
	private String confirmSenha;

	/**
	 * Procedimento a serem tomados quando pressionado o botao {@code Alterar}
	 *
	 * @param event
	 *            Evento startado pelo usuário
	 */
	@FXML
	public void botaoAlterarListener(ActionEvent event) {

		//Se as informações passadas pelo usuário forem válidas, efetuar a alteração da senha
		if (validarInformacoes()) {
			// Se o retorno da alteracao do usuario for true significa que a
			// alteracao foi ok
			if (getUsuarioNE().alterarUsuario(menuControl.user.getLogin(), menuControl.user.getNomeCompleto(),
					menuControl.user.getTipoAcesso(), novaSenha)) {
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
		} else if (!senha.equals(menuControl.user.getSenha())) {
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
	 * @param msg
	 *            Mensagem que se deseja passar para o usuário.
	 */
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
		txtSenha.textProperty().addListener(new LimitedTextListener<String>(txtSenha, 20));
		txtNovaSenha.textProperty().addListener(new LimitedTextListener<String>(txtNovaSenha, 20));
		txtConfirmSenha.textProperty().addListener(new LimitedTextListener<String>(txtConfirmSenha, 20));
	}

	/**
	 * Efetuar configurações iniciais para o controlador.
	 *
	 * @param menuControl
	 *            Objeto Controller do Menu principal
	 */
	public void setApp(MenuController menuControl) {
		this.menuControl = menuControl;
	}

}
