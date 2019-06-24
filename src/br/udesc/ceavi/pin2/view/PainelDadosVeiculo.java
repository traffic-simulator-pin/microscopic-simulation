package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import static br.udesc.ceavi.pin2.SimulacaoMicroscopica.doubleDuasCasas;
import br.udesc.ceavi.pin2.control.ControleVeiculo;
import br.udesc.ceavi.pin2.control.IControleVeiculo;
import br.udesc.ceavi.pin2.control.ObservadorVeiculo;
import br.udesc.ceavi.pin2.control.traci.Veiculo;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Painel para armazenar os dados de um veículo.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class PainelDadosVeiculo extends JPanel implements ObservadorVeiculo {
    
    private IControleVeiculo controller;
    private JLabel tituloVeiculo;
    private JLabel velocidadeVeiculo;
    private JLabel posicaoVeiculo;
    
    /**
     * Cria um novo painel para armazenar e exibir os dados dos veículos.
     * @param veiculo
     */
    public PainelDadosVeiculo(Veiculo veiculo){
        this.controller = new ControleVeiculo(veiculo);
        this.controller.addObservador(this);
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }
    
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
    }
    
    /**
     * Realiza a inicialização dos componentes do painel.
     */
    private void iniciaComponentes() {
        this.tituloVeiculo = new JLabel("Veículo " + this.controller.getIdVeiculo());
        this.velocidadeVeiculo = new JLabel("Velocidade: Desconhecida");
        this.posicaoVeiculo = new JLabel("Posição: Desconhecida");
        this.add(this.tituloVeiculo);
        this.add(this.velocidadeVeiculo);
        this.add(this.posicaoVeiculo);
    }
    
    public IControleVeiculo getControleVeiculo(){
        return this.controller;
    }

    @Override
    public void onDadosVeiculoAlterado() {
        this.tituloVeiculo.setText("Veiculo " + this.controller.getIdVeiculo());
        this.velocidadeVeiculo.setText("Velocidade: " + SimulacaoMicroscopica.doubleDuasCasas.format(this.controller.getVelocidadeVeiculo()));
        this.posicaoVeiculo.setText("Posição: " + this.controller.getPosicaoVeiculo());
    }
    
}
