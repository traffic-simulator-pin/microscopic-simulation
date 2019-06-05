package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.exceptions.LogException;
import java.io.File;

/**
 * Interface para observadores relacionados a inicialização da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public interface ObservadorInicial {
    
    /**
     * Indica que um arquivo foi carregado.
     * @param arquivo 
     */
    public void arquivoCarregado(File arquivo);
    
    /**
     * Indica que a rede está sendo carregada;
     */
    public void inicioGeracaoRede();
    
    /**
     * Indica o sucesso no carregamento da rede;
     */
    public void sucessoGeracaoRede();
    
    /**
     * Indica um erro no carregamento da rede;
     * @param ex
     */
    public void erroGeracaoRede(LogException ex);
    
}
