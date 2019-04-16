package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;

/**
 * Controle para gerenciamento dos detalhes da simulação.
 * @author Jeferson Penz
 */
public class ControleDetalhes implements IControleDetalhes{

    private IControleSimulacao simulacao;

    @Override
    public void setControlerSimulacao(IControleSimulacao controle) {
        this.simulacao = controle;
    }
    
}
