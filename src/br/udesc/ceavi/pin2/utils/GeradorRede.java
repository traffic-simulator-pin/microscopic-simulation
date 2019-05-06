package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import java.io.File;

/**
 * Classe responsável por gerar os arquivos de rede de tráfego.
 * @author jpedroschmitz
 */
public class GeradorRede extends ExecucaoMultiEtapas{
    
    private final File arquivoSimulacao;
    
    /**
     * Cria um novo controle para geração do arquivo da rede
     * @param arquivoSimulacao 
     */
    public GeradorRede(File arquivoSimulacao){
        this.arquivoSimulacao = arquivoSimulacao;
    }
    
    /**
     * {@inheritdoc}
     */
    @Override
    protected void executaEtapa(int etapa){
        switch(etapa){
            case 1:
                this.criaRedeTrafego();
                break;
            case 2:
                this.criarArquivoDePOI();
                break;
            // Adicionar mais etapas.
        }
    }
    
    /**
     * Realiza a criação dos arquivos da rede de tráfego.
     */
    private void criaRedeTrafego() {
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
             "netconvert --osm-files " + this.arquivoSimulacao.getAbsolutePath() + " --geometry.remove --roundabouts.guess --ramps.guess --junctions.join --tls.guess-signals --tls.discard-simple --tls.join -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + File.separator + "rede.net.xml"
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
             "polyconvert --net-file " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + File.separator + "rede.net.xml --osm-files " + this.arquivoSimulacao.getAbsolutePath() + " --type-file " + polygonUtil.getAbsolutePath() + " -o " + SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + File.separator + "poi.net.xml"
        );
        terminal.start();
    }
    
    @Override
    /**
     * {@inheritdoc}
     */
    protected int getTotalEtapas(){
        return 2;
    }
}
