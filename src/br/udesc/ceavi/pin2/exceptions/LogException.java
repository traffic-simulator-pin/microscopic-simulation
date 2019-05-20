package br.udesc.ceavi.pin2.exceptions;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;

/**
 * Classe para excessões quais geram log de dados. Ao capturar esta excessão, gere o log para o arquivo utilizando do
 * método "generateLog".
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public abstract class LogException extends Exception{
    
    /**
     * Realiza a geração e armazenamento do log de erros.
     */
    public void generateLog(){
        SimulacaoMicroscopica.getInstance().log(this);
    }
    
    /**
     * Retorna os dados para geração do log.
     * @return 
     */
    public abstract String getLogData();
    
}
