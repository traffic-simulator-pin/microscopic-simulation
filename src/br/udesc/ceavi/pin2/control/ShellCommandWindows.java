package br.udesc.ceavi.pin2.control;

/**
 * Fábrica para criação de comandos para execução no windows.
 * @author jpedroschmitz
 */
public class ShellCommandWindows extends ShellCommand{

    @Override
    /**
     * {@inheritdoc}
     */
    public Thread getNewShell(ShellListener listener, String... comando) {
        return new Thread(new ShellWindows(comando, listener));
    }
    
}
