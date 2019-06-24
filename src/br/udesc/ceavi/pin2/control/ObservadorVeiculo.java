package br.udesc.ceavi.pin2.control;

/**
 * Observador para dados do veículo.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public interface ObservadorVeiculo {
    
    /**
     * Notifica caso os dados do veículo mudem.
     */
    public void onDadosVeiculoAlterado();
    
}
