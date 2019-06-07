package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.io.File;

/**
 * Interface para realizar o controle da tela inicial da aplicação.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public interface IControleInicial {
    
    /**
     * Ordena o controlador para iniciar a busca de um novo arquivo para simulação.
     * @param arquivo
     * @throws br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida
     */
    public void iniciaCarregamentoNovoArquivo(File arquivo) throws ExtensaoArquivoInvalida;
    
    /**
     * Ordena ao controlador para iniciar a simulação de dados com base no arquivo carregado.
     * @throws br.udesc.ceavi.pin2.exceptions.LogException
     */
    public void iniciaSimulacao(String densidade, String velocidade) throws LogException;
    
    /**
     * Anexa um observador ao controlador.
     * @param observador 
     */
    public void anexaObservador(ObservadorInicial observador);
    
    /**
     * Desanexa um observador do controlador
     * @param observador 
     */
    public void desanexaObservador(ObservadorInicial observador);
    
}
