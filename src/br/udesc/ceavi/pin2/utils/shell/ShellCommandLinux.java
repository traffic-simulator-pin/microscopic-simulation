package br.udesc.ceavi.pin2.utils.shell;

/**
 * Fábrica para criação de comandos para execução no linux.
 * @author João Pedro Schmitz
 */
public class ShellCommandLinux extends ShellCommand{

    @Override
    /**
     * {@inheritdoc}
     */
    public Thread getNewShell(ShellListener listener, String... comando) {
        return new Thread(new ShellLinux(comando, listener));
    }
    
}
