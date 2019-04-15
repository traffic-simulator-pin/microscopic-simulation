package br.udesc.ceavi.pin2.control;

/**
 * Interface para realizar o controle dos detalhes da simulação.
 * @author Jeferson Penz
 */
public interface IControleDetalhes {

    /**
     * Define o controle usado para observar os detalhes da simulação.
     * @param controle 
     */
    public void setControlerSimulacao(IControleSimulacao controle);
    
}
