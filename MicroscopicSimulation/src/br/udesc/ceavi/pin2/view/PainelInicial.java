package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleInicial;
import br.udesc.ceavi.pin2.control.IControleInicial;
import br.udesc.ceavi.pin2.control.ObservadorInicial;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Painel para a tela inicial da aplicação.
 * @author Jeferson Penz
 */
public class PainelInicial extends JPanel implements ObservadorInicial{
    
    private IControleInicial controller;
    private JPanel           painelArquivo;
    private JPanel           painelConfig;
    private PainelAcoes      painelAcoes;
    
    /**
     * Cria um novo painel inicial para a aplicação.
     */
    public PainelInicial() {
        super();
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.controller = new ControleInicial();
        this.controller.anexaObservador(this);
        
        this.setBorder(BorderFactory.createLineBorder(SimulacaoMicroscopica.COR_BORDA, 2));
        this.setLayout(new BorderLayout(0, 0));
    }
    /**
     * Realiza a inicialização dos componentes do painel.
     */
    private void iniciaComponentes() {
        this.painelArquivo = new PainelArquivo(this.controller);
        this.painelConfig  = new PainelConfiguracoes();
        this.painelAcoes   = new PainelAcoes();
        this.painelAcoes.adicionaAcao("iniciar", "Iniciar Simulação", (ActionEvent e) -> {
            this.controller.iniciaSimulacao();
        });
        this.painelAcoes.desabilitaAcao("iniciar");
        this.add(painelArquivo, BorderLayout.NORTH);
        this.add(painelConfig,  BorderLayout.CENTER);
        this.add(painelAcoes,   BorderLayout.SOUTH);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void arquivoCarregado(File arquivo) {
        this.painelAcoes.habilitaAcao("iniciar");
    }
    
}
