package br.udesc.ceavi.pin2.utils.shell;

/**
 * Fábrica para criação de comandos para execução.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public abstract class ShellCommand {
    
    /**
     * Cria um novo shell para executar o comando e notificar o listener.
     * @param comando
     * @param listener
     * @return 
     */
    public abstract Thread getNewShell(ShellListener listener, String... comando);
    
}
