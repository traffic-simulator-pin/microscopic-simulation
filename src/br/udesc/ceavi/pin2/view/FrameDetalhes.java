package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleDetalhes;
import br.udesc.ceavi.pin2.control.IControleDetalhes;
import br.udesc.ceavi.pin2.control.IControleSimulacao;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Frame com os detalhes da simulação.
 * @author Jeferson Penz
 */
public class FrameDetalhes extends JFrame{
    
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
        this.setSize(new Dimension(800, 600));
        this.getContentPane().removeAll();
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
        // TODO REMOVER
        this.painelDados.adicionaDados("Teste1", "Teste<br/><ul><li>Teste</li><li>Teste</li></ul>");
        this.painelDados.adicionaDados("Teste2", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
        this.painelDados.adicionaDados("Teste3", "Teste<br/>Teste<br/>Teste");
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
    }
    
}
