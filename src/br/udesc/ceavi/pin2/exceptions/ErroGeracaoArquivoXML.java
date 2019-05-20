package br.udesc.ceavi.pin2.exceptions;

/**
 * Indica algum erro na geração do arquivo XML.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ErroGeracaoArquivoXML extends LogException{
    
    private Exception excessaoOriginal;
    
    /**
     * Realiza a criação de uma nova excessão para erros na geração de arquivos.
     * @param excessaoOriginal 
     */
    public ErroGeracaoArquivoXML(Exception excessaoOriginal){
        super();
        this.excessaoOriginal = excessaoOriginal;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public String getMessage() {
        return "Não foi possível gerar o arquivo XML.";
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public String getLogData() {
        return "Erro na geração do arquivo XML: " + excessaoOriginal.toString();
    }
    
}
