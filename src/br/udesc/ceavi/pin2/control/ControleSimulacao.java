package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para realizar a execução da simulação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public class ControleSimulacao implements IControleSimulacao, ShellListener{
    
    private final List<ObservadorSimulacao> observadores;
    
    /**
     * Cria um novo controlador para realizar a execução da simulação.
     */
    public ControleSimulacao() {
        this.observadores = new ArrayList<>();
        this.iniciaSimulacao();
    }
    
    private void iniciaSimulacao(){
        SimulacaoMicroscopica.getInstance().log("Iniciando geração da rede de tráfego.");
        Thread terminal = SimulacaoMicroscopica.getInstance().getShellCommand().getNewShell(this,
            "sumo-gui -n " + SimulacaoMicroscopica.getInstance().trataEnderecoArquivo(SimulacaoMicroscopica.getInstance().getWorkspaceFolder() + "/rede.net.xml") +
                    " --game --game.mode tsl --window-size 1000,400 --window-position "
        );
        terminal.start();
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
        this.finalizaSimulacao();
    }

    @Override
    public void onCommandSucess(String retorno) {
        
    }
    
}
