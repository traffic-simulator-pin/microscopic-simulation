package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.control.IControleSimulacao;
import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleSimulacao;
import br.udesc.ceavi.pin2.control.ObservadorSimulacao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Painel para a tela de simulação da aplicação.
 * @author Jeferson Penz
 */
public class PainelSimulacao extends JPanel implements ObservadorSimulacao{
    
    private IControleSimulacao controller;
    private JPanel             painelConfig;
    private JPanel             painelSimulacao;
    private PainelAcoes        painelAcoes;
    
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
        this.controller.anexaObservador(this);
        
        this.setBorder(BorderFactory.createLineBorder(SimulacaoMicroscopica.COR_BORDA, 2));
        this.setLayout(new BorderLayout(0, 0));
    }
    
    /**
     * Realiza a inicialização dos componentes do painel.
     */
    private void iniciaComponentes() {
        this.painelConfig    = new PainelConfiguracoes();
        this.painelSimulacao = new JPanel();
        this.painelSimulacao.setBackground(new Color(100, 100, 100));
        this.painelSimulacao.add(new JLabel("Aqui estaria a simulação?")); // TODO REMOVER
        this.painelAcoes     = new PainelAcoes();
        this.painelAcoes.adicionaAcao("finalizar", "Finalizar Simulação", (ActionEvent e) -> {
            this.controller.finalizaSimulacao();
        });
        this.painelAcoes.adicionaAcao("detalhar", "Detalhes da Simulação", (ActionEvent e) -> {
            SimulacaoMicroscopica.getInstance().exibeDetalhes(this.controller);
        });
        this.add(painelConfig,    BorderLayout.NORTH);
        this.add(painelSimulacao, BorderLayout.CENTER);
        this.add(painelAcoes,     BorderLayout.SOUTH);
    }
}
