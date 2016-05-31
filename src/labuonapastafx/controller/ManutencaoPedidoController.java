package labuonapastafx.controller;

import br.com.daruma.jna.DUAL;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import labuonapastafx.LabuonapastaFX;
import labuonapastafx.model.*;
import labuonapastafx.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe controladora do painel de manutenção dos Pedidos.
 *
 * @author Deivid Assumpção Rodrigues
 * @version %I%, %G%
 * @since 1.0
 */
public class ManutencaoPedidoController extends StackPane implements Initializable {

	static final String VIEW_INCLUIR = "view/IncluirPedido.fxml";
	static final String VIEW_ALTERAR = "view/AlterarPedido.fxml";

	private static Stage window;

	@FXML
	private GridPane gridForm;
	@FXML
	private TextField txtNumPed;
	@FXML
	private TextField txtTelefone;
	@FXML
	private TextField txtNome;
	@FXML
	private DatePicker dtpickRetirada;
	@FXML
	private TextField txtHoraDe;
	@FXML
	private TextField txtHoraAte;
	@FXML
	private TextField txtGeladeira;
	@FXML
	private ComboBox<Produto> cbxProduto;
	@FXML
	private ComboBox<Produto> cbxMolho;
	@FXML
	private TextField txtQtde;
	@FXML
	private TextArea txtObservacoes;
	@FXML
	private ListView<String> listItens;
	@FXML
	private Label lblTotal;
	@FXML
	private Button btnIncluir;
	@FXML
	private Button btnAlterar;
	@FXML
	private Button btnExcluir;

	// Variáveis de controle geral
	private PedidoController pedidoController;
	private ClienteNe clieNe;
	private PedidoNe pedidoNe;
	private List<ItemPedido> itens;

	// Variáveis para controle do formulário da tela.
	private String telefone, nome, horaDe, horaAte, geladeira, observacao;
	private LocalDate dtRetirada;
	private Produto produto, molho;
	private BigDecimal qtde, total = BigDecimal.ZERO;

	/**
	 * Construtor privado para garantir apenas uma instancia desse objeto.
	 *
	 * @param pedidoController
	 *            Objeto PedidoController para obtenção de valores comum.
	 * @param fxml
	 *            Caminho do .fxml que será controlado.
	 * @return Retorna um objeto PedidoController.
	 */
	public static ManutencaoPedidoController getInstance(PedidoController pedidoController, String fxml) {

		window = new Stage();

		FXMLLoader loader = new FXMLLoader();

		StackPane page;

		try (InputStream in = LabuonapastaFX.class.getResourceAsStream(fxml)) {
			loader.setBuilderFactory(new JavaFXBuilderFactory());
			loader.setLocation(LabuonapastaFX.class.getResource(fxml));

			page = loader.load(in);

			Scene scene = new Scene(page);

			window.setScene(scene);
			window.initModality(Modality.WINDOW_MODAL);
			window.setResizable(false);
			window.initOwner(LabuonapastaFX.getStage());
			window.centerOnScreen();
			window.getIcons().add(
					new Image(pedidoController.getClass().getResource("../view/imagens/brasao_menu.png").toString()));

			window.show();

		} catch (IOException e) {
			e.printStackTrace();

		}

		ManutencaoPedidoController manutencaoPedidoController = loader.getController();

		manutencaoPedidoController.pedidoController = pedidoController;

		manutencaoPedidoController.setApp();

		// Se iniciou a tela de alteração e exclusão, inicializar as informações
		// do formulário.
		if (fxml.equals(VIEW_ALTERAR)) {
			manutencaoPedidoController.setValueFields(pedidoController.getPedidoSel());
		}

		return manutencaoPedidoController;
	}

