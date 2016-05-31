package labuonapastafx.controller;

import labuonapastafx.model.*;
import labuonapastafx.persistencia.ConfigXML;
import labuonapastafx.persistencia.PedidoDao;

import java.time.LocalDate;
import java.util.List;

public class PedidoNe {

    private final PedidoDao pedDao;

    public PedidoNe() {
        pedDao = new PedidoDao();
    }

    /**
     * Incluir um pedido.
     *
     * @param pedido Pedido que se deseja incluir na base. Não é necessário
     *               informar o código e nem a data em que foi efetuada o pedido.
     * @return Objeto do tipo Pedido contendo as informações atualizadas.
     */
    public void incluir(Pedido pedido) {

        //Calcular quantos dias serão subtraídos para obter o primeiro
        // dia da semana (Segunda-Feira).
        int dias = pedido.getDataRetirada().getDayOfWeek().getValue() - 1;

        LocalDate primeiroDia = pedido.getDataRetirada().minusDays(dias);

        LocalDate ultimoDia = primeiroDia.plusDays(6);

        /*
         * Sincronizar esse ponto para que não ocorra duplicidade de número de pedidos.
         */
        synchronized (this) {
            int numeroPedido = pedDao.obterUltimoNumeroPedido(java.sql.Date.valueOf(primeiroDia),
                    java.sql.Date.valueOf(ultimoDia));

            //Se ainda não existe pedido para o período informado, então iremos obter
            // qual é o número inicial cadastrado para os pedidos novos.
            if (numeroPedido == 0) {
                numeroPedido = Integer.parseInt(ConfigXML.getInstance()
                        .obterXMLTextByElement("inicial"));
            } else {
                numeroPedido += 1; //Adicionar 1 ao número do pedido.
            }

            pedido.setNumeroPedido(numeroPedido);

            pedDao.incluir(pedido);
        }

    }

    /**
     * Efetuar alterações no Pedido conforme passado por parâmetro.
     *
     * @param pedido Pedido que se deseja alterar as informações. O código do
     *               pedido precisa ser de um pedido que realmente existe na base,
     *               por tanto não deve ser alterado.
     */
    public void alterar(Pedido pedido) {

        /* Calcular quantos dias serão subtraídos para obter o primeiro
         * dia da semana (Segunda-Feira).
         */
        int dias = pedido.getDataRetirada().getDayOfWeek().getValue() - 1;

        LocalDate primeiroDia = pedido.getDataRetirada().minusDays(dias);

        LocalDate ultimoDia = primeiroDia.plusDays(6);

        synchronized (this) {

            /* Verifica se o pedido já existe para o período informado. Dessa forma não corre o
             * risco de incluir o mesmo número em um mesmo perído caso ocorra a mudança da data
             * de retirada.
             */
            boolean existe = pedDao.verificarNumeroPedido(java.sql.Date.valueOf(primeiroDia),
                    java.sql.Date.valueOf(ultimoDia), pedido);

            if (existe) {
                //Se o número já existe para o período informado, cria outro número.
                int numeroPedido = pedDao
                        .obterUltimoNumeroPedido(java.sql.Date.valueOf(primeiroDia),
                                java.sql.Date.valueOf(ultimoDia));

                pedido.setNumeroPedido(numeroPedido);
            }

            pedDao.alterar(pedido);
        }

    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param clie Cliente que se deseja consultar os pedidos.
     * @return List com todos os pedidos pertecentes ao cliente informado.
     */
    public List<Pedido> obterPedidos(Cliente clie) {
        return pedDao.listarPedidos(clie);
    }

    /**
     * Obter pedidos que pertence a um determinado {@code Cliente}.
     *
     * @param date Data dos pedidos.
     * @return Lista de pedidos que atendem ao argumento passado.
     */
    public List<Pedido> obterPedidos(LocalDate date) {
        return pedDao.listarPedidos(date);
    }

    /**
     * Excluir todos os Pedidos do Cliente informado.
     *
     * @param clie Cliente a qual se deseja excluir os Pedidos.
     */
    public void excluirPorCliente(Cliente clie) {

        pedDao.excluir(clie);

    }

    public boolean excluirPedido(Pedido ped) {
        pedDao.excluir(ped);

        return true;
    }
}
