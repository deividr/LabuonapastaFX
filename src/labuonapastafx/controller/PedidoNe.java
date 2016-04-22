package labuonapastafx.controller;

import labuonapastafx.model.*;
import labuonapastafx.persistencia.PedidoDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoNe {

    private final PedidoDao pedDao;

    public PedidoNe() {
        pedDao = new PedidoDao();
    }

    /**
     * Incluir um pedido.
     *
     * @param pedido Pedido que se deseja incluir na base. Não é necessário informar o código e
     *               nem a data em que foi efetuada o pedido.
     * @return
     */
    public Pedido incluir(Pedido pedido) {

        pedido.setPedId(0);
        pedido.setDtPedido(LocalDate.now());
        pedido.setRetirado((byte) 0);

        return pedDao.incluir(pedido);
    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param clie Cliente que se deseja consultar os pedidos.
     * @return List com todos os pedidos pertecentes ao cliente informado.
     */
    public List<Pedido> obterPedidos(Cliente clie) {
        return pedDao.obterPedidos(clie);
    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param date Data dos pedidos.
     * @return
     */
    public List<Pedido> obterPedidos(LocalDate date) {
        return pedDao.obterPedidos(date);
    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @return
     */
    public List<Pedido> obterPedidos() {
        return pedDao.obterPedidos();
    }

    /**
     * Excluir todos os Pedidos do Cliente informado.
     *
     * @param clie Cliente a qual se deseja excluir os Pedidos.
     */
    public void excluirPorCliente(Cliente clie) {

        pedDao.excluir(clie);

    }

    /**
     * Efetuar alterações no Pedido conforme passado por parâmetro.
     *
     * @param pedido Pedido que se deseja alterar as informações. O código do pedido precisa ser de
     *               um pedido que realmente existe na base, por tanto não deve ser alterado.
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
