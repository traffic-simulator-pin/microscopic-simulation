package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Tela Temporaria cuja funcao e simular a tela onde ira ocorrer a integracao de todas as equipes
 *
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João
 * Pedro Schimitz
 */
public class FrameTemporario extends JFrame {

    /**
     * Cria uma nova tela principal para a aplicação.
     */
    public FrameTemporario() {
       // super(SimulacaoMicroscopica.NOME_APLICACAO);
        //  SwingUtilities.invokeLater(() -> {
        this.iniciaPropriedadesJanela();
//        });

    }

    /**
     * Inicia as propriedades da janela.
     */
    private void iniciaPropriedadesJanela() {
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(new Dimension(800, 500));
        this.setLocationRelativeTo(null);
    }


}
