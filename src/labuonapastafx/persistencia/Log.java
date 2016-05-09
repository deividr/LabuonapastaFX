package labuonapastafx.persistencia;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Efetuar gravação de log para os erros que não são de lógica do negócio.
 */
public class Log {

    private Log() {

    }

    /**
     * Gravar o log usando a API nativa Logger do Java.
     *
     * @param className Nome da Classe onde ocorreu a excessão.
     * @param methodName Método onde ocorreu a excessão.
     * @param menssagem Qual a mensagem que se deseja logar.
     * @param ex Exception que foi capturada.
     */
    public static void logar(String className, String methodName, String menssagem, Exception ex) {

        try {
            //Logar informações em arquivo XML. O log será anexado aos que já existem.
            Logger logger = Logger.getLogger("Testando a digitação de qualquer coisa!");

            logger.addHandler(new FileHandler("log.txt", true));

            logger.logp(Level.SEVERE, className, methodName, menssagem, ex);
        } catch (IOException e) {
            throw new RuntimeException("Erro na gravação do arquivo de LOG.");
        }

    }

}
