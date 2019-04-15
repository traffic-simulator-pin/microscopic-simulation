package br.udesc.ceavi.pin2;

import br.udesc.ceavi.pin2.control.IControleSimulacao;
import br.udesc.ceavi.pin2.view.FrameDetalhes;
import br.udesc.ceavi.pin2.view.FramePrincipal;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe Principal da Aplicação.
 * 
 * @author Jeferson Penz
 */
public class SimulacaoMicroscopica {
    
    public static final String NOME_APLICACAO = "Simulação Microscópica - Projeto Integrador";
    public static final String EXTENSAO_ARQUIVO = "xml";
    public static final String FORMATO_DATA = "yyyy.MM.dd.HH.mm.ss";
    public static final Color  COR_FUNDO      = new Color(245, 245, 245);
    public static final Color  COR_BORDA      = new Color(190, 190, 190);
    public static final Color  COR_SEPARADOR  = new Color(200, 200, 200);
    
    private FramePrincipal frameAplicacao;
    private FrameDetalhes  frameDetalhes;
    
    /**
     * Cria uma nova instância para a simulação microscópica.
     */
    private SimulacaoMicroscopica(){
        this.frameAplicacao = new FramePrincipal();
        this.frameDetalhes  = new FrameDetalhes();
    }
    
    private static SimulacaoMicroscopica instance;
    /**
     * Retorna a atual ou uma nova (caso não exista) instância da Aplicação.
     * @return 
     */
    public static SimulacaoMicroscopica getInstance(){
        if(instance == null){
            instance = new SimulacaoMicroscopica();
        }
        return instance;
    }
    
    /**
     * Inicia a tela de simulação.
     */
    public void iniciaAplicacao(){
        this.frameAplicacao.carregaTelaInicialSimulacao();
    }
    
    /**
     * Inicia a simulação com base nos dados fornecidos.
     * @param dadosSimulacao
     * @param configuracoes 
     */
    public void iniciaSimulacao(Object dadosSimulacao, Object configuracoes){
        this.frameAplicacao.carregaTelaExecucaoSimulacao();
    }
    
    /**
     * Termina a execução da simulação atual.
     */
    public void fechaSimulacao(){
        this.ocultaDetalhes();
        this.iniciaAplicacao();
    }
    
    /**
     * Exibe a janela de detalhes da simulação utilizando dos dados do controle especificado.
     * @param controle 
     */
    public void exibeDetalhes(IControleSimulacao controle){
        this.frameDetalhes.carregaDetalhes(controle);
        this.frameDetalhes.setVisible(true);
    }
    
    /**
     * Oculta a janela de detalhes da simulação.
     */
    public void ocultaDetalhes(){
        this.frameDetalhes.setVisible(false);
    }
    
    /**
     * Método para inicialização da aplicação.
     * @param args 
     */
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("Não foi possível carregar o L&F do sistema.");
        }
        SimulacaoMicroscopica.getInstance().iniciaAplicacao();
    }
    
}