	/**
	 * Incluir um novo item na lista de itens, esse evento será acionado pelao
	 * botão "+".
	 *
	 * @param event
	 *            Ação feita no botão de inclusão de item na lista.
	 */
	@FXML
	void incluirItemListener(ActionEvent event) {

		getValuesItemPedido();

		if (produto == null) {
			showAlertWarning("Informar um produto para incluir.");
			cbxProduto.requestFocus();
		} else if (qtde.doubleValue() == 0.0) {
			showAlertWarning("Informar a quantidade do produto.");
			txtQtde.requestFocus();
		} else {

			int codigo;

			if (itens.isEmpty()) {
				codigo = 1;
			} else {
				codigo = itens.get(itens.size() - 1).getCodigo() + 1;
			}

			ItemPedido item = new ItemPedido(codigo, produto, molho, qtde);

			// Adicionar o item na ListView da tela.
			addItemList(item);

			// Adidionar o item no array List de ItemPedido.
			itens.add(item);

			// Obter valor do item multiplicando a quantidade pelo seu valor
			// unitário cadastrado.
			BigDecimal valor = item.getProduto().getValor().multiply(item.getQtde());

			total = total.add(valor);

			setLblTotal();

			cbxProduto.getSelectionModel().clearSelection();

			cbxMolho.getSelectionModel().clearSelection();

			txtQtde.setText("");

			cbxProduto.requestFocus();
		}

	}

	/**
	 * Excluir um item da lista de pedidos do cliente.
	 *
	 * @param event
	 *            Ação feita no botão de exclusão de item da lista.
	 */
	@FXML
	void excluirItemListener(ActionEvent event) {

		int index = listItens.getSelectionModel().getSelectedIndex();

		// Obter valor do item multiplicando a quantidade pelo seu valor
		// unitário cadastrado.
		BigDecimal valor = itens.get(index).getProduto().getValor().multiply(itens.get(index).getQtde());

		total = total.subtract(valor);

		setLblTotal();

		itens.remove(index);

		listItens.getItems().clear();

		int codigo = 1;

		for (ItemPedido item : itens) {
			item.setCodigo(codigo++);
			addItemList(item);
		}

	}

	/**
	 * Formatar o campo de total.
	 */
	private void setLblTotal() {
		lblTotal.setText(String.format("%8.2f", total.floatValue()));
	}

	/**
	 * Formatar e incluir um novo item na lista de itens do pedido.
	 *
	 * @param item
	 *            Item para inclusão na lista de itens.
	 */
	private void addItemList(ItemPedido item) {

		String massaMaisMolho = String.format("%s %s", item.getProduto().getNome(),
				item.getMolho() != null ? item.getMolho().getNome() : "");

		/*
		 * Formato da string:
		 *
		 * ITEM QTDE UN DESCRICAO VALOR 999 999.999 kg
		 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 9999,99
		 *
		 */
		String strFormat = String.format("%3s  %7s %s  %-30s  %7.2f", item.getCodigo(), item.getQtde().toString(),
				item.getProduto().getUnidade().getCodigo().toLowerCase(), massaMaisMolho,
				item.getProduto().getValor().multiply(item.getQtde()));

		listItens.getItems().add(strFormat);

	}

	/**
	 * Incluir o pedido na base quando o botão Incluir for pressionado.
	 *
	 * @param event
	 *            Evento disparado para esse método, no caso, acionado o botão
	 *            Incluir.
	 */
	@FXML
	void botaoIncluirListener(ActionEvent event) {
		// Se as informações foram preenchidas corretamente, faz a inclusão na
		// base de pedidos.
		if (validateFields()) {

			Cliente clie = clieNe.obterClienteTelefone(telefone);

			// Se não existe o cliente cadastrado na base será feito a inclusão
			// dos dados básicos.
			if (clie == null) {
				clieNe.incluirCliente(nome, telefone, "", "", "");
				clie = clieNe.obterClienteTelefone(telefone);
			}

			Pedido pedido = new Pedido().setUsuario(LabuonapastaFX.user).setCliente(clie).setDataPedido(LocalDate.now())
					.setDataRetirada(dtRetirada).setHoraDe(horaDe).setHoraAte(horaAte).setGeladeira(geladeira)
					.setObservacao(observacao).setItens(FXCollections.observableList(itens));

			try {
				pedidoNe.incluir(pedido);
			} catch (RuntimeException e) {
				showAlertWarning(e.getMessage());
				return;
			}

			// Informar a inclusão do pedido e o seu número para o usuário.
			showAlertInformation("Inclusão do Pedido efetuada com sucesso.", "Pedido " + pedido.getNumeroPedido());

			txtTelefone.requestFocus();

			// Adiciona o novo pedido a tabela de pedidos.
			pedidoController.adicionarPedido(pedido);

			imprimirCupom(pedido);

			limparCampos();

		}
	}

