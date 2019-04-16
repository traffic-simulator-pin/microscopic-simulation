package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Painel para realizar a configuração dos dados da simulação.
 * @author Jeferson Penz
 */
public class PainelConfiguracoes extends JPanel{
    
    private List<JComponent> configuracoes;

    /**
     * Cria um novo painel para realizar a configuracao dos dados da simulação.
     */
    public PainelConfiguracoes() {
        super();
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }

    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades() {
        this.configuracoes = new ArrayList<>();
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, SimulacaoMicroscopica.COR_BORDA),
                                                          BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Realiza a inicialização dos componentes da tela.
     */
    private void iniciaComponentes() {
        JLabel titulo = new JLabel("Propriedades da Simulação");
        titulo.setFont(new Font(titulo.getFont().getName(), Font.BOLD, 12));
        JComboBox<String> densidade = new JComboBox<>(new String[] { "100%", "50%", "20%", "10%" });
        densidade.setSelectedItem("50%");
        JComboBox<String> velocidade = new JComboBox<>(new String[] { "200%", "100%", "50%", "25%" });
        velocidade.setSelectedItem("100%");
        this.add(titulo);
        this.add(this.criaPainelConfiguracao("Densidade",  densidade, 25));
        this.add(this.criaPainelConfiguracao("Velocidade", velocidade, 25));
    }
    
    /**
     * Cria um novo painel para alterar uma das configurações do simulador.
     * @param label
     * @param configuracao
     * @return 
     */
    private JPanel criaPainelConfiguracao(String label, JComponent configuracao){
        return this.criaPainelConfiguracao(label, configuracao, 0);
    }
    
    /**
     * Cria um novo painel para alterar uma das configurações do simulador.
     * @param label
     * @param configuracao
     * @return 
     */
    private JPanel criaPainelConfiguracao(String label, JComponent configuracao, int alturaMaxima){
        this.configuracoes.add(configuracao);
        JPanel painelConfiguracao = new JPanel();
        painelConfiguracao.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        painelConfiguracao.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SimulacaoMicroscopica.COR_SEPARADOR),
                                                                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        painelConfiguracao.setLayout(new BorderLayout(0, 0));
        JLabel labelConfiguracao = new JLabel(label);
        painelConfiguracao.add(labelConfiguracao, BorderLayout.WEST);
        painelConfiguracao.add(configuracao,      BorderLayout.EAST);
        if(alturaMaxima > 0){
            painelConfiguracao.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaMaxima + 10));
        }
        return painelConfiguracao;
    }
    
    /**
     * Desabilita o painel de configurações.
     */
    public void desabilitaConfiguracoes(){
        this.configuracoes.forEach((configuracao) -> {
            configuracao.setEnabled(false);
        });
    }
    
    /**
     * Desabilita o painel de configurações.
     */
    public void habilitaConfiguracoes(){
        this.configuracoes.forEach((configuracao) -> {
            configuracao.setEnabled(true);
        });
    }
    
}
