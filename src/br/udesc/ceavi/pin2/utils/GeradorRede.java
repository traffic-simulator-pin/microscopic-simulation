package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;
import java.io.File;

/**
 * Classe responsável por gerar os arquivos de rede de tráfego.
 * @author jpedroschmitz
 */
public class GeradorRede implements Runnable, ShellListener{
    
    private File arquivoSimulacao;
    private int etapaAtual;
    
    public GeradorRede(File arquivoSimulacao){
        this.arquivoSimulacao = arquivoSimulacao;
    }

    @Override
    public void run() {
        this.etapaAtual = 1;
        this.executaEtapaGeracao(this.etapaAtual);
    }
    
    private void executaEtapaGeracao(int etapa){
        switch(etapa){
            case 1:
                this.criaRedeTrafego();
                break;
            case 2:
                this.criarArquivoDePOI();
                break;
            case 3:
                // NOTIFICAR FINALIZAÇÃO SUCESSO
            default:
                // NOTIFICAR FINALIZAÇÃO ERRO
        }
    }
    
    /**
     * Realiza a criação dos arquivos da rede de tráfego.
     */
    private void criaRedeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
             "netconvert --osm-files " + this.arquivoSimulacao.getAbsolutePath() + " --geometry.remove --roundabouts.guess --ramps.guess --junctions.join --tls.guess-signals --tls.discard-simple --tls.join -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + "/rede.net.xml",
             SimulacaoMicroscopica.getInstance().getOperatingSystem().isWindows() ? "dir" : "ls"
        );
        terminal.start();
    }
    
    /**
     * Cria o arquivo de pontos de interesse para a rede.
     */
    private void criarArquivoDePOI() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração de arquivo de pontos de interesse.");
        File polygonUtil = new File("src/br/udesc/ceavi/pin2/utils/osmPolyconvert.typ.xml");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
             "polyconvert --net-file " + this.arquivoSimulacao.getAbsolutePath() + " --osm-files " + this.arquivoSimulacao.getAbsolutePath() + " --type-file " + polygonUtil.getAbsolutePath() + " -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + "/poi.net.xml",
             SimulacaoMicroscopica.getInstance().getOperatingSystem().isWindows() ? "dir" : "ls"
        );
        terminal.start();
    }

    @Override
    public void onCommandException(ErroExecucaoCommando ex) {
        this.etapaAtual = -1;
        this.executaEtapaGeracao(this.etapaAtual);
    }

    @Override
    public void onCommandSucess(String retorno) {
        this.etapaAtual++;
        this.executaEtapaGeracao(this.etapaAtual);
    }
    
    /**
     * Retorna se a execução foi finalizada.
     * @return 
     */
    public synchronized boolean getExecucaoFinalizada(){
        return this.etapaAtual < 1;
    }
    
    /**
     * Retorna se ocorreu um erro na execução.
     * @return 
     */
    public synchronized boolean getExecucaoErro(){
        return this.etapaAtual == -1;
    }
}
