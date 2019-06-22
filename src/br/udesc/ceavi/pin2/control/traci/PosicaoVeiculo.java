package br.udesc.ceavi.pin2.control.traci;

/**
 * Representa a posição de um veículo.
 * @author Jeferson Penz
 */
public class PosicaoVeiculo {
    
    private double x;
    private double y;

    public PosicaoVeiculo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{ x: " + this.x + "; y: " + this.y + " }";
    }
    
}