	/**
	 * Alterar o pedido nas bases quando o botão Alterar for acionado.
	 *
	 * @param event
	 *            Ação de clicar no botão de alteração.
	 */
	@FXML
	void botaoAlterarListener(ActionEvent event) {
		// Se as informações foram preenchidas corretamente, faz a alteração na
		// base de pedidos.
		if (validateFields()) {

			Pedido pedido = new Pedido().setPedId(pedidoController.getPedidoSel().getPedId())
					.setNumeroPedido(pedidoController.getPedidoSel().getNumeroPedido())
					.setCliente(pedidoController.getPedidoSel().getCliente()).setUsuario(LabuonapastaFX.user)
					.setDataPedido(pedidoController.getPedidoSel().getDataPedido()).setDataRetirada(dtRetirada)
					.setHoraDe(horaDe).setHoraAte(horaAte).setGeladeira(geladeira).setObservacao(observacao)
					.setItens(FXCollections.observableList(itens));

			/*
			 * Tenta efetuar a alteração caso ocorra erro emite mensagem para o
			 * usuário.
			 */
			try {
				pedidoNe.alterar(pedido);
			} catch (RuntimeException e) {
				showAlertWarning(e.getMessage());
				return;
			}

			pedidoController.atualizarPedido(pedido);

			imprimirCupom(pedido);

			// Informar a inclusão do pedido e o seu número para o usuário.
			showAlertInformation("Alteração do Pedido efetuada com sucesso.",
					"Pedido " + pedidoController.getPedidoSel().getNumeroPedido());

			sair();
		}
	}

