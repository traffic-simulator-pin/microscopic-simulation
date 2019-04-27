package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.utils.shell.ShellListener;
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
        this.notificaInicioGeracaoRede();
        this.criaPastaTemporariaArquivo();
        //TODO Iniciar a thread.
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

    //TODO REMOVER
    public void onCommandSucess(String retorno) {
        SwingUtilities.invokeLater(() -> {
            SimulacaoMicroscopica.getInstance().log("Retorno:\n" + retorno);
            this.notificaSucessoGeracaoRede();
            SimulacaoMicroscopica.getInstance().iniciaSimulacao(null, null);
        });
    }

    //TODO REMOVER
    public void onCommandException(ErroExecucaoCommando ex) {
        SwingUtilities.invokeLater(() -> {
            this.notificaErroGeracaoRede(ex);
        });
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

}
