package labuonapastafx.exception;

/**
 * Exception para indicar o que o número do pedido foi duplicado.
 */
public class NumeroPedidoDuplicadoException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumeroPedidoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