	/**
	 * Formatar e imprimir o cupom do <code>Pedido</code>.
	 * 
	 * @param pedido
	 *            Informações do Pedido a ser impresso.
	 */
	private void imprimirCupom(Pedido pedido) {

		StringBuilder strTexto = new StringBuilder("<e><b><ce>La Buonapasta</ce></b></e>\n");

		strTexto.append("------------------------------------------------\n");
		strTexto.append("<b>Data:</b> <dt></dt>  <b>Hora:</b> <hr></hr>\n");
		strTexto.append("<b>Cliente:</b> " + pedido.getCliente().getNome() + "\n");

		strTexto.append("<b>Telefone:</b> "
				+ pedido.getCliente().getTelefone1()
                .replaceAll("([0-9]{2})([0-9]{4,5})([0-9]{4})", "($1)$2-$3") + "\n");

		strTexto.append("------------------------------------------------\n");
		strTexto.append("<b>NRO.PEDIDO:</b> " + pedido.getNumeroPedido() + "\n");

		if (!pedido.getGeladeira().equals(""))
			strTexto.append("<b>GELADEIRA.:</b> " + pedido.getGeladeira() + "\n");

		strTexto.append("<b>RETIRADA..:</b> "
				+ pedido.getDataRetirada()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");

		if (!pedido.getHoraDe().equals(""))
			strTexto.append("<b>HORARIO...:</b> "
                    + pedido.getHoraDe().replaceAll("[0-9]{2}[0-9]{2}", "$1:$2") + " as "
					+ pedido.getHoraAte().replaceAll("[0-9]{2}[0-9]{2}", "$1:$2") + "\n");

		strTexto.append("------------------------------------------------\n");
		strTexto.append("<c>Item    Qtde.     Descricao                          Valor</c>\n");
		strTexto.append("<c>----------------------------------------------------------</c>\n");
		/* III QQQQQQQ UU DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD 9.999.99 */

		pedido.getItens().forEach(item -> {

			String massaMaisMolho = String.format("%s %s", item.getProduto().getNome(),
					item.getMolho() != null ? item.getMolho().getNome() : "");

			String strFormat = String.format(" %3s  %7s %s  %-30s  %,8.2f",
                    item.getCodigo(),
                    item.getQtde().toString(),
					item.getProduto().getUnidade().getCodigo().toLowerCase(),
                    massaMaisMolho,
					item.getProduto().getValor().multiply(item.getQtde()));

			strTexto.append("<c>" + strFormat + "</c>" + "\n");

		});

        strTexto.append("<c>                  ----------------------------------------</c>\n");

		String strTotal =
				String.format("                  TOTAL......................: R$ %,8.2f", total);

        strTexto.append("<c><b>" + strTotal + "</b></c>\n");

		// Se existir observações imprime:
		if (!pedido.getObsercao().equals("")) {
			strTexto.append("<l></l>");
			strTexto.append("<b>OBSERVACOES:</b> " + pedido.getObsercao());
		}

		strTexto.append("<sl>2</sl>");

		strTexto.append("<ce><ean13>" + String.format("12345678%04d",pedido.getNumeroPedido())
				+ "</ean13></ce>");

		strTexto.append("<sl>3</sl>");
		strTexto.append("<gui></gui>");

		System.out.println(strTexto);

		int iRetorno = DUAL.iImprimirTexto(strTexto.toString(), strTexto.length());

		if (iRetorno != 1)
			showAlertWarning("Erro ao efetuar a impressão do cupom, verifique a impressora e " 
					+ "tente novamente.");

	}

	@FXML
	void botaoExcluirListener(ActionEvent event) {
		if (pedidoNe.excluirPedido(pedidoController.getPedidoSel())) {
			pedidoController.removerPedido();

			// Informar a inclusão do pedido e o seu número para o usuário.
			showAlertInformation("Exclusão do Pedido efetuada com sucesso.",
					"Pedido " + pedidoController.getPedidoSel().getNumeroPedido());

			sair();
		} else {
			showAlertWarning("Pedido não encontrado, favor confirmar as informações.");
		}
	}

	@FXML
	void botaoLimparListener(ActionEvent event) {
		limparCampos();
	}

	@FXML
	void limparCampos() {

		gridForm.getChildren().stream().forEach((c) -> {
			if (c instanceof TextField) {
				((TextField) c).setText("");
			} else if (c instanceof ComboBox) {
				((ComboBox<?>) c).getSelectionModel().clearSelection();
			} else if (c instanceof DatePicker) {
				((DatePicker) c).setValue(LocalDate.now());
			} else if (c instanceof TextArea) {
				((TextArea) c).clear();
			} else if (c instanceof ListView) {
				((ListView<?>) c).getItems().clear();
			}
		});

		lblTotal.setText("0,00");

		total = BigDecimal.ZERO;

		txtTelefone.requestFocus();

		itens.clear();

	}

	@FXML
	void botaoSairListener(ActionEvent event) {

		sair();

	}

	@FXML
	private void sair() {

		/*
		 * Como essa tela não sai de memória enquanto enquanto a
		 * PedidoController estiver ativa, temos que limpar os campos para que
		 * no retorno não venha nenhum tipo de sujeira das alterações
		 * anteriores.
		 */
		limparCampos();

		window.close();
	}

	/**
	 * Método para validar os campos da tela.
	 */
	private boolean validateFields() {
		getValuesWindow();

		if (telefone.equals("")) {
			showAlertWarning("Informar o telefone do Cliente");
			txtTelefone.requestFocus();
			return false;
		} else if (telefone.length() < 10) {
			showAlertWarning("Número de telefone inválido");
			txtTelefone.requestFocus();
			return false;
		}

		if (nome.equals("")) {
			showAlertWarning("Informar o nome do Cliente");
			txtNome.requestFocus();
			return false;
		}

		if (dtRetirada.isBefore(LocalDate.now())) {
			showAlertWarning("Data inválida. Favor informar data de hoje ou posterior.");
			dtpickRetirada.requestFocus();
			return false;
		}

		if (!horaDe.equals("")) {
			if (!horaDe.matches("[0-9]{2}:[0-9]{2}")) {
				showAlertWarning("Hora De inválida.");
				txtHoraDe.requestFocus();
				return false;
			}

			try {
				LocalTime.of(Integer.parseInt(horaDe.substring(0, 2)), Integer.parseInt(horaDe.substring(3, 5)));
			} catch (DateTimeException e) {
				showAlertWarning("Hora De inválida.");
				txtHoraDe.requestFocus();
				return false;
			}
		}

		if (!horaAte.equals("")) {
			if (!horaAte.matches("[0-9]{2}:[0-9]{2}")) {
				showAlertWarning("Hora Até inválida.");
				txtHoraAte.requestFocus();
				return false;
			}

			try {
				LocalTime.of(Integer.parseInt(horaAte.substring(0, 2)), Integer.parseInt(horaAte.substring(3, 5)));
			} catch (DateTimeException e) {
				showAlertWarning("Hora Até inválida.");
				txtHoraAte.requestFocus();
				return false;
			}
		}

		if (itens.isEmpty()) {
			showAlertWarning("Incluir ao menos um item para o pedido.");
			cbxProduto.requestFocus();
			return false;
		}

		return true;

	}

	/**
	 * Setar as informações do formulário com os dados do Pedido informado.
	 *
	 * @param ped
	 *            Pedido que se deseja formatar no formulário do cadastro.
	 */
	private void setValueFields(Pedido ped) {
		txtNumPed.setText(ped.getNumeroPedido().toString());
		txtTelefone.setText(ped.getCliente().getTelefone1());
		txtNome.setText(ped.getCliente().getNome());
		dtpickRetirada.setValue(ped.getDataRetirada());

		if (!ped.getHoraDe().equals("")) {
			txtHoraDe.setText(ped.getHoraDe());
		}

		if (!ped.getHoraAte().equals("")) {
			txtHoraAte.setText(ped.getHoraAte());
		}

		if (!ped.getGeladeira().equals("")) {
			txtGeladeira.setText(ped.getGeladeira());
		}

		listItens.getItems().clear();
		itens.clear();

		for (ItemPedido item : ped.getItens()) {
			addItemList(item);
			itens.add(item);
			// Soma valor do item ao valor total:
			total = total.add(item.getQtde().multiply(item.getProduto().getValor()));
		}

		setLblTotal();

		if (!ped.getObsercao().equals("")) {
			txtObservacoes.setText(ped.getObsercao());
		}
	}

	/**
	 * Método para obter os valores dos campos referentes a inclusão do Pedido.
	 */
	private void getValuesWindow() {
		this.telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
		this.nome = txtNome.getText();
		this.dtRetirada = dtpickRetirada.getValue();
		this.horaDe = txtHoraDe.getText();
		this.horaAte = txtHoraAte.getText();
		this.geladeira = txtGeladeira.getText();
		this.observacao = txtObservacoes.getText();
	}

	/**
	 * Método para obter os valores dos campos referentes aos Itens do Pedido.
	 */
	private void getValuesItemPedido() {
		this.produto = cbxProduto.getValue();

		this.molho = cbxMolho.getValue();

		if (!txtQtde.getText().equals("")) {
			this.qtde = new BigDecimal(txtQtde.getText().replace(",", "."));
		} else {
			this.qtde = BigDecimal.valueOf(0.0);
		}
	}

	/**
	 * Mostrar tela de mensagem para apontar erro ao <code>usuário</>.
	 *
	 * @param msg
	 *            Mensagem que se deseja passar para o usuário.
	 */
	private void showAlertWarning(String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Cadastro de Pedido");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
				.add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
		alert.showAndWait();
	}

	/**
	 * Mostrar tela de mensagem para apontar erro ao <code>usuário</>.
	 *
	 * @param msg
	 *            Mensagem que se deseja passar para o usuário.
	 */
	private void showAlertInformation(String header, String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Cadastro de Pedido");
		alert.setHeaderText(header);

		Label lblNumeroPedido = new Label(msg);

		lblNumeroPedido.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold");
		lblNumeroPedido.setAlignment(Pos.CENTER);

		alert.getDialogPane().setContent(lblNumeroPedido);

		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
				.add(new Image("/labuonapastafx/view/imagens/brasao_back.png"));
		alert.showAndWait();
	}

	/**
	 * Inicializar a classe de controle.
	 *
	 * @param url
	 *            Sei lá, descrição apenas para tirar o warning.
	 * @param rb
	 *            Sei lá, descrição apenas para tirar o warning.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		clieNe = new ClienteNe();

		pedidoNe = new PedidoNe();

		itens = new ArrayList<>();

		dtpickRetirada.setValue(LocalDate.now());

		carregarListeners();

		Platform.runLater(() -> txtTelefone.requestFocus());

	}

	/**
	 * Preparar os combos de Produtos e Molhos.
	 */
	private void carregarCombosProdutos() {

		cbxProduto.getItems().setAll(pedidoController.getProdutos());

		// Criar um tipo de produto null para o caso de não ser selecionado
		// nenhum molho.
		cbxMolho.getItems().add(new Produto());

		// Adciona todos os produtos do tipo molho ao ComboBox de Molhos.
		cbxMolho.getItems().addAll(pedidoController.getMolhos());

		// Converter a String escrita em um objeto produto da lista.
		cbxProduto.setConverter(new StringConverter<Produto>() {

			@Override
			public String toString(Produto produto) {
				if (produto == null) {
					return "";
				}
				return produto.getNome();
			}

			@Override
			public Produto fromString(String string) {
				if (!pedidoController.getMapProdutos().containsKey(string)) {
					cbxProduto.setValue(null);
					return null;
				}

				return pedidoController.getMapProdutos().get(string);
			}

		});

		new AutoCompleteComboBoxListener<>(cbxProduto);

	}

	/**
	 * Método responsável por fazer o carregamento de todos os Listener's de
	 * controle da tela.
	 */
	private void carregarListeners() {

		txtTelefone.textProperty().addListener(new FoneFieldListener(txtTelefone));

		txtTelefone.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {

				getValuesWindow();

				// Se telefone foi informado, consultar se está cadastrado para
				// algum cliente.
				if (!this.telefone.equals("")) {
					Cliente clie = clieNe.obterClienteTelefone(telefone);
					if (clie != null) {
						txtNome.setText(clie.getNome());
					} else {
						txtNome.setText("");
					}
				}
			}
		});

