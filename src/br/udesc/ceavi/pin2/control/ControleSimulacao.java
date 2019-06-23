package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.control.traci.ITraCIAdapter;
import br.udesc.ceavi.pin2.control.traci.TraCIObserver;
import br.udesc.ceavi.pin2.control.traci.TraasAdapter;
import br.udesc.ceavi.pin2.control.traci.Veiculo;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoTraCI;
import br.udesc.ceavi.pin2.exceptions.ErroInicioTraCI;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para realizar a execução da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ControleSimulacao implements IControleSimulacao, ShellListener, TraCIObserver{
    
    private final List<ObservadorSimulacao> observadores;
    private int portaSumo;
    private boolean logaDadosVeiculos;
    private ITraCIAdapter sumo;
    private Map<String, IControleVeiculo> veiculosAnalisados;
    
    /**
     * Cria um novo controlador para realizar a execução da simulação.
     */
    public ControleSimulacao() {
        this.observadores = new ArrayList<>();
        this.veiculosAnalisados = new HashMap<>();
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
            "sumo-gui --game --game.mode tls --start " + configuracoes +
            " -c " +SimulacaoMicroscopica.getInstance().trataEnderecoArquivo(new File(SimulacaoMicroscopica.getInstance().getWorkspaceFolder()).getAbsolutePath() + "/simulacao.sumocfg")  
        );
        terminal.start();
        SimulacaoMicroscopica.getInstance().exibeDetalhes(this);
        if(this.portaSumo != 0){
            this.iniciaTraCI();
        }
    }

    private void iniciaTraCI() throws ErroInicioTraCI {
        this.sumo = new TraasAdapter(this.portaSumo);
        this.sumo.addObservador(this);
        this.sumo.begin();
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
                case "logDadosVeiculos":
                    this.logaDadosVeiculos = value.equals("1");
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

    @Override
    public void onErroTraCI(ErroExecucaoTraCI ex) {
        this.observadores.forEach((observador) -> {
            observador.erroExecucaoSimulacao(ex);
        });
    }
    
    @Override
    public void adicionaVeiculoAnalisado(IControleVeiculo veiculo){
        this.veiculosAnalisados.put(veiculo.getIdVeiculo(), veiculo);
    }

    @Override
    public void onNovoVeiculo(Veiculo veiculo) {
        this.observadores.forEach((observador) -> {
            observador.entradaTraCI("Novo veículo: " + veiculo.getIdVeiculo());
        });
        this.observadores.forEach((observador) -> {
            observador.novoVeiculo(veiculo);
        });
    }

    @Override
    public void onDadoVeiculo(String dado, Veiculo veiculo) {
        this.veiculosAnalisados.get(veiculo.getIdVeiculo()).notificaObservadoresVeiculoAlterado();
        if(!this.logaDadosVeiculos){
            return;
        }
        this.observadores.forEach((observador) -> {
            observador.logTraCI("Recebeu " + dado + " do Veículo: " + veiculo.getIdVeiculo());
        });
    }
    
}
