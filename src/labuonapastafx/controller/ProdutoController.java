package labuonapastafx.controller;

import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import labuonapastafx.model.LimitedTextListener;
import labuonapastafx.model.Produto;
import labuonapastafx.model.ProdutoEnum;
import labuonapastafx.model.UnidadeEnum;

/**
 * Classe controladora do panel de manutencao dos Produtos do sistema. Fornece
 * ao Produto master ou administrador a possibilidade de incluir outros Produtos
 * e atribuir a eles funcoes especificas.
 *
 * @author Deivid Assumpcao Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ProdutoController extends StackPane implements Initializable {

	@FXML
	private GridPane gridForm;
	@FXML
	private TextField txtProdID;
	@FXML
	private TextField txtProduto;
	@FXML
	private ChoiceBox<ProdutoEnum> cbxTipo;
	@FXML
	private ChoiceBox<UnidadeEnum> cbxUnidade;
	@FXML
	private TextField txtValor;
	@FXML
	private TableView<Produto> tblProduto;
	@FXML
	private TableColumn<Produto, String> tblcolProduto;
	@FXML
	private TableColumn<Produto, ProdutoEnum> tblcolTipo;
	@FXML
	private TableColumn<Produto, UnidadeEnum> tblcolUnidade;
	@FXML
	private TableColumn<Produto, String> tblcolValor;

	// Variáveis de controle geral
	private MenuController menuControl;
	private ProdutoNE ProdutoNE;
	private ObservableList<Produto> prodts;

	// Variáveis de controle do formulário da tela
	private String nome;
	private ProdutoEnum tipo;
	private UnidadeEnum unidade;
	private Double valor;

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
			// Se o retorno da inclusão do produto for true significa que a
			// inclusão foi ok
			if (getProdutoNE().incluirProduto(nome, unidade, valor, tipo)) {
				showAlert("Inclusão de produto efetuada com sucesso");
				txtProduto.requestFocus();
				limparCampos();
				reiniciarListaProduto();
			} else {
				// Inclusão nao foi efetuada porque o produto ja existe na base
				// de dados
				showAlert("Produto já existe na base de dados");
				txtProduto.requestFocus();
			}

		}
	}

	/**
	 * Procedimento a serem tomados quando pressionado o botao {@code Alterar}
	 *
	 * @param event Evento startado pelo produto
	 */
	@FXML
	public void botaoAlterarListener(ActionEvent event) {

		// Efetuar a alteração somente se as informações passadas estiverem
		// corretas
		if (validarInformacoes()) {

			Produto Produto = getProdutoNE().obterProduto(this.nome);

			if (Produto != null) {
				// Se o retorno da inclusao do Produto for true significa que a
				// inclusao foi ok
				if (getProdutoNE().alterarProduto(nome, unidade, valor, tipo)) {
					showAlert("Alteracao de Produto efetuada com sucesso");
					limparCampos();
					reiniciarListaProduto();
				} else {
					// inclusao nao foi efetuada porque o Produto já existe na
					// base de dados
					showAlert("Produto nao existe");
				}
			} else {
				showAlert("Produto nao existe");
			}

		}

	}

	/**
	 * Procedimento a serem tomados quando pressionado o botao {@code Excluir}
	 *
	 * @param event Evento startado pelo produto
	 */
	@FXML
	public void botaoExcluirListener(ActionEvent event) {
		getValueFields();

		if (nome.equals("")) {
			showAlert("Informar o nome");
		} else {
			new Alert(AlertType.CONFIRMATION, "Confirma a exclusão do Produto").showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					if (getProdutoNE().exclusaoLogica(this.nome)) {
						showAlert("Exclusão efetuada com sucesso");
						limparCampos();
						reiniciarListaProduto();
					} else {
						showAlert("Produto não encontrado na base");
					}
				}
				txtLogin.requestFocus();
			});
		}
	}

	/**
	 * Procedimento a serem tomados quando pressionado o botao {@code Consultar}
	 *
	 * @param event
	 *            Evento startado pelo produto
	 */
	@FXML
	public void botaoConsultarListener(ActionEvent event) {
		getValueFields();

		Produto Produto = getProdutoNE().obterProduto(this.login);

		if (Produto == null) {
			showAlert("Produto não encontrado");
			return;
		}

		setValueFields(Produto);

	}

	/**
	 * Procedimento a serem tomados quando pressionado uma linha da
	 * {@code Tabela de Produtos}
	 *
	 * @param event
	 *            Evento startado pelo produto
	 */
	@FXML
	private void tabelaProdutoListener(MouseEvent event) {
		// Se foi clicado duas vezes e o item não está nulo então processa a
		// consulta
		if (event.getClickCount() == 2 && tblProduto.getSelectionModel().getSelectedItem() != null) {

			Produto Produto = tblProduto.getSelectionModel().getSelectedItem();

			setValueFields(Produto);
		}
	}

	@FXML
	public void botaoSairListener() {
		menuControl.unloadView();
	}

	/**
	 * Limpar os campos do fomulario de Produtos
	 */
	private void limparCampos() {

		gridForm.getChildren().stream().forEach((c) -> {
			if (c instanceof TextField) {
				((TextField) c).setText("");
			} else if (c instanceof ChoiceBox) {
				((ChoiceBox<?>) c).getSelectionModel().select(1);
			}
		});

		txtProduto.requestFocus();
	}

	/**
	 * Procedimento a serem tomados quando o Produto digitar algum texto no
	 * campo {@code Login}
	 */
	@FXML
	private void txtLoginListener(KeyEvent event) {
		reiniciarListaProduto();
	}

	/**
	 * Obter o texto digitado no {@code Login} e atualizar a lista de Produtos
	 * que possuam os caracteres informados
	 */
	private void reiniciarListaProduto() {
		prodts.setAll(getProdutoNE().listarProdutos(txtLogin.getText()));
	}

	/**
	 * Obter os valores dos componentes do formulario de Produtos
	 */
	private void getValueFields() {
		this.nome = txtProduto.getText();
		this.tipo = cbxTipo.getValue();
		this.unidade = cbxUnidade.getValue();

		//Verificar se o valor digitado é um Double válido
		try {
			this.valor = Double.parseDouble(txtValor.getText());
		} catch (NumberFormatException e) {
			showAlert("O valor informado não é válido");
			txtValor.requestFocus();
		}

	}

	/**
	 * Valorizar os componentes do formuario conforme variaveis internas da
	 * classe ProdutoGUI
	 */
	private void setValueFields(Produto prodt) {
		txtProdID.setText(Integer.toString(prodt.getProdID()));
		txtProduto.setText(prodt.getProduto());
		cbxTipo.setValue(prodt.getTipo());
		cbxUnidade.setValue(prodt.getUnidade());
		txtValor.setText(String.valueOf(prodt.getValor()));
	}

	/**
	 * Validar as informações passadas pelo produto
	 */
	private boolean validarInformacoes() {

		getValueFields();

		if (nome.equals("")) {
			showAlert("Informar o nome do Produto");
			txtProduto.requestFocus();
			return false;
		};
		
		return true;
	}

	/**
	 * Mostrar tela de mensagem para apontar erro ao produto.
	 *
	 * @param msg
	 *            Mensagem que se deseja passar para o produto.
	 */
	private void showAlert(String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Informação Inválida");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
		alert.showAndWait();
	}

	/**
	 * Retorna o objeto instaciado da classe ProdutoNE
	 *
	 * @return ProdutoNE Eh uma instancia da clase ProdutoNE
	 */
	private ProdutoNE getProdutoNE() {
		if (ProdutoNE == null) {
			ProdutoNE = new ProdutoNE();
		}

		return this.ProdutoNE;
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		txtLogin.textProperty().addListener(new LimitedTextListener<String>(txtLogin, 15));
		txtNome.textProperty().addListener(new LimitedTextListener<String>(txtNome, 50));
		txtSenha.textProperty().addListener(new LimitedTextListener<String>(txtSenha, 20));
		txtConfirmSenha.textProperty().addListener(new LimitedTextListener<String>(txtConfirmSenha, 20));

		cbxAcesso.getItems().addAll(AcessoEnum.values());
		cbxAcesso.setValue(AcessoEnum.CADASTRO);

		// Obter a lista inicial dos produtos cadastrados na base de dados.
		prodts = FXCollections.observableArrayList(getProdutoNE().listarProdutos());

		// Formatar a TableView com as informações dos produtos obtidos.
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

		tblProduto.setRowFactory((TableView<Produto> table) -> new TableRow<Produto>() {
			@Override
			protected void updateItem(Produto user, boolean empty) {
				// Sempre chamar o metodo herdado para que a atualiação das
				// linhas ocorra
				// com sucesso:
				super.updateItem(user, empty);

				if (!empty) {
					if (!user.isAtivo()) {
						getStyleClass().add("Produto-inativo");
					} else {
						getStyleClass().removeAll(Collections.singleton("Produto-inativo"));
					}
				} else {
					this.setVisible(false);
					getStyleClass().removeAll(Collections.singleton("Produto-inativo"));
				}
			}
		});

		tblProduto.setItems(prodts);

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
