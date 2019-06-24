package br.udesc.ceavi.pin2.exceptions;

/**
 * Excessão para erros de execução no TraCI
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class ErroExecucaoTraCI extends LogException{

    private Exception excessaoOriginal;

    /**
     * Cria uma nova excessão para erros ocorridos no TraCI.
     * @param excessaoOriginal
     */
    public ErroExecucaoTraCI(Exception excessaoOriginal) {
        super();
        this.excessaoOriginal = excessaoOriginal;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "Houve um erro de comunicação com o servidor TraCI.";
    }
    
    @Override
    /**
     * {@inheritdoc}
     */
    public String getLogData() {
        return "Erro de comunicação com o TraCI: " + this.excessaoOriginal.toString();
    }
    
}