		txtNome.textProperty().addListener(new LimitedTextListener(txtNome, 40));
		txtHoraAte.textProperty().addListener(new HoraFieldListener(txtHoraAte));
		txtHoraDe.textProperty().addListener(new HoraFieldListener(txtHoraDe));
		txtGeladeira.textProperty().addListener(new LimitedTextListener(txtGeladeira, 3));

		// Limitar a 200 caracteres as informações do campo Observação:
		txtObservacoes.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 200) {
				txtObservacoes.setText(oldValue);
			} else {
                txtObservacoes.setText(newValue.replaceAll("[^a-zA-Z0-9 .?!+\\-%&]", ""));
            }
		});

		txtQtde.textProperty().addListener(new QuantityFieldListener(txtQtde));

		cbxProduto.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Se o produto selecionado não for uma massa será inibido o combo
			// de molho, pois molho
			// só pode ser selecionado quando o produto principal for uma massa.
			if (newValue != null && newValue.getTipo() != ProdutoEnum.MASSA) {
				cbxMolho.setValue(null);
				cbxMolho.setDisable(true);
			} else {
				cbxMolho.setDisable(false);
			}
		});

	}

	/**
	 * Efetuar configurações iniciais para o controlador.
	 */
	public void setApp() {

		carregarCombosProdutos();

	}

}
