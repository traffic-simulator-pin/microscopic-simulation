package br.udesc.ceavi.pin2.control;

/**
 * Fábrica para criação de comandos para execução no linux.
 * @author jpedroschmitz
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
