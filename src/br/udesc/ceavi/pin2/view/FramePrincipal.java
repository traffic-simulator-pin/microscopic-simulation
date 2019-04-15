package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Tela Principal da aplicação.
 * @author Jeferson Penz
 */
public class FramePrincipal extends JFrame{

    /**
     * Cria uma nova tela principal para a aplicação.
     */
    public FramePrincipal() {
        super(SimulacaoMicroscopica.NOME_APLICACAO);
        SwingUtilities.invokeLater(() -> {
            this.iniciaPropriedadesJanela();
        });
    }
    
    /**
     * Inicia as propriedades da janela.
     */
    private void iniciaPropriedadesJanela(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    
    /**
     * Carrega a tela inicial de simulação.
     */
    public void carregaTelaInicialSimulacao(){
        this.setVisible(false);
        this.setSize(new Dimension(400, 300));
        this.getContentPane().removeAll();
        JPanel painelBase = new PainelInicial();
        this.setContentPane(painelBase);
        this.setVisible(true);
    }
    
    /**
     * Carrega a tela de execução da simulação.
     */
    public void carregaTelaExecucaoSimulacao(){
        this.setVisible(false);
        this.setSize(new Dimension(800, 600));
        this.getContentPane().removeAll();
        JPanel painelBase = new PainelSimulacao();
        this.setContentPane(painelBase);
        this.setVisible(true);
    }
    
}
