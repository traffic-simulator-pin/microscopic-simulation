package br.udesc.ceavi.pin2.exceptions;

/**
 * Excessão para erros de execução no TraCI
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class ErroInicioTraCI extends LogException{

    private Exception excessaoOriginal;

    /**
     * Cria uma nova excessão para erros ocorridos no TraCI.
     * @param excessaoOriginal
     */
    public ErroInicioTraCI(Exception excessaoOriginal) {
        super();
        this.excessaoOriginal = excessaoOriginal;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "Não foi possivel iniciar o servidor. Verifique o estado da porta e tente novamente.";
    }
    
    @Override
    /**
     * {@inheritdoc}
     */
    public String getLogData() {
        return "Erro de comunicação com o TraCI: " + this.excessaoOriginal.toString();
    }
    
}
