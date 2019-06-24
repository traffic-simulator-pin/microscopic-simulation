package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.control.traci.Veiculo;
import br.udesc.ceavi.pin2.utils.PosicaoVeiculo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para dados do veículo.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class ControleVeiculo implements IControleVeiculo{

    private Veiculo veiculo;
    private List<ObservadorVeiculo> observadores;

    public ControleVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.observadores = new ArrayList<>();
    }
    
    @Override
    public String getIdVeiculo() {
        return veiculo.getIdVeiculo();
    }

    @Override
    public double getVelocidadeVeiculo() {
        return veiculo.getVelocidadeVeiculo();
    }

    @Override
    public PosicaoVeiculo getPosicaoVeiculo() {
        return veiculo.getPosicaoVeiculo();
    }
    
    @Override
    public void notificaObservadoresVeiculoAlterado(){
        this.observadores.forEach((observador) -> {
            observador.onDadosVeiculoAlterado();
        });
    }

    @Override
    public void addObservador(ObservadorVeiculo observador) {
        this.observadores.add(observador);
    }
    
}
