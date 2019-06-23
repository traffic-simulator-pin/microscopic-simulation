package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * Tela Principal da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
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
//        addInternalFrameListener(new InternalFrameAdapter(){
//            @Override
//            public void internalFrameClosing(InternalFrameEvent e) {
//                if(SimulacaoMicroscopica.getInstance().isExecutando()){
//                    SimulacaoMicroscopica.getInstance().fechaSimulacao();
//                }
//            }
//        });
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
