package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para realizar a execução da simulação.
 * @author Jeferson Penz
 */
public class ControleSimulacao implements IControleSimulacao{
    
    private final List<ObservadorSimulacao> observadores;
    
    /**
     * Cria um novo controlador para realizar a execução da simulação.
     */
    public ControleSimulacao() {
        this.observadores = new ArrayList<>();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void finalizaSimulacao() {
        SimulacaoMicroscopica.getInstance().log("Finalizando simulação.");
        SimulacaoMicroscopica.getInstance().fechaSimulacao();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void anexaObservador(ObservadorSimulacao observador) {
        this.observadores.add(observador);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void desanexaObservador(ObservadorSimulacao observador){
        this.observadores.remove(observador);
    }
    
}
