package br.udesc.ceavi.pin2.control;

/**
 *
 * @author jpedroschmitz
 */
public class ShellCommandLinux extends ShellCommand{

    @Override
    public Shell getNewShell(String commands) {
        return new ShellLinux(commands);
    }
    
}
