package labuonapastafx.controller;

import java.util.ArrayList;
import labuonapastafx.model.Cliente;
import labuonapastafx.persistencia.ClienteDao;

public class ClienteNe {

    private final ClienteDao clienteDao;

    public ClienteNe() {
        clienteDao = new ClienteDao();
    }

    /**
     * Obter o {@code Cliente} que corresponde ao código do cliente informado.
     *
     * @param cdCliente a qual se deseja pesquisar
     * @return retorna objeto {@code Cliente} correspondente ao nome informado
     */
    public Cliente obterCodCliente(int cdCliente) {

        return clienteDao.lerCodCliente(cdCliente);

    }

    /**
     * Obter o {@code Cliente} que corresponde ao nome informado.
     *
     * @param nome a qual se deseja pesquisar
     * @return retorna objeto {@code Cliente} correspondente ao nome informado
     */
    public Cliente obterClienteNome(String nome) {

        return clienteDao.lerNome(nome);

    }

    /**
     * Obter o {@code Cliente} que corresponde ao nome informado.
     *
     * @param telefone Telefone principal que se deseja consultar.
     * @return retorna objeto {@code Cliente} correspondente ao nome informado
     */
    public Cliente obterClienteTelefone(String telefone) {

        return clienteDao.lerTelefone(telefone);

    }

    /**
     * Incluir um {@code Cliente}.
     *
     * @param nome Nome do cliente.
     * @param telefone1 Telefone principal do cliente.
     * @param telefone2 Telefone secundário do cliente.
     * @param email Email do cliente.
     * @param endereco Endereço do cliente.
     * @return True se a inclusão ocorreu com sucesso, False se o Cliente já existe na base.
     */
    public boolean incluirCliente(String nome, String telefone1, String telefone2, String email,
            String endereco) {

        boolean temNome = obterClienteNome(nome) != null;
        boolean temTelefone1 = obterClienteTelefone(telefone1) != null;
        boolean temTelefone2 = false;

        //Se o telefone2 foi informado, consultar se já existe cadastro pra ele.
        if (!telefone2.equals("")) {
            temTelefone2 = obterClienteTelefone(telefone2) == null;
        }

        // Se o cliente não existir faça a inclusão
        if (!temNome && !temTelefone1 && !temTelefone2) {
            Cliente cliente = new Cliente(0, nome, telefone1, telefone2, email, endereco, null);
            clienteDao.incluir(cliente);
            return true;
        } else {
            // Se o cliente já existir o retorno será false
            return false;
        }

    }

    /**
     * Atualizar as informações do {@code Cliente} que foi passado como parâmetro.
     *
     * @param cdCliente Código do cliente
     * @param nome Nome do cliente
     * @param telefone1 Telefone princial do cliente.
     * @param telefone2 Telefone secundário do cliente.
     * @param email Email do cliente.
     * @param endereco Endereco do cliente
     *
     * @return True se atualizado com sucesso, False se houve erro.
     */
    public boolean alterarCliente(int cdCliente, String nome, String telefone1, String telefone2,
            String email, String endereco) {

        Cliente clie = obterCodCliente(cdCliente);
        Cliente clie2 = obterClienteTelefone(telefone1);
        Cliente clie3 = null;

        //Se o telefone secundário foi informado verificar se já existe para outro usuário.
        if (!telefone2.equals("")) {
            clie3 = obterClienteTelefone(telefone2);
        }

        if (clie != null) {

            // Se o cliente existir e o telefone alterado pertence a outro Cliente, atualiza. 
            // Senão retorna false para o chamador.
            if ((clie2 != null && clie.getClieId() != clie2.getClieId())
                    || (clie3 != null && clie.getClieId() != clie3.getClieId())) {
                return false;
            }

            clie.setNome(nome);
            clie.setTelefone1(telefone1);
            clie.setTelefone2(telefone2);
            clie.setEmail(email);
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
     * @param cdCliente do {@code Cliente} que se deseja excluir
     * @return True se exclusão foi ok, ou False se ocorreu algum erro
     */
    public boolean excluir(int cdCliente) {

        if (obterCodCliente(cdCliente) != null) {
            clienteDao.excluir(cdCliente);
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
     * Retorna uma lista dos clientes que começam com a informação passada por parâmetro.
     *
     * @param nome do cliente que se deseja usar como filtro.
     * @return Lista de clientes.
     */
    public ArrayList<Cliente> listarClientes(String nome) {

        return clienteDao.listar(nome);

    }

    /**
     * Retorna uma lista dos clientes que começam com a informação passada por parâmetro.
     *
     * @param telefone do cliente que se deseja usar como filtro.
     * @return Lista de clientes.
     */
    public ArrayList<Cliente> listarClientesTelefone(String telefone) {

        return clienteDao.listarTelefone(telefone);

    }

}
