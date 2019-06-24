package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroGeracaoArquivoXML;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.io.File;
import java.util.Map;
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
import org.w3c.dom.NodeList;

/**
 * Classe responsável por gerar os arquivos de rede de tráfego.
 *
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João
 * Pedro Schimitz
 */
public class GeradorRede extends ExecucaoMultiEtapas {

    private File arquivoSimulacao;
    private final File pastaSimulacao;

    /**
     * Cria um novo controle para geração do arquivo da rede
     *
     * @param arquivoSimulacao
     * @param configuracoes
     */
    public GeradorRede(File arquivoSimulacao, Map<String, String> configuracoes) {
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
                this.realocarArquivoDeEntrada();
                break;
            case 2:
                this.criarArquivoEdge();
                break;
            case 3:
                this.criarArquivoNode();
                break;
            case 4:
                this.criaRedeTrafego();
                break;
            case 5:
                this.criarArquivoDePOI();
                break;
            case 6:
                this.criarArquivoDeTrafego();
                break;
            case 7:
                this.criarArquivoSimulacao();
                break;
            default:
                break;
        }
    }

    /* 
     - Gerar as rotas na mão.
     - Liberar botões para nova execução no caso de erro
     - Integração com a lib para mostrar os dados da simulação em tempo real na tela de detalhes;
     */
    private void realocarArquivoDeEntrada() {
        try {
            SimulacaoMicroscopica.getInstance().log("Copiando arquivos de entrada do XML para pasta da simulação.");
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Document documentoEntrada = documentBuilder.parse(this.arquivoSimulacao);
            Element rootEntrada = documentoEntrada.getDocumentElement();
            NodeList nodesList = rootEntrada.getElementsByTagName("node");
            NodeList edgesList = rootEntrada.getElementsByTagName("edge");
            Element root = document.createElement("data");
            document.appendChild(root);

            Element nodes = document.createElement("nodes");
            root.appendChild(nodes);

            for (int i = 0; i < nodesList.getLength(); i++) {
                Element e = (Element) nodesList.item(i);
                Element node = document.createElement("node");
                node.setAttribute("id", e.getAttribute("id"));
                node.setAttribute("lat", e.getAttribute("x"));
                node.setAttribute("lon", e.getAttribute("y"));
                nodes.appendChild(node);
            }

            Element edges = document.createElement("edges");
            root.appendChild(edges);

            for (int i = 0; i < edgesList.getLength(); i++) {
                Element e = (Element) edgesList.item(i);
                Element edge = document.createElement("edge");
                edge.setAttribute("capacity", e.getAttribute("capacity"));
                edge.setAttribute("id", e.getAttribute("id"));
                edge.setAttribute("length", e.getAttribute("length"));
                edge.setAttribute("name", e.getAttribute("name"));
                edge.setAttribute("numLanes", e.getAttribute("numLanes"));
                edge.setAttribute("oneway", e.getAttribute("oneway"));
                edge.setAttribute("from", e.getAttribute("source"));
                edge.setAttribute("speed", e.getAttribute("speed"));
                edge.setAttribute("to", e.getAttribute("target"));
                edge.setAttribute("type", e.getAttribute("type"));

                edges.appendChild(edge);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            File newFile = new File(this.pastaSimulacao.getAbsolutePath() + File.separator + "entrada.xml");
            StreamResult streamResult = new StreamResult(newFile);

            this.arquivoSimulacao = newFile;

            transformer.transform(domSource, streamResult);
            this.onCommandSucess("criado arquivos com sucesso");
            SimulacaoMicroscopica.getInstance().log("Arquivo de entrada copiado.");
        } catch (Exception e) {
            this.notificaErroExecucaoComando((LogException) e);
            SimulacaoMicroscopica.getInstance().log("ERRO na criação do arquivo de entrada");
        }

    }

    private void criarArquivoNode() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de nodos pelo entrada.xml.");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Document documentoEntrada = documentBuilder.parse(this.arquivoSimulacao);
            Element rootEntrada = documentoEntrada.getDocumentElement();
            NodeList nodesList = rootEntrada.getElementsByTagName("node");
            Element root = document.createElement("nodes");
            document.appendChild(root);

            for (int i = 0; i < nodesList.getLength(); i++) {
                Element e = (Element) nodesList.item(i);
                Element node = document.createElement("node");
                node.setAttribute("id", e.getAttribute("id"));
                node.setAttribute("x", e.getAttribute("lat"));
                node.setAttribute("y", e.getAttribute("lon"));
                root.appendChild(node);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(this.pastaSimulacao.getAbsolutePath()
                    + File.separator + "nodes.nod.xml"));
            transformer.transform(domSource, streamResult);
            this.onCommandSucess("criado arquivo node com sucesso");
            SimulacaoMicroscopica.getInstance().log("Arquivo de nodos criado.");
        } catch (Exception e) {
            this.notificaErroExecucaoComando((LogException) e);
            SimulacaoMicroscopica.getInstance().log("ERRO na criação de nodos");
        }

    }

    private void criarArquivoEdge() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de edges pelo entrada.xml.");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Document documentoEntrada = documentBuilder.parse(this.arquivoSimulacao);
            Element rootEntrada = documentoEntrada.getDocumentElement();
            NodeList edgesList = rootEntrada.getElementsByTagName("edge");
            Element root = document.createElement("edges");
            document.appendChild(root);

            for (int i = 0; i < edgesList.getLength(); i++) {
                Element e = (Element) edgesList.item(i);
                Element edge = document.createElement("edge");
                edge.setAttribute("capacity", e.getAttribute("capacity"));
                edge.setAttribute("id", e.getAttribute("id"));
                edge.setAttribute("length", e.getAttribute("length"));
                edge.setAttribute("name", e.getAttribute("name"));
                edge.setAttribute("numLanes", e.getAttribute("numLanes"));
                edge.setAttribute("oneway", e.getAttribute("oneway"));
                edge.setAttribute("from", e.getAttribute("from"));
                edge.setAttribute("speed", e.getAttribute("speed"));
                edge.setAttribute("to", e.getAttribute("to"));
                edge.setAttribute("type", e.getAttribute("type"));

                root.appendChild(edge);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(this.pastaSimulacao.getAbsolutePath() + File.separator + "edges.edg.xml"));
            transformer.transform(domSource, streamResult);
            this.onCommandSucess("criado arquivo edges com sucesso");
            SimulacaoMicroscopica.getInstance().log("Arquivo de edges criado.");
        } catch (Exception e) {
            this.notificaErroExecucaoComando((LogException) e);
            SimulacaoMicroscopica.getInstance().log("Erro na criação de edges");
        }

    }

    /**
     * Realiza a criação dos arquivos da rede de tráfego. Artigo p.18
     * "Converting from an OSM ﬁle to a SUMO .net.xml ﬁle"
     */
    private void criaRedeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
                "netconvert --node-files=" + this.pastaSimulacao.getAbsolutePath() + File.separator + "nodes.nod.xml "
                + "--edge-files=" + this.pastaSimulacao.getAbsolutePath() + File.separator + "edges.edg.xml "
                + "--output-file=" + this.pastaSimulacao.getAbsolutePath() + File.separator + "rede.net.xml --ignore-errors.edge-type"
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
        File polygonUtil = new File(SimulacaoMicroscopica.getInstance().trataEnderecoArquivo("src/jar/lib/osmPolyconvert.typ.xml"));
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
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de tráfego.");

        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
                "py \"%SUMO_HOME%" + File.separator + "tools" + File.separator + "randomTrips.py\" --net-file="
                + this.pastaSimulacao.getAbsolutePath() + File.separator + "rede.net.xml --end=50 --route-file="
                + this.pastaSimulacao.getAbsolutePath() + File.separator + "rotas.rou.xml"
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().log("Arquivo de tráfego gerado.");
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
            SimulacaoMicroscopica.getInstance().log("Arquivo de simulação gerado.");
        } catch (ParserConfigurationException | TransformerException ex) {
            this.notificaErroExecucaoComando(new ErroGeracaoArquivoXML(ex));
            SimulacaoMicroscopica.getInstance().log("Erro na geração de arquivo de trafégo");
        }
    }

    @Override
    /**
     * {@inheritdoc}
     */
    protected int getTotalEtapas() {
        return 7;
    }
}
