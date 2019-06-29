package br.udesc.ceavi.pin2.control.traci;

import br.udesc.ceavi.pin2.utils.PosicaoVeiculo;
import java.awt.Point;

/**
 *
 * @author Jeferson Penz
 */
public class Veiculo {
    
    private String IdVeiculo;
    
    private double velocidadeVeiculo;
    private PosicaoVeiculo posicaoVeiculo;

    /**
     * Cria um novo veículo com o ID informado.
     * @param IdVeiculo 
     */
    public Veiculo(String IdVeiculo) {
        this.IdVeiculo = IdVeiculo;
    }

    /**
     * Retorna o ID do veículo.
     * @return 
     */
    public String getIdVeiculo() {
        return IdVeiculo;
    }

    /**
     * Retorna a velocidade do veículo.
     * @return 
     */
    public double getVelocidadeVeiculo() {
        return velocidadeVeiculo;
    }

    /**
     * Define a velocidade do veículo.
     * @param velocidadeVeiculo 
     */
    public void setVelocidadeVeiculo(double velocidadeVeiculo) {
        this.velocidadeVeiculo = velocidadeVeiculo;
    }

    /**
     * Retorna a posição veículo.
     * @return 
     */
    public PosicaoVeiculo getPosicaoVeiculo() {
        return posicaoVeiculo;
    }

    /**
     * Define a posição do veículo
     * @param posicaoVeiculo 
     */
    public void setPosicaoVeiculo(PosicaoVeiculo posicaoVeiculo) {
        this.posicaoVeiculo = posicaoVeiculo;
    }

    @Override
    public String toString() {
        return "{ id: " + IdVeiculo + "; velocidade: " + velocidadeVeiculo + "; posicao:" + posicaoVeiculo + " }";
    }
    
}
