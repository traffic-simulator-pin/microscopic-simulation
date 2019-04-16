package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import br.udesc.ceavi.pin2.utils.OSUtils;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * Controlador para a tela inicial da aplicação.
 *
 * @author Jeferson Penz
 */
public class ControleInicial implements IControleInicial {

    // Modelo de simulação carregado do arquivo.
    private File arquivoSimulacao;

    private final List<ObservadorInicial> observadores;

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
        String extensao = FilenameUtils.getExtension(arquivo.getName()).toLowerCase();
        if (!extensao.equals(SimulacaoMicroscopica.EXTENSAO_ARQUIVO)) {
            throw new ExtensaoArquivoInvalida(extensao, SimulacaoMicroscopica.EXTENSAO_ARQUIVO);
        }
        this.processaArquivo(arquivo);
        this.notificaArquivoCarregado(arquivo);
    }

    /**
     * Realiza o processamento e carregamento dos dados arquivo.
     *
     * @param file
     */
    private void processaArquivo(File file) {
        this.arquivoSimulacao = file;
    }

    private boolean criaPastaParaSalvarArquivos() {
        SimpleDateFormat sdf = new SimpleDateFormat(SimulacaoMicroscopica.FORMATO_DATA);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        File file = new File("simulacoes/resultados/" + sdf.format(timestamp));
        if (file.mkdirs()) {
            SimulacaoMicroscopica.getInstance().setWorkspaceFolder("simulacoes/resultados/" + sdf.format(timestamp));
            return true;
        } else {
            // TODO, criar execeção
            return false;
        }
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
    public void iniciaSimulacao() {
        SimulacaoMicroscopica.getInstance().iniciaSimulacao(null, null);
        
        this.criaPastaParaSalvarArquivos();
        this.criaRedeDeTrafego();
        // Etapas 
        // OK - Criar pasta para salvar os arquivos da simulação
        // OK - Pegar arquivo OSM e converter para .net.xml
        // Limpar arquivo .net.xml removendo o desnecessário
        // Especificar informações que estão faltando, como velocidades máxima das ruas
        // Iniciar o SUMO
        // -> Definir o trafégo aleatoriamente
        // Criar arquivo example.sumocfg
        // Chamar pelo terminal sumo-gui -c example.sumocfg
    }
    
    public void criaRedeDeTrafego() {
        Shell terminal = SimulacaoMicroscopica.getShellCommand().getNewShell("netconvert --osm " + this.arquivoSimulacao.getAbsolutePath() + " -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + "/rede.net.xml");
        System.out.println(terminal.runShell());
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

}
