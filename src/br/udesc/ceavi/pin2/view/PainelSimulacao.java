package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.control.IControleSimulacao;
import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleSimulacao;
import br.udesc.ceavi.pin2.control.ObservadorSimulacao;
import br.udesc.ceavi.pin2.control.traci.Veiculo;
import br.udesc.ceavi.pin2.exceptions.CampoDesconhecidoException;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Painel para a tela de simulação da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class PainelSimulacao extends JPanel implements ObservadorSimulacao{
    
    private IControleSimulacao  controller;
    private PainelConfiguracoes painelConfig;
    private JPanel              painelDadosVeiculo;
    private PainelAcoes         painelAcoes;
    
    /**
     * Cria um novo painel inicial para a aplicação.
     */
    public PainelSimulacao() {
        super();
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }
    
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.controller = new ControleSimulacao();
        try {
            this.controller.iniciaSimulacao();
        } catch(LogException ex){
            ex.generateLog();
            JOptionPane.showMessageDialog(this, "Houve um erro ao iniciar a simulação:\n" + ex.getMessage());
        }
        this.controller.anexaObservador(this);
        
        this.setBorder(BorderFactory.createLineBorder(SimulacaoMicroscopica.COR_BORDA, 2));
        this.setLayout(new BorderLayout(0, 0));
    }
    
    /**
     * Realiza a inicialização dos componentes do painel.
     */
    private void iniciaComponentes() {
        this.painelConfig    = new PainelConfiguracoes();
        try {
            this.painelConfig.setAllConfiguracoes(SimulacaoMicroscopica.getInstance().getConfiguracoes());
        } catch(CampoDesconhecidoException ex){
            JOptionPane.showMessageDialog(this, "Houve um erro ao iniciar a simulação:\n" + ex.getMessage());
        }
        this.painelConfig.desabilitaConfiguracoes();
        this.painelAcoes     = new PainelAcoes();
        this.painelAcoes.adicionaAcao("finalizar", "Finalizar Simulação", (ActionEvent e) -> {
            this.controller.finalizaSimulacao();
        });
        this.painelDadosVeiculo = new JPanel();
        this.painelDadosVeiculo.setBackground(Color.WHITE);
        this.painelDadosVeiculo.setLayout(new BoxLayout(this.painelDadosVeiculo, BoxLayout.Y_AXIS));
        JScrollPane paneDadosVeiculos = new JScrollPane(this.painelDadosVeiculo);
        this.add(painelConfig,     BorderLayout.NORTH);
        this.add(paneDadosVeiculos, BorderLayout.CENTER);
        this.add(painelAcoes,      BorderLayout.SOUTH);
    }

    @Override
    public void erroExecucaoSimulacao(LogException ex) {
        ex.generateLog();
        JOptionPane.showMessageDialog(this, "Houve um erro ao iniciar a simulação:\n" + ex.getMessage());
    }

    @Override
    public void sucessoExecucaoSimulacao() {}

    @Override
    public void entradaTraCI(String entrada) {
        SimulacaoMicroscopica.getInstance().log(SimulacaoMicroscopica.LOG_TYPE.TRACI, entrada);
    }

    @Override
    public void logTraCI(String entrada) {
        SimulacaoMicroscopica.getInstance().log(SimulacaoMicroscopica.LOG_TYPE.TRACI, entrada);
    }

    @Override
    public void novoVeiculo(Veiculo veiculo) {
        PainelDadosVeiculo painel = new PainelDadosVeiculo(veiculo);
        this.controller.adicionaVeiculoAnalisado(painel.getControleVeiculo());
        this.painelDadosVeiculo.add(painel);
        SwingUtilities.invokeLater(() -> {
            this.painelDadosVeiculo.revalidate();
        });
    }
}
