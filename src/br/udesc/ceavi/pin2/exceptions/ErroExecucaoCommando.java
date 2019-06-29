package br.udesc.ceavi.pin2.exceptions;

/**
 * Representa erros ocorridos durante uma tentativa de execução dos comandos.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ErroExecucaoCommando extends LogException{

    private String commando;
    private Exception excessaoOriginal;

    /**
     * Cria uma nova excessão para erros ocorridos na execução do commando.
     * @param commando
     */
    public ErroExecucaoCommando(String commando) {
        super();
        this.commando = commando;
        this.excessaoOriginal = null;
    }

    /**
     * Cria uma nova excessão para erros ocorridos na execução do commando.
     * @param commando
     * @param excessaoOriginal 
     */
    public ErroExecucaoCommando(String commando, Exception excessaoOriginal) {
        this.commando = commando;
        this.excessaoOriginal = excessaoOriginal;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "Não foi possivel processar o commando.";
    }
    
    @Override
    /**
     * {@inheritdoc}
     */
    public String getLogData() {
        return "Erro na execução do commando " + this.commando + (this.excessaoOriginal == null ? "" : (":\n" + this.excessaoOriginal.toString()));
    }
    
}
