package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;

/**
 * Interface para observadores relacionados a execução da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public interface ObservadorSimulacao {

    public void erroExecucaoSimulacao(ErroExecucaoCommando ex);

    public void sucessoExecucaoSimulacao();
    
    public void entradaTraCI(String entrada);
    
}
