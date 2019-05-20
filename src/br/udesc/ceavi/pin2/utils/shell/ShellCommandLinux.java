package br.udesc.ceavi.pin2.utils.shell;

/**
 * Fábrica para criação de comandos para execução no linux.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
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
