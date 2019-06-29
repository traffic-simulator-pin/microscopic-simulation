package br.udesc.ceavi.pin2.control;

/**
 * Controle para gerenciamento dos detalhes da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ControleDetalhes implements IControleDetalhes{

    private IControleSimulacao simulacao;

    @Override
    public void setControlerSimulacao(IControleSimulacao controle) {
        this.simulacao = controle;
    }

    @Override
    public IControleSimulacao getControllerSimulacao() {
        return simulacao;
    }
    
}
