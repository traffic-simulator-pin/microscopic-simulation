package br.udesc.ceavi.pin2.exceptions;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Representa um erro na extensão de um arquivo que tentou ser carregado.
 * @author Jeferson Penz
 */
public class ExtensaoArquivoInvalida extends Exception{
    
    private String extensaoOriginal;
    private String[] extensaoValida;

    /**
     * Cria uma nova excessão devido a extensão de um arquivo que tentou ser carregado.
     * @param extensaoOriginal - Extensão do arquivo original.
     * @param extensaoValida   - Extensões aceitas.
     */
    public ExtensaoArquivoInvalida(String extensaoOriginal, String... extensaoValida) {
        this.extensaoOriginal = extensaoOriginal;
        this.extensaoValida   = extensaoValida;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "A extensão do arquivo (." + this.extensaoOriginal + ") é inválida, apenas são aceitos arquivos " + Arrays.toString(extensaoValida) + ".";
    }
    
}
