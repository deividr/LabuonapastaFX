package labuonapastafx.persistencia;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Leitura e Manutenção no arquivo de configurações do sistema.
 *
 * @author Deivid Assumpção Rodrigues
 */
public class ConfigXML {

    private static ConfigXML instance;

    private DocumentBuilder dBuilder;
    private Document doc;
    private File file;

    private ConfigXML() {
        inicializarDocumentBuilder();
        criarXMLConfig();
    }

    /**
     * Obter uma instância.
     *
     * @return Instância de <code>ConfigXML</code>.
     */
    public static ConfigXML getInstance() {
        //Se instancia ainda não existe criar uma.
        if (instance == null)
            instance = new ConfigXML();

        return instance;
    }

    /**
     * Criar arquivo de configuração caso ele não exista.
     */
    private void criarXMLConfig() {

        file = new File("config.xml");

        //Se arquivo já existir faz o parser e retorna.
        if (file.exists()) {
            try {
                doc = dBuilder.parse(file);
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //Se o arquivo não existir, irá criar um novo.
        doc = dBuilder.newDocument();

        // Criar os elementos do XML.
        Element rootElement = doc.createElement("configuracoes");
        Element pedido = doc.createElement("pedido");
        Element inicial = doc.createElement("inicial");
        Element ultimo = doc.createElement("ultimo");

        // Criar a hieraquia entres os elementos.
        doc.appendChild(rootElement);
        rootElement.appendChild(pedido);
        pedido.appendChild(inicial);
        inicial.appendChild(doc.createTextNode("0"));
        pedido.appendChild(ultimo);
        ultimo.appendChild(doc.createTextNode("0"));

        commitAlteracoes();
    }

    /**
     * Efetuar a gravação do arquivo XML em disco.
     */
    public void commitAlteracoes() {

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource domSource = new DOMSource(doc);

            StreamResult result = new StreamResult(file);

            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Obter o <code>text</code> do elemento informado.
     *
     * @param element Elemento que será consultado para obtenção do <code>text</code>.
     * @return String com o conteúdo do elemento informado.
     */
    public String obterXMLTextByElement(String element) {

        doc.getDocumentElement().normalize();

        Element elementXML = (Element) doc.getElementsByTagName(element).item(0);

        return elementXML.getTextContent();

    }

    /**
     * Alterar o <code>text</code> do elemento informado.
     *
     * @param element Elemento que será alterado.
     */
    public void alterarXMLTextByElement(String element, String text) {

        doc.getDocumentElement().normalize();

        Element elementXML = (Element) doc.getElementsByTagName(element).item(0);

        elementXML.setTextContent(text);

    }

    /**
     * Inicializar o DocumentBuilder.
     */
    private void inicializarDocumentBuilder() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
