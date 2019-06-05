package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Painel padrão para gerenciamento de ações da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public class PainelAcoes extends JPanel{
    
    private Map<String, JButton> acoes;

    /**
     * Cria um novo painel de ações para a aplicação.
     */
    public PainelAcoes() {
        super();
        this.iniciaPropriedades();
    }
    
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.acoes = new HashMap<>();
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Border bordaInferior = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0),
                                                                  BorderFactory.createMatteBorder(0, 0, 2, 0, SimulacaoMicroscopica.COR_BORDA));
        this.setBorder(BorderFactory.createCompoundBorder(bordaInferior, BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
    
    /**
     * Adiciona uma nova ação ao painel para realização do evento especificado.
     * @param identificador
     * @param titulo
     * @param clique 
     */
    public void adicionaAcao(String identificador, String titulo, ActionListener clique){
        JButton botaoAcao = new JButton(titulo);
        botaoAcao.addActionListener(clique);
        this.add(botaoAcao);
        this.acoes.put(identificador, botaoAcao);
    }
    
    /**
     * Habilita a ação do painel de ações.
     * @param identificador 
     */
    public void habilitaAcao(String identificador){
        JButton acao = this.acoes.get(identificador);
        if(acao != null){
            acao.setEnabled(true);
        }
    }
    
    /**
     * Desabilita a ação do painel de ações.
     * @param identificador 
     */
    public void desabilitaAcao(String identificador){
        JButton acao = this.acoes.get(identificador);
        if(acao != null){
            acao.setEnabled(false);
        }
    }
    
    /**
     * Exibe uma ação oculta no painel de ações.
     * @param identificador 
     */
    public void exibeAcao(String identificador){
        JButton acao = this.acoes.get(identificador);
        if(acao != null){
            acao.setVisible(true);
        }
    }
    
    /**
     * Oculta uma ação no painel de ações.
     * @param identificador 
     */
    public void ocultaAcao(String identificador){
        JButton acao = this.acoes.get(identificador);
        if(acao != null){
            acao.setVisible(false);
        }
    }
    
}
