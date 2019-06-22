package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.exceptions.ErroInicioTraCI;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;
import it.polito.appeal.traci.SumoTraciConnection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controlador para realizar a execução da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ControleSimulacao implements IControleSimulacao, ShellListener{
    
    private static final int NUMERO_MAXIMO_TENTATIVAS_TRACI = 10;
    
    private final List<ObservadorSimulacao> observadores;
    private int portaSumo;
    private StringBuilder detalhesExecucao;
    
    /**
     * Cria um novo controlador para realizar a execução da simulação.
     */
    public ControleSimulacao() {
        this.observadores = new ArrayList<>();
    }
    
    /**
     * {@inheritdoc}
     * @throws br.udesc.ceavi.pin2.exceptions.LogException
     */
    @Override
    public void iniciaSimulacao() throws LogException{
        SimulacaoMicroscopica.getInstance().log("Iniciando execução da Simulação.");
        String configuracoes = this.getConfiguracoesFormatadas();
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
            "sumo-gui --game --game.mode tls " + configuracoes +
            " -c " +SimulacaoMicroscopica.getInstance().trataEnderecoArquivo(new File(SimulacaoMicroscopica.getInstance().getWorkspaceFolder()).getAbsolutePath() + "/simulacao.sumocfg")  
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().exibeDetalhes(this);
        if(this.portaSumo != 0){
            this.iniciaTraCI();
        }
    }

    private void iniciaTraCI() throws ErroInicioTraCI {
        int tentativa            = 0;
        Exception ultErro        = null;
        SumoTraciConnection sumo = null;
        while(tentativa < NUMERO_MAXIMO_TENTATIVAS_TRACI && sumo == null){
            try {
                sumo = new SumoTraciConnection(this.portaSumo);
            } catch (IOException | InterruptedException ex) {
                ultErro = ex;
            }
        }
        if(sumo == null){
            throw new ErroInicioTraCI(ultErro);
        }
        sumo.addOption("start", "1"); // auto-run on GUI show
        try {
            sumo.runServer();
        } catch (IOException ex) {
            throw new ErroInicioTraCI(ex);
        }
    }
    
    private String getConfiguracoesFormatadas(){
        StringBuilder config = new StringBuilder();
        for (Map.Entry<String, String> entry : SimulacaoMicroscopica.getInstance().getConfiguracoes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch(key){
                case "velocidade":
                    config.append("--step-length ").append(Integer.parseInt(value) / 1000f);
                    break;
                case "porta":
                    this.portaSumo = Integer.parseInt(value);
                    config.append("--remote-port ").append(value);
                    config.append(" --num-clients 1");
                    break;
                default:
                    break;
            }
            config.append(" ");
        }
        return config.toString();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void finalizaSimulacao() {
        SimulacaoMicroscopica.getInstance().log("Finalizando simulação.");
        SimulacaoMicroscopica.getInstance().fechaSimulacao();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void anexaObservador(ObservadorSimulacao observador) {
        this.observadores.add(observador);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void desanexaObservador(ObservadorSimulacao observador){
        this.observadores.remove(observador);
    }

    @Override
    public void onCommandException(ErroExecucaoCommando ex) {
        this.observadores.forEach((observador) -> {
            observador.erroExecucaoSimulacao(ex);
        });
        this.finalizaSimulacao();
    }

    @Override
    public void onCommandSucess(String retorno) {
        SimulacaoMicroscopica.getInstance().log("Retorno:\n" + retorno);
        this.observadores.forEach((observador) -> {
            observador.sucessoExecucaoSimulacao();
        });
    }
    
}
