package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroCriacaoDiretorio;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FilenameUtils;

/**
 * Controlador para a tela inicial da aplicação.
 *
 * @author Jeferson Penz
 */
public class ControleInicial implements IControleInicial, ShellListener {

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
    public void iniciaSimulacao() throws LogException{
        SimulacaoMicroscopica.getInstance().log("Iniciando processo de simulação.");
        this.criaPastaTemporariaArquivo();
        this.criaRedeTrafego();
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

    /**
     * Realiza a criação das pastas temporárias para armazenar os arquivos.
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
     * Realiza a criação dos arquivos da rede de tráfego.
     */
    private void criaRedeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        this.notificaInicioGeracaoRede();
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
             "netconvert --osm " + this.arquivoSimulacao.getAbsolutePath() + " -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + "/rede.net.xml"
            ,SimulacaoMicroscopica.getInstance().getOperatingSystem().isWindows() ? "dir" : "ls"
        );
        terminal.start();
    }

    /**
     * Notifica os observadores que a rede está sendo carregada.
     */
    private void notificaInicioGeracaoRede() {
        this.observadores.forEach((observador) -> {
            observador.inicioGeracaoRede();
        });
    }

    @Override
    public void onCommandSucess(String retorno) {
        SwingUtilities.invokeLater(() -> {
            SimulacaoMicroscopica.getInstance().log("Retorno:\n" + retorno);
            this.notificaSucessoGeracaoRede();
            SimulacaoMicroscopica.getInstance().iniciaSimulacao(null, null);
        });
    }

    @Override
    public void onCommandException(ErroExecucaoCommando ex) {
        SwingUtilities.invokeLater(() -> {
            this.notificaErroGeracaoRede(ex);
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

}
