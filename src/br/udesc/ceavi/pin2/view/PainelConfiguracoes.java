package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.CampoDesconhecidoException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

/**
 * Painel para realizar a configuração dos dados da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class PainelConfiguracoes extends JPanel{
    
    private Map<String, JComponent> configuracoes;

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
        this.configuracoes = new HashMap<>();
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
        JComboBox<String> densidade = new JComboBox<>(new String[] { "1000", "500", "100", "50" });
        densidade.setSelectedItem("500");
        JComboBox<String> velocidade = new JComboBox<>(new String[] { "200", "100", "50", "25" });
        velocidade.setSelectedItem("100");
        JFormattedTextField porta = this.getConfiguracaoPorta();
        porta.setText("6060");
        porta.setPreferredSize(new Dimension(100, 10));
        JCheckBox logDadosVei = new JCheckBox();
        this.add(titulo);
        this.add(this.criaPainelConfiguracao("densidade", "Densidade",  densidade, 25));
        this.add(this.criaPainelConfiguracao("velocidade", "Velocidade", velocidade, 25));
        this.add(this.criaPainelConfiguracao("porta", "Porta", porta, 25));
        this.add(this.criaPainelConfiguracao("logDadosVeiculos", "Gerar log de Dados dos Veículos", logDadosVei, 20));
    }
    
    private JFormattedTextField getConfiguracaoPorta(){
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1000);
        formatter.setMaximum(9999);
        formatter.setAllowsInvalid(false);
        return new JFormattedTextField(format);
    }
    
    /**
     * Cria um novo painel para alterar uma das configurações do simulador.
     * @param label
     * @param configuracao
     * @return 
     */
    private JPanel criaPainelConfiguracao(String id, String label, JComponent configuracao){
        return this.criaPainelConfiguracao(id, label, configuracao, 0);
    }
    
    /**
     * Cria um novo painel para alterar uma das configurações do simulador.
     * @param label
     * @param configuracao
     * @return 
     */
    private JPanel criaPainelConfiguracao(String id, String label, JComponent configuracao, int alturaMaxima){
        this.configuracoes.put(id, configuracao);
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
        this.configuracoes.entrySet().stream().map((entry) -> {
            return entry;
        }).map((entry) -> entry.getValue()).forEachOrdered((value) -> {
            value.setEnabled(false);
        });
    }
    
    /**
     * Desabilita o painel de configurações.
     */
    public void habilitaConfiguracoes(){
        this.configuracoes.entrySet().stream().map((entry) -> {
            return entry;
        }).map((entry) -> entry.getValue()).forEachOrdered((value) -> {
            value.setEnabled(true);
        });
    }
    
    public Map<String, String> getAllConfiguracoes() throws CampoDesconhecidoException{
        Map<String, String> configuracao = new HashMap<>();
        for (Map.Entry<String, JComponent> entry : this.configuracoes.entrySet()) {
            String valor          = "";
            String key            = entry.getKey();
            JComponent componente = entry.getValue();
            switch(componente.getClass().getCanonicalName()){
                case "javax.swing.JComboBox":
                    valor = ((String)((JComboBox<String>) componente).getSelectedItem());
                    break;
                case "javax.swing.JTextField":
                case "javax.swing.JFormattedTextField":
                    valor = ((JTextField) componente).getText();
                    break;
                case "javax.swing.JCheckBox":
                    valor = ((JCheckBox) componente).isSelected() ? "1" : "0";
                    break;
                default:
                    throw new CampoDesconhecidoException(componente);
            }
            configuracao.put(key, valor);
        }
        return configuracao;
    }
    
}
