package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.utils.PosicaoVeiculo;

/**
 * Interface para controle de dados de um veículo.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public interface IControleVeiculo {
    
    /**
     * Busca o ID do veículo.
     * @return 
     */
    public String getIdVeiculo();
    
    /**
     * Busca a velocidade do veículo.
     * @return 
     */
    public double getVelocidadeVeiculo();
    
    /**
     * Busca a posição do veículo.
     * @return 
     */
    public PosicaoVeiculo getPosicaoVeiculo();
    
    /**
     * Notifica os observadores que o veículo foi alterado.
     */
    public void notificaObservadoresVeiculoAlterado();
    
    /**
     * Adiciona um observador para dados do veículo.
     * @param observador 
     */
    public void addObservador(ObservadorVeiculo observador);
    
}
