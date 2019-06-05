package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroCriacaoDiretorio;
import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.ExecucaoMultiEtapas;
import br.udesc.ceavi.pin2.utils.GeradorRede;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Controlador para a tela inicial da aplicação.
 *
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João
 * Pedro Schimitz
 */
public class ControleInicial implements IControleInicial {

    // Modelo de simulação carregado do arquivo.
    private File arquivoSimulacao;

    private final List<ObservadorInicial> observadores;
    private ExecucaoMultiEtapas geradorDados;

    /**
     * Cria um novo controlador para realizar o controle inicial da simulação.
     */
    public ControleInicial() {
        this.observadores = new ArrayList<>();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void iniciaCarregamentoNovoArquivo(File arquivo) throws ExtensaoArquivoInvalida {
        SimulacaoMicroscopica.getInstance().log("Aberto novo arquivo: " + arquivo.getName());
        String extensao = FilenameUtils.getExtension(arquivo.getName()).toLowerCase();
        if (!extensao.equals(SimulacaoMicroscopica.EXTENSAO_ARQUIVO)) {
            throw new ExtensaoArquivoInvalida(extensao, SimulacaoMicroscopica.EXTENSAO_ARQUIVO);
        }
        this.arquivoSimulacao = arquivo;
        this.notificaArquivoCarregado(arquivo);
    }

    /**
     * Notifica os observadores que um arquivo foi carregado.
     */
    private void notificaArquivoCarregado(File arquivo) {
        this.observadores.forEach((observador) -> {
            observador.arquivoCarregado(arquivo);
        });
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void iniciaSimulacao() throws LogException {
        SimulacaoMicroscopica.getInstance().log("Iniciando processo de simulação.");
        //tratar o xml do outro grupo
        //TODO 
        //tratarXml();
        this.notificaInicioGeracaoRede();
        this.criaPastaTemporariaArquivo();
        this.geradorDados = new GeradorRede(this.arquivoSimulacao);
        SwingUtilities.invokeLater(() -> {
            if (this.realizaGeracaoDados()) {
                SimulacaoMicroscopica.getInstance().log("Retorno:\n" + this.geradorDados.getRetorno());
                this.notificaSucessoGeracaoRede();
                SimulacaoMicroscopica.getInstance().iniciaSimulacao(null, null);
            } else {
                this.notificaErroGeracaoRede(this.geradorDados.getErro());
            }
        });
    }

    /**
     * Realiza a geração de dados de forma síncrona e retorna se a execução
     * ocorreu com sucesso.
     *
     * @return
     */
    private boolean realizaGeracaoDados() {
        if (this.geradorDados == null) {
            return false;
        }
        new Thread(this.geradorDados).start();
        while (!this.geradorDados.getExecucaoFinalizada()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        return !this.geradorDados.getExecucaoErro();
    }

    /**
     * Realiza a criação das pastas temporárias para armazenar os arquivos.
     *
     * @return
     */
    private void criaPastaTemporariaArquivo() throws LogException {
        SimulacaoMicroscopica.getInstance().log("Criando pasta temporária para os arquivos.");
        SimpleDateFormat sdf = new SimpleDateFormat(SimulacaoMicroscopica.FORMATO_DATA);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        File file = new File("simulacoes/resultados/" + sdf.format(timestamp));
        if (!file.mkdirs()) {
            throw new ErroCriacaoDiretorio(file.getAbsolutePath());
        }
        SimulacaoMicroscopica.getInstance().log("Criada pasta " + file.getAbsolutePath());
        SimulacaoMicroscopica.getInstance().setWorkspaceFolder(file.getPath());
    }

    /**
     * Notifica os observadores que a rede está sendo carregada.
     */
    private void notificaInicioGeracaoRede() {
        this.observadores.forEach((observador) -> {
            observador.inicioGeracaoRede();
        });
    }

    /**
     * Notifica os observadores que a rede foi carregada.
     */
    private void notificaSucessoGeracaoRede() {
        this.observadores.forEach((observador) -> {
            observador.sucessoGeracaoRede();
        });
    }

    /**
     * Notifica os observadores erros no carregamento da rede.
     */
    private void notificaErroGeracaoRede(LogException ex) {
        this.observadores.forEach((observador) -> {
            observador.erroGeracaoRede(ex);
        });
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void anexaObservador(ObservadorInicial observador) {
        this.observadores.add(observador);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void desanexaObservador(ObservadorInicial observador) {
        this.observadores.remove(observador);
    }

    private void tratarXml() {
        //excluir as tags connections
        try {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document document = builder.parse(this.arquivoSimulacao);
        Element root = document.getDocumentElement();
        NodeList nodes = root.getElementsByTagName("conections");
        
        for(int i = 0; i < nodes.getLength(); i++){
            Element e = (Element)nodes.item(i);
            e.getParentNode().removeChild(e);
        }

             XMLSerializer serializer = new XMLSerializer(System.out, new OutputFormat((Document) document,"iso-8859-1",true));
             serializer.serialize(document);
        
        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            Logger.getLogger(ControleInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
       

    }

}
