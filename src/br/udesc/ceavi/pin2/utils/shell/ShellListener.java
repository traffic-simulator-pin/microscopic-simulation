package br.udesc.ceavi.pin2.utils.shell;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;

/**
 * Interface para implementação por classes que executam comandos em Shell e processam o retorno.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public interface ShellListener {
    
    /**
     * Implementação do processamento de excessões
     * @param ex 
     */
    public void onCommandException(ErroExecucaoCommando ex);
    /**
     * Implementação do processamento de sucesso.
     * @param retorno 
     */
    public void onCommandSucess(String retorno);
    
}
