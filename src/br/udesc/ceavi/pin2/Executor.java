package br.udesc.ceavi.pin2;

/**
 * Classe Executora da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class Executor implements Runnable{

    @Override
    public void run() {
        if(SimulacaoMicroscopica.getInstance().isExecutando()){
            SimulacaoMicroscopica.getInstance().fechaAplicacao();
        }
        SimulacaoMicroscopica.getInstance().iniciaAplicacao();
    }
    
}
