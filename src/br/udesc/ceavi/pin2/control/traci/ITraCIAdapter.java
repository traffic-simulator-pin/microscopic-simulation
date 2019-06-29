package br.udesc.ceavi.pin2.control.traci;

import br.udesc.ceavi.pin2.exceptions.ErroInicioTraCI;
import java.util.List;
import java.util.Map;

/**
 * Interface para adaptadores para comunicação com o servidor TraCI.
 * @author Jeferson Penz
 */
public interface ITraCIAdapter {
    
    /**
     * Inicia a comunicação e a simulação.
     * @throws ErroInicioTraCI 
     */
    public void begin() throws ErroInicioTraCI;
    
    /**
     * Busca uma lista de todos os veículos.
     * @return 
     */
    public Map<String, Veiculo> getAllVeiculos();
    
    /**
     * Adiciona um observador para eventos do TraCI.
     * @param observador 
     */
    public void addObservador(TraCIObserver observador);
    
}
