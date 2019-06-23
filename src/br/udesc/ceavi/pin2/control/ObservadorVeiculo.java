package br.udesc.ceavi.pin2.control;

/**
 * Observador para dados do veículo.
 * @author Jeferson Penz
 */
public interface ObservadorVeiculo {
    
    /**
     * Notifica caso os dados do veículo mudem.
     */
    public void onDadosVeiculoAlterado();
    
}
