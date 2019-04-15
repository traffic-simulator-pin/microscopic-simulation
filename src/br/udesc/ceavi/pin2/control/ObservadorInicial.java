package br.udesc.ceavi.pin2.control;

import java.io.File;

/**
 * Interface para observadores relacionados a inicialização da simulação.
 * @author Jeferson Penz
 */
public interface ObservadorInicial {
    
    /**
     * Indica que um arquivo foi carregado.
     * @param arquivo 
     */
    public void arquivoCarregado(File arquivo);
    
}
