package br.udesc.ceavi.pin2.control.traci;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoTraCI;

/**
 * Observadores do estado do TraCI.
 * @author Jeferson Penz
 */
public interface TraCIObserver {
    
    /**
     * Em caso de erro no TraCI.
     * @param ex 
     */
    public void onErroTraCI(ErroExecucaoTraCI ex);
    
    /**
     * No surgimento de um novo veículo.
     * @param veiculo 
     */
    public void onNovoVeiculo(Veiculo veiculo);

    public void onDadoVeiculo(String dado, Veiculo veiculo);
    
}
