package br.udesc.ceavi.pin2.control;

/**
 * Interface para realizar o controle da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public interface IControleSimulacao {
    
    /**
     * Finaliza a execução da simulação.
     */
    public void finalizaSimulacao();
    
    /**
     * Anexa um observador ao controlador.
     * @param observador 
     */
    public void anexaObservador(ObservadorSimulacao observador);
    
    /**
     * Desanexa um observador do controlador
     * @param observador 
     */
    public void desanexaObservador(ObservadorSimulacao observador);
    
}
