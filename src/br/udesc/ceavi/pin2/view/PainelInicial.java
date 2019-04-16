package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleInicial;
import br.udesc.ceavi.pin2.control.IControleInicial;
import br.udesc.ceavi.pin2.control.ObservadorInicial;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Painel para a tela inicial da aplicação.
 * @author Jeferson Penz
 */
public class PainelInicial extends JPanel implements ObservadorInicial{
    
    private IControleInicial    controller;
    private JPanel              painelArquivo;
    private PainelConfiguracoes painelConfig;
    private PainelAcoes         painelAcoes;
    
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
            try {
                this.controller.iniciaSimulacao();
            } catch(LogException ex){
                ex.generateLog();
                JOptionPane.showMessageDialog(this, "Houve um erro ao iniciar a simulação:\n" + ex.getMessage());
            }
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

    @Override
    /**
     * {@inheritdoc}
     */
    public void inicioGeracaoRede() {
        this.painelConfig.desabilitaConfiguracoes();
        this.painelAcoes.desabilitaAcao("iniciar");
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void sucessoGeracaoRede() {}

    @Override
    /**
     * {@inheritdoc}
     */
    public void erroGeracaoRede(LogException ex) {
        ex.generateLog();
        JOptionPane.showMessageDialog(this, "Houve um erro ao iniciar a simulação:\n" + ex.getMessage());
        this.painelAcoes.desabilitaAcao("iniciar");
    }
    
}
