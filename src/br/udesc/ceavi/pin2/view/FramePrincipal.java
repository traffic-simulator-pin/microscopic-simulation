package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Tela Principal da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class FramePrincipal extends JFrame {

    /**
     * Cria uma nova tela principal para a aplicação.
     */
    public FramePrincipal() {
        super(SimulacaoMicroscopica.NOME_APLICACAO);
        SwingUtilities.invokeLater(() -> {
            this.iniciaPropriedadesJanela();
        });
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                if(SimulacaoMicroscopica.getInstance().isExecutando()){
                    SimulacaoMicroscopica.getInstance().fechaAplicacao();
                }
            }
        });
    }
    
    /**
     * Inicia as propriedades da janela.
     */
    private void iniciaPropriedadesJanela(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        SimulacaoMicroscopica.getInstance().log("Criada tela inicial da aplicação.");
    }
    
    /**
     * Carrega a tela de execução da simulação.
     */
    public void carregaTelaExecucaoSimulacao(){
        this.setVisible(false);
        this.setSize(new Dimension(600, 400));
        this.getContentPane().removeAll();
        JPanel painelBase = new PainelSimulacao();
        this.setContentPane(painelBase);
        this.setVisible(true);
        SimulacaoMicroscopica.getInstance().log("Criada tela de execução da aplicação.");
    }
    
}
