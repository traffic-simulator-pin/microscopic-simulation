package br.udesc.ceavi.pin2;

import br.udesc.ceavi.pin2.control.IControleSimulacao;
import br.udesc.ceavi.pin2.control.ShellCommand;
import br.udesc.ceavi.pin2.control.ShellCommandLinux;
import br.udesc.ceavi.pin2.control.ShellCommandWindows;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.OSUtils;
import br.udesc.ceavi.pin2.view.FrameDetalhes;
import br.udesc.ceavi.pin2.view.FramePrincipal;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe Principal da Aplicação.
 * 
 * @author Jeferson Penz
 */
public class SimulacaoMicroscopica {
    
    public static final String NOME_APLICACAO   = "Simulação Microscópica - Projeto Integrador";
    public static final String EXTENSAO_ARQUIVO = "osm";
    public static final String FORMATO_DATA     = "yyyy.MM.dd.HH.mm.ss";
    public static final Color  COR_FUNDO        = new Color(245, 245, 245);
    public static final Color  COR_BORDA        = new Color(190, 190, 190);
    public static final Color  COR_SEPARADOR    = new Color(200, 200, 200);
    
    private static final OSUtils OPERATING_SYSTEM = new OSUtils();
    
    private final FramePrincipal frameAplicacao;
    private final FrameDetalhes  frameDetalhes;
    private String         workspaceFolder;
    private BufferedWriter geradorLogs;
    private BufferedWriter geradorLogsErros;
    
    /**
     * Cria uma nova instância para a simulação microscópica.
     */
    private SimulacaoMicroscopica(){
        this.frameAplicacao = new FramePrincipal();
        this.frameDetalhes  = new FrameDetalhes();
        File log  = new File(this.getNomeArquivoLogSistema());
        if(log.exists()){
            log.delete();
        }
        this.recriarGeradorLogs();
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
        this.log("Iniciando simulação.");
        this.frameAplicacao.carregaTelaExecucaoSimulacao();
    }
    
    /**
     * Termina a execução da simulação atual.
     */
    public void fechaSimulacao(){
        this.ocultaDetalhes();
        this.setWorkspaceFolder(null);
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
     * Retorna a pasta de trabalho do programa.
     * @return 
     */
    public String getWorkspaceFolder() {
        return workspaceFolder;
    }

    /**
     * Define a pasta de trabalho do programa.
     * @param workspaceFolder 
     */
    public void setWorkspaceFolder(String workspaceFolder) {
        this.workspaceFolder = workspaceFolder;
        this.recriarGeradorLogs();
    }

    /**
     * Retorna o sistema operacional do programa.
     * @return 
     */
    public OSUtils getOperatingSystem() {
        return OPERATING_SYSTEM;
    }
    
    private ShellCommand   shellCommand;
    /**
     * Retorna o shell para execução dos comandos.
     * @return 
     */
    public ShellCommand getShellCommand() {
        if (this.getOperatingSystem().isUnix()) {
            this.shellCommand = new ShellCommandLinux();
        }
        else if (this.getOperatingSystem().isWindows()) {
            this.shellCommand = new ShellCommandWindows();
        }
        else {
            // TODO lançar excessão de que o sistema não oferece suporte ao shell.
        }
        return this.shellCommand;
    }
    
    /**
     * Recria os arquivos de geração de logs de erros.
     */
    private void recriarGeradorLogs(){
        String nomeLog;
        String nomeErro;
        if(this.workspaceFolder != null && !this.workspaceFolder.isEmpty()){
            new File(this.workspaceFolder + File.separator + "logs").mkdir();
            nomeLog  = this.workspaceFolder + File.separator + "logs" + File.separator + "execution.log";
            nomeErro = this.workspaceFolder + File.separator + "logs" + File.separator + "errors.log";
        }
        else {
            nomeLog  = this.getNomeArquivoLogSistema();
            nomeErro = nomeLog;
        }
        try {
            this.geradorLogs      = new BufferedWriter(new FileWriter(nomeLog, true));
            this.geradorLogsErros = new BufferedWriter(new FileWriter(nomeErro, true));
        } catch (IOException ex) {
            System.out.println("Não foi possível gerar o arquivo de log: " + ex.getMessage());
        }
    }
    
    /**
     * Retorna o nome do arquivo de logs do sistema.
     */
    private String getNomeArquivoLogSistema(){
        String res = System.getProperty("java.io.tmpdir");
        if(res.charAt(res.length() - 1) != File.separatorChar){
             res += File.separator;
        }
        res += "microscopic-simulation.latest.log";
        return res;
    }
    
    /**
     * Armazena o log de uma excessão no arquivo de logs da aplicação.
     * @param exception 
     */
    public void log(LogException exception){
        this.log(LOG_TYPE.ERROR, exception.getLogData());
    }

    /**
     * Armazena um log de execução da aplicação no arquivo de logs da aplicação.
     * @param message 
     */
    public void log(String message) {
        this.log(LOG_TYPE.EXECUTION, message);
    }
    
    private final DateFormat dateFormatter = new SimpleDateFormat();
    public enum LOG_TYPE { EXECUTION, WARNING, ERROR }
    /**
     * Armazena a mensagem no arquivo de logs da aplicação.
     * @param type
     * @param message
     */
    public void log(LOG_TYPE type, String message){
        message = message.trim();
        message = dateFormatter.format(new Date()) + " - " + message;
        switch(type){
            case WARNING:
                message = "WARNING " + message;
                break;
            case ERROR:
                message = "ERROR " + message;
                break;
        }
        try {
            if(type == LOG_TYPE.ERROR){
                geradorLogsErros.write(message);
                geradorLogsErros.newLine();
                geradorLogsErros.flush();
            }
            else {
                geradorLogs.write(message);
                geradorLogs.newLine();
                geradorLogs.flush();
            }
        } catch (IOException ex) {
            System.out.println("Não foi possível escrever ao arquivo de logs.");
        }
    }
    
    /**
     * Método para inicialização da aplicação.
     * @param args 
     */
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SimulacaoMicroscopica.getInstance().log(LOG_TYPE.EXECUTION, "Carregou o L&F do Sistema");
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            SimulacaoMicroscopica.getInstance().log(LOG_TYPE.ERROR, "Não foi possível carregar o L&F do sistema.");
        }
        SimulacaoMicroscopica.getInstance().iniciaAplicacao();
    }
    
}
