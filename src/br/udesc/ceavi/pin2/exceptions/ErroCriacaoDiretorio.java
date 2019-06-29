package br.udesc.ceavi.pin2.exceptions;

import java.io.IOException;

/**
 * Excessão lançada quando não é possível criar o diretório.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public class ErroCriacaoDiretorio extends LogException{
    
    private String diretorioAlvo;
    private IOException excessaoOriginal;

    /**
     * Cria uma nova excessão para casos de erros de diretório.
     * @param diretorioAlvo
     * @param excessaoOriginal 
     */
    public ErroCriacaoDiretorio(String diretorioAlvo) {
        super();
        this.diretorioAlvo    = diretorioAlvo;
        this.excessaoOriginal = null;
    }
    
    /**
     * Cria uma nova excessão para casos de erros de diretório.
     * @param diretorioAlvo
     * @param excessaoOriginal 
     */
    public ErroCriacaoDiretorio(String diretorioAlvo, IOException excessaoOriginal) {
        super();
        this.diretorioAlvo    = diretorioAlvo;
        this.excessaoOriginal = excessaoOriginal;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "Não foi possível criar o diretório de origem.";
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getLogData() {
        return "Erro na criação do diretório de origem " + this.diretorioAlvo + (this.excessaoOriginal == null ? "" : (":\n" + this.excessaoOriginal.toString()));
    }
    
}
