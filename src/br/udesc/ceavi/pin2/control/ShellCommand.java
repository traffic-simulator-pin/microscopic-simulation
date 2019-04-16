package br.udesc.ceavi.pin2.control;

/**
 * Fábrica para criação de comandos para execução.
 * @author João Pedro Schmitz
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
