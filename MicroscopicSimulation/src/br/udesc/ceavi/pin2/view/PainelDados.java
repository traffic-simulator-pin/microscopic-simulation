package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.ControleSimulacao;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Painel para os dados da simulação
 * @author Jeferson Penz
 */
public class PainelDados extends JPanel{
    
    private JPanel panelDados;

    /**
     * Cria um novo painel para conter os dados da simulação.
     */
    public PainelDados() {
        super();
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }
    
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));
    }
    
    /**
     * Realiza a inicialização dos componentes do painel.
     */
    private void iniciaComponentes() {
        JLabel titulo = new JLabel("Dados da Simulação:");
        titulo.setFont(new Font(titulo.getFont().getName(), Font.BOLD, 12));
        titulo.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        titulo.setPreferredSize(new Dimension(Integer.MAX_VALUE, 20));
        titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.panelDados = new JPanel();
        this.panelDados.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.panelDados.setLayout(new BoxLayout(this.panelDados, BoxLayout.Y_AXIS));
        this.add(titulo);
        this.add(new JScrollPane(this.panelDados));
    }
    
    /**
     * Adiciona um dado da simulação a tela. Os dados podem ser fornecidos em formato de HTML.
     * @param titulo
     * @param dados 
     */
    public void adicionaDados(String titulo, String dados){
        JPanel panel = new JPanel();
        panel.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SimulacaoMicroscopica.COR_SEPARADOR));
        panel.setLayout(new BorderLayout(5, 0));
        JLabel labelTitulo = new JLabel(titulo);
        JLabel labelDados  = new JLabel("<html>" + dados + "</html>");
        panel.add(labelTitulo, BorderLayout.WEST);
        panel.add(labelDados,  BorderLayout.CENTER);
        this.panelDados.add(panel);
    }
    
    /**
     * Limpa os dados da tela.
     */
    public void limpaDados(){
        this.panelDados.removeAll();
    }
    
}
