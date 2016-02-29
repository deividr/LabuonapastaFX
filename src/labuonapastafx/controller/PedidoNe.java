package labuonapastafx.controller;

import labuonapastafx.model.*;
import labuonapastafx.persistencia.PedidoDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class PedidoNe {

    private PedidoDao pedDao;

    public PedidoNe() {
        pedDao = new PedidoDao();
    }

    /**
     * Incluir um pedido.
     *
     * @param usuar Usuário que está efetuando a inclusão do Pedido.
     * @param clie Cliente que está fazendo o Pedido.
     * @param dataRetirada Data em que o Pedido será retirado.
     * @param horaDe Hora inicial em que o Pedido deve ser retirado.
     * @param horaAte Hora final em que o Pedido deve ser retirado.
     * @param itens Lista dos produtos que compõe o pedido.
     * @return
     */
    public boolean incluir(Usuario usuar, Cliente clie, LocalDate dataRetirada, Integer horaDe,
            Integer horaAte, ArrayList<ItemPedido> itens) {
       
        Pedido ped = new Pedido(0, usuar, clie, LocalDate.now(), dataRetirada, horaDe, horaAte, itens);
        
        pedDao.incluir(ped);
        
        return true;
    }

	/**
	 * Obter pedidos que pertence a um determinado {@code Cliente}.
	 *
	 * @param clieId Código do Cliente que se deseja consultar os pedidos.
	 * @return
     */
	public ArrayList<Pedido> obterPedidoCliente(int clieId) {
        return pedDao.obterPedidosCliente(clieId);
	}

    /**
     * Excluir todos os Pedidos do Cliente informado.
     * @param clie Cliente a qual se deseja excluir os Pedidos.
     */
    public void excluirPorCliente(Cliente clie) {

        pedDao.excluirPorCliente(clie);

    }

    /**
     * Efetuar alterações no Pedido conforme passado por parâmetro.
     *
     * @param pedido Pedido que se deseja alterar as informações.
     *
     * @return
     */
    public boolean alterar(Pedido pedido) {

    }
}
