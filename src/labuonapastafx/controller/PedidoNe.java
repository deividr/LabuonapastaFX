package labuonapastafx.controller;

import labuonapastafx.model.*;
import labuonapastafx.persistencia.ClienteDao;
import labuonapastafx.persistencia.PedidoDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    public boolean incluir(Usuario usuar, Cliente clie, Date dataRetirada, Integer horaDe, 
            Integer horaAte, ArrayList<ItemPedido> itens) {
       
        Pedido ped = new Pedido(null, usuar, clie, Calendar.getInstance().getTime(), dataRetirada, 
                horaDe, horaAte, itens);
        
        pedDao.incluir(ped);
        
        return true;
    }
}
