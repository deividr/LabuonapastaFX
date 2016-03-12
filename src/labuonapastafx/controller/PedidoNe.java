package labuonapastafx.controller;

import labuonapastafx.model.*;
import labuonapastafx.persistencia.PedidoDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
     * @param dtRetirada Data em que o Pedido será retirado.
     * @param horaDe Hora inicial em que o Pedido deve ser retirado.
     * @param horaAte Hora final em que o Pedido deve ser retirado.
     * @param itens Lista dos produtos que compõe o pedido.
     * @return
     */
    public boolean incluir(Usuario usuar, Cliente clie, LocalDate dtRetirada, Integer horaDe,
                           Integer horaAte, String geladeira, List<ItemPedido> itens, String observacao,
                           Byte retirado) {
       
        Pedido ped = new Pedido(0, usuar, clie, LocalDate.now(), dtRetirada, horaDe,
        		horaAte, geladeira, itens, observacao, retirado);
        
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
     * 
     * @param clie Cliente a qual se deseja excluir os Pedidos.
     */
    public void excluirPorCliente(Cliente clie) {

        pedDao.excluirPorCliente(clie);

    }

    /**
     * Efetuar alterações no Pedido conforme passado por parâmetro.
     *
     * @param pedido Pedido que se deseja alterar as informações. O código do pedido precisa ser
     * de um pedido que realmente existe na base, por tanto não deve ser alterado.
     *
     * @return
     */
    public boolean alterar(Pedido pedido) {
    	
    	pedDao.alterar(pedido);
    	
    	return true;
    }

	public boolean excluirPedido(Pedido ped) {
		pedDao.excluir(ped);	
		
		return true;
	}
}
