package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleDetalhes;
import br.udesc.ceavi.pin2.control.IControleDetalhes;
import br.udesc.ceavi.pin2.control.IControleSimulacao;
import br.udesc.ceavi.pin2.control.ObservadorSimulacao;
import br.udesc.ceavi.pin2.control.traci.Veiculo;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Frame com os detalhes da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class FrameDetalhes extends JFrame implements ObservadorSimulacao{
    
    private IControleDetalhes controller;
    private JPanel            painelDetalhes;
    private PainelDados       painelDados;
    private PainelAcoes       painelAcoes;

    /**
     * Cria uma nova janela com os detalhes da simulação.
     */
    public FrameDetalhes() {
        super(SimulacaoMicroscopica.NOME_APLICACAO);
        this.controller = new ControleDetalhes();
        SwingUtilities.invokeLater(() -> {
            this.iniciaPropriedadesJanela();
            this.carregaTelaDetalhes();
        });
    }
    
    /**
     * Inicia as propriedades da janela.
     */
    private void iniciaPropriedadesJanela(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setResizable(false);
    }
    
    /**
     * Carrega a tela de detalhes da simulação.
     */
    public void carregaTelaDetalhes(){
        this.setSize(new Dimension(600, 400));
        this.getContentPane().removeAll();
        this.setLocationRelativeTo(null);
        this.setContentPane(this.iniciaPainelDetalhes());
    }

    /**
     * Inicia o painel de detalhes da aplicação e retorna ele.
     * @return 
     */
    private JPanel iniciaPainelDetalhes() {
        this.painelDetalhes = new JPanel();
        this.painelDetalhes.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.painelDetalhes.setBorder(BorderFactory.createLineBorder(SimulacaoMicroscopica.COR_BORDA, 2));
        this.painelDetalhes.setLayout(new BorderLayout(0, 0));
        this.painelDados = new PainelDados();
        this.painelDados.adicionaDados("Etapa", "Iniciada Simulação.");
        this.painelAcoes = new PainelAcoes();
        this.painelDetalhes.add(painelDados, BorderLayout.CENTER);
        this.painelDetalhes.add(painelAcoes, BorderLayout.SOUTH);
        return this.painelDetalhes;
    }
    
    /**
     * Carrega os detalhes da simulação do controle especificado.
     * @param controller 
     */
    public void carregaDetalhes(IControleSimulacao controller){
        this.controller.setControlerSimulacao(controller);
        this.controller.getControllerSimulacao().anexaObservador(this);
    }

    public void ocultaDetalhes() {
        this.controller.getControllerSimulacao().desanexaObservador(this);
        this.controller.setControlerSimulacao(null);
    }

    @Override
    public void erroExecucaoSimulacao(LogException ex) {}

    @Override
    public void sucessoExecucaoSimulacao(){}

    @Override
    public void entradaTraCI(String entrada) {
        SwingUtilities.invokeLater(() -> {
            this.painelDados.adicionaDados("Entrada TraCI: ", entrada);
            this.revalidate();
        });
    }

    @Override
    public void logTraCI(String entrada) {}

    @Override
    public void novoVeiculo(Veiculo veiculo) {}
    
}
