package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroGeracaoArquivoXML;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe responsável por gerar os arquivos de rede de tráfego.
 *
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class GeradorRede extends ExecucaoMultiEtapas {

    private final File arquivoSimulacao;
    private final File pastaSimulacao;

    /**
     * Cria um novo controle para geração do arquivo da rede
     *
     * @param arquivoSimulacao
     */
    public GeradorRede(File arquivoSimulacao) {
        this.arquivoSimulacao = arquivoSimulacao;
        this.pastaSimulacao = new File(SimulacaoMicroscopica.getInstance().getWorkspaceFolder());
    }

    /**
     * {@inheritdoc}
     */
    @Override
    protected void executaEtapa(int etapa) {
        switch (etapa) {
            case 1:
                this.criaRedeTrafego();
                break;
            case 2:
                this.criarArquivoDePOI();
                break;
            case 3:
                this.criarArquivoDeTrafego();
                break;
            case 4:
                this.criarArquivoSimulacao();
                break;
        }
    }

    /**
     * Realiza a criação dos arquivos da rede de tráfego. Artigo p.18
     * "Converting from an OSM ﬁle to a SUMO .net.xml ﬁle"
     */
    private void criaRedeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
                "netconvert --osm-files " + this.arquivoSimulacao.getAbsolutePath()
                + " --geometry.remove --roundabouts.guess --ramps.guess --junctions.join --tls.guess-signals --tls.discard-simple --tls.join -o "
                + this.pastaSimulacao.getAbsolutePath() + File.separator + "rede.net.xml"
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().log("Arquivo de rede de tráfego gerado.");
    }

    /**
     * Cria o arquivo de pontos de interesse para a rede. Artigo p.20 "3.2 The
     * Network Topolog"
     */
    private void criarArquivoDePOI() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de pontos de interesse.");
        File polygonUtil = new File(SimulacaoMicroscopica.getInstance().trataEnderecoArquivo("src/br/udesc/ceavi/pin2/utils/osmPolyconvert.typ.xml"));
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
                "polyconvert --net-file " + this.pastaSimulacao.getAbsolutePath() + File.separator
                + "rede.net.xml --osm-files " + this.arquivoSimulacao.getAbsolutePath() + " --type-file "
                + polygonUtil.getAbsolutePath() + " -o " + this.pastaSimulacao.getAbsolutePath() + File.separator + "poi.net.xml"
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().log("Arquivo de pontos de interesse gerado.");
    }

    /**
     * Cria o arquivo de trafégo. Artigo p.21 "3.3.1 Specifying Random Trafﬁc"
     */
    private void criarArquivoDeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de trafégo.");
        File randomTrips = new File(SimulacaoMicroscopica.getInstance().trataEnderecoArquivo("src/br/udesc/ceavi/pin2/utils/randomTrips.py"));
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
                randomTrips.getAbsolutePath() + " -n "
                + this.pastaSimulacao.getAbsolutePath() + File.separator + "rede.net.xml -e 5000 -r "
                + this.pastaSimulacao.getAbsolutePath() + File.separator + "rotas.rou.xml"
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().log("Arquivo de trafégo gerado.");
    }

    /**
     * Cria arquivo que será executado na simulação
     */
    private void criarArquivoSimulacao() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de simulação.");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("configuration");
            document.appendChild(root);

            Element input = document.createElement("input");
            root.appendChild(input);

            Element netFile = document.createElement("net-file");
            Attr valueNetFile = document.createAttribute("value");
            valueNetFile.setValue(this.pastaSimulacao.getAbsolutePath() + File.separator + "rede.net.xml");
            netFile.setAttributeNode(valueNetFile);

            Element routeFile = document.createElement("route-files");
            Attr valueRouteFile = document.createAttribute("value");
            valueRouteFile.setValue(this.pastaSimulacao.getAbsolutePath() + File.separator + "rotas.rou.xml");
            routeFile.setAttributeNode(valueRouteFile);

            Element aditionalFile = document.createElement("additional-files");
            Attr valueAddFile = document.createAttribute("value");
            valueAddFile.setValue(this.pastaSimulacao.getAbsolutePath() + File.separator + "poi.net.xml");
            aditionalFile.setAttributeNode(valueAddFile);

            input.appendChild(netFile);
            input.appendChild(routeFile);
            input.appendChild(aditionalFile);

            Element output = document.createElement("output");
            Element summaryOutput = document.createElement("summary-output");
            Attr summary = document.createAttribute("value");
            summary.setValue(this.pastaSimulacao.getAbsolutePath() + File.separator + "results.out.xml");
            summaryOutput.setAttributeNode(summary);
            output.appendChild(summaryOutput);
            root.appendChild(output);

            Element processing = document.createElement("processing");
            Element routeErrors = document.createElement("ignore-route-errors");
            Attr routeErrorsValue = document.createAttribute("value");
            routeErrorsValue.setValue("true");
            routeErrors.setAttributeNode(routeErrorsValue);
            processing.appendChild(routeErrors);
            root.appendChild(processing);

            Element report = document.createElement("report");

            Element verbose = document.createElement("verbose");
            Attr verboseValue = document.createAttribute("value");
            verboseValue.setValue("true");
            verbose.setAttributeNode(verboseValue);

            Element log = document.createElement("duration-log.statistics");
            Attr logValue = document.createAttribute("value");
            logValue.setValue("true");
            log.setAttributeNode(logValue);

            report.appendChild(verbose);
            report.appendChild(log);

            root.appendChild(report);
            
            Element additional = document.createElement("additional");
            Element type = document.createElement("vType");
            Attr maxSpeed = document.createAttribute("maxSpeed");
            maxSpeed.setValue("27.8");
            type.setAttributeNode(maxSpeed);
            
            Attr vClass = document.createAttribute("vClass");
            vClass.setValue("car");
            type.setAttributeNode(vClass);
            
            additional.appendChild(type);
            root.appendChild(additional);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(this.pastaSimulacao.getAbsolutePath() + File.separator + "simulacao.sumocfg"));

            transformer.transform(domSource, streamResult);
            this.notificaSucessoExecucaoComando("\nArquivo de simulação gerado.\n");
        } catch (ParserConfigurationException | TransformerException ex) {
            this.notificaErroExecucaoComando(new ErroGeracaoArquivoXML(ex));
        }
    }

    @Override
    /**
     * {@inheritdoc}
     */
    protected int getTotalEtapas() {
        return 4;
    }
}
