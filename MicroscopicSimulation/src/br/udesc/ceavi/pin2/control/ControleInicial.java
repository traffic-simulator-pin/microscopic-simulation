package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * Controlador para a tela inicial da aplicação.
 * @author Jeferson Penz
 */
public class ControleInicial implements IControleInicial{
    
    // Modelo de simulação carregado do arquivo.
    private Object modeloSimulacao;
    
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
    public void iniciaCarregamentoNovoArquivo(File arquivo) throws ExtensaoArquivoInvalida{
        String extensao = FilenameUtils.getExtension(arquivo.getName()).toLowerCase();
        if(!extensao.equals("sim")){
            throw new ExtensaoArquivoInvalida(extensao, ".sim");
        }
        this.processaArquivo(arquivo);
        this.notificaArquivoCarregado(arquivo);
    }
    
    /**
     * Realiza o processamento e carregamento dos dados arquivo.
     * @param file 
     */
    private void processaArquivo(File file){
        
    }
    
    /**
     * Notifica os observadores que um arquivo foi carregado.
     */
    private void notificaArquivoCarregado(File arquivo){
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
    public void desanexaObservador(ObservadorInicial observador){
        this.observadores.remove(observador);
    }
    
}
