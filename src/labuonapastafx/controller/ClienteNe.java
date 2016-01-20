package labuonapastafx.controller;

import java.util.ArrayList;
import labuonapastafx.model.Cliente;
import labuonapastafx.persistencia.ClienteDao;

public class ClienteNe {

	private ClienteDao clienteDao;

	public ClienteNe() {
		clienteDao = new ClienteDao();
	}

	/**
	 * Obter o {@code Cliente} que corresponde ao código do cliente informado.
	 *
	 * @param clieId
	 *            a qual se deseja pesquisar
	 * @return retorna objeto {@code Cliente} correspondente ao nome informado
	 */
	public Cliente obterClienteId(int clieId) {

		return clienteDao.lerId(clieId);

	}

	/**
	 * Obter o {@code Cliente} que corresponde ao nome informado.
	 *
	 * @param nome
	 *            a qual se deseja pesquisar
	 * @return retorna objeto {@code Cliente} correspondente ao nome informado
	 */
	public Cliente obterClienteNome(String nome) {

		return clienteDao.lerNome(nome);

	}

	/**
	 * Obter o {@code Cliente} que corresponde ao nome informado.
	 *
	 * @param telefone
	 *            a qual se deseja pesquisar
	 * @return retorna objeto {@code Cliente} correspondente ao nome informado
	 */
	public Cliente obterClienteTelefone(String telefone) {

		return clienteDao.lerTelefone(telefone);

	}

	/**
	 * Incluir um {@code Cliente}.
	 *
	 * @param nome
	 *            Nome do cliente.
	 * @param telefone
	 *            Telefone do cliente.
	 * @param endereco
	 *            Endereço do cliente.
	 * @return True se a inclusão ocorreu com sucesso, False se ocorreu algum
	 *         erro.
	 */
	public boolean incluirCliente(String nome, String telefone1, String telefone2, String email,
			String endereco) {

		// Se o cliente nao existir faca a inclusao
		if (obterClienteNome(nome) == null) {
			Cliente cliente = new Cliente(0, nome, telefone1, telefone2, email, endereco, null);
			clienteDao.incluir(cliente);
			return true;
		} else {
			// Se o cliente jah existir o retorno serah false
			return false;
		}

	}

	/**
	 * Atualizar as informações do {@code Cliente} que foi passado como
	 * parâmetro.
	 *
	 * @param clieId
	 *            Código do cliente
	 * @param nomeNovo
	 *            Nome do cliente
	 * @param telefone
	 *            Telefone do cliente
	 * @param endereco
	 *            Endereco do cliente
	 *
	 * @return True se atualizado com sucesso, False se houve erro.
	 */
	public boolean alterarCliente(int clieId, String nomeNovo, String telefone, String endereco) {

		Cliente clie = obterClienteId(clieId);

		// se o cliente existir atualiza, senão retorna false para o chamador
		if (clie != null) {
			clie.setNome(nomeNovo);
			clie.setTelefone1(telefone);
			clie.setEndereco(endereco);

			clienteDao.atualizar(clie);

			// retorna que a atualização foi ok
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Excluir o {@code Cliente} pelo ID do cliente.
	 *
	 * @param clieId
	 *            do {@code Cliente} que se deseja excluir
	 * @return True se exclusão foi ok, ou False se ocorreu algum erro
	 */
	public boolean excluirId(int clieId) {

		if (obterClienteId(clieId) != null) {
			clienteDao.excluirId(clieId);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Excluir o {@code Cliente} pelo telefone do cliente.
	 *
	 * @param telefone
	 *            Telefone do {@code Cliente} que se deseja excluir
	 * @return True se exclusão foi ok, ou False se ocorreu algum erro
	 */
	public boolean excluirTelefone(String telefone) {

		if (obterClienteTelefone(telefone) != null) {
			clienteDao.excluirTelefone(telefone);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Excluir o {@code Cliente} pelo nome do cliente.
	 *
	 * @param nome
	 *            do {@code Cliente} que se deseja excluir
	 * @return True se exclusão foi ok, ou False se ocorreu algum erro
	 */
	public boolean excluirNome(String nome) {

		if (obterClienteNome(nome) != null) {
			clienteDao.excluirNome(nome);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Retorna uma lista dos clientes cadastrados na base de dados.
	 *
	 * @return Lista de clientes.
	 */
	public ArrayList<Cliente> listarClientes() {

		return clienteDao.listar();

	}

	/**
	 * Retorna uma lista dos clientes que começam com a informação passa por
	 * parâmetro.
	 *
	 * @param nome
	 *            do cliente que se deseja usar como filtro.
	 * @return Lista de clientes.
	 */
	public ArrayList<Cliente> listarClientes(String nome) {

		return clienteDao.listar(nome);

	}

}
