package br.udesc.ceavi.pin2.control.traci;

import br.udesc.ceavi.pin2.utils.PosicaoVeiculo;
import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ErroExecucaoTraCI;
import br.udesc.ceavi.pin2.exceptions.ErroInicioTraCI;
import de.tudresden.sumo.config.Constants;
import de.tudresden.sumo.subscription.ResponseType;
import de.tudresden.sumo.subscription.SubscribtionVariable;
import de.tudresden.sumo.subscription.SubscriptionObject;
import de.tudresden.sumo.subscription.VariableSubscription;
import de.tudresden.ws.container.SumoPosition2D;
import de.tudresden.ws.container.SumoPrimitive;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class TraasAdapter implements ITraCIAdapter, Observer{
    
    private static final int NUMERO_MAXIMO_TENTATIVAS_TRAAS = 10;
    
    private Map<String, Veiculo> veiculos;
    private SumoTraciConnection sumo;
    private List<TraCIObserver> observadores;
    
    /**
     * Cria um novo adaptador que utiliza o Traas para se comunicar com o SUMO.
     * @param portaSumo
     * @throws ErroInicioTraCI 
     */
    public TraasAdapter(int portaSumo) throws ErroInicioTraCI{
        this.veiculos = new HashMap<>();
        this.observadores = new ArrayList<>();
        int tentativa            = 0;
        Exception ultErro        = null;
        while(tentativa < NUMERO_MAXIMO_TENTATIVAS_TRAAS && sumo == null){
            try {
                this.sumo = new SumoTraciConnection(portaSumo);
            } catch (IOException | InterruptedException ex) {
                ultErro = ex;
            }
        }
        if(this.sumo == null){
            throw new ErroInicioTraCI(ultErro);
        }
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void begin() throws ErroInicioTraCI{
        try {
            this.sumo.runServer();
        } catch (IOException ex) {
            throw new ErroInicioTraCI(ex);
        }
        this.sumo.addObserver(this);
        
        // Nos inscrevemos para receber os ids de veículos que partem.
        VariableSubscription variaveis = new VariableSubscription(SubscribtionVariable.simulation,0,100000 * 60, "");
        variaveis.addCommand(Constants.VAR_DEPARTED_VEHICLES_IDS);
        try {
            this.sumo.do_subscription(variaveis);
        } catch (Exception ex) {
            throw new ErroInicioTraCI(ex);
        }
        
        Thread sumoThread = new Thread(() -> {
            while(!sumo.isClosed()){
                if(!SimulacaoMicroscopica.getInstance().isExecutando()){
                    sumo.close();
                }
                try {
                    sumo.do_timestep();
                    Thread.sleep(10);
                } catch (Exception ex) {}
            }
        });
        sumoThread.start();
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public Map<String, Veiculo> getAllVeiculos() {
        return veiculos;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void update(Observable observado, Object variavel) {
        if(variavel.getClass() == SubscriptionObject.class){
            SubscriptionObject objetoVariavel = (SubscriptionObject) variavel;
            try {
                this.trataObjetoVariavel(objetoVariavel);
            } catch (Exception ex){
                this.notificaObservadoresErroTraCI(new ErroExecucaoTraCI(ex));
            }
        }
    }

    /**
     * Realiza o tratamento de um objeto de variável retornado pelo Traas.
     * @param objetoVariavel 
     */
    private void trataObjetoVariavel(SubscriptionObject objetoVariavel) throws Exception {
        // Se ele retornou uma variável de simulação.
        if (objetoVariavel.response == ResponseType.SIM_VARIABLE) {
            // Se esta variável for o ID dos veículos.
            if(objetoVariavel.variable == Constants.VAR_DEPARTED_VEHICLES_IDS){
                // Analisa o retorno.
                SumoStringList textoSumo = (SumoStringList) objetoVariavel.object;
                if(textoSumo.size() > 0) {
                    for (String idVeiculo : textoSumo) {
                        Veiculo novoVeiculo = new Veiculo(idVeiculo);
                        this.veiculos.put(idVeiculo, novoVeiculo);
                        this.notificaObservadoresNovoVeiculo(novoVeiculo);
                        // Se inscreve para receber os dados do veículo.
                        VariableSubscription inscricaoVariavel = new VariableSubscription(SubscribtionVariable.vehicle,0,100000 * 60, idVeiculo);
                        inscricaoVariavel.addCommand(Constants.VAR_POSITION);
                        inscricaoVariavel.addCommand(Constants.VAR_SPEED);
                        this.sumo.do_subscription(inscricaoVariavel);
                    }
                }
            }
        }
        // Se forem os dados do Veículo
        else if (objetoVariavel.response == ResponseType.VEHICLE_VARIABLE) {
            Veiculo veiculo = this.veiculos.get(objetoVariavel.id);
            if (objetoVariavel.variable == Constants.VAR_SPEED) {
                SumoPrimitive sp = (SumoPrimitive) objetoVariavel.object;
                veiculo.setVelocidadeVeiculo((double) sp.val);
                this.notificaObservadoresDadosVeiculo("Velocidade: " + sp.val, veiculo);
            } 
            else if (objetoVariavel.variable == Constants.VAR_POSITION) {
                SumoPosition2D sc = (SumoPosition2D) objetoVariavel.object;
                PosicaoVeiculo pos = new PosicaoVeiculo(sc.x, sc.y);
                veiculo.setPosicaoVeiculo(pos);
                this.notificaObservadoresDadosVeiculo("Posição: " + pos, veiculo);
            }
        }
    }

    /**
     * Notifica os observadores de um erro.
     * @param ex 
     */
    private void notificaObservadoresErroTraCI(ErroExecucaoTraCI ex) {
        this.observadores.forEach((observador) -> {
            observador.onErroTraCI(ex);
        });
    }

    /**
     * Notifica os observadores de um novo veiculo.
     * @param novoVeiculo 
     */
    private void notificaObservadoresNovoVeiculo(Veiculo novoVeiculo) {
        this.observadores.forEach((observador) -> {
            observador.onNovoVeiculo(novoVeiculo);
        });
    }

    private void notificaObservadoresDadosVeiculo(String dado, Veiculo veiculo) {
        this.observadores.forEach((observador) -> {
            observador.onDadoVeiculo(dado, veiculo);
        });
    }
    
    @Override
    /**
     * {@inheritdoc}
     */
    public void addObservador(TraCIObserver observador){
        this.observadores.add(observador);
    }
    
}
