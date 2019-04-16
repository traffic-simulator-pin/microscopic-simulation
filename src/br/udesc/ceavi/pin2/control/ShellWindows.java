package br.udesc.ceavi.pin2.control;

/**
 *
 * @author João Pedro Schmitz
 */
public class ShellWindows extends Shell {

    public ShellWindows(String commands) {
        super(commands);
    }

    @Override
    public String runShell() {
        return "Windows";
    }
    
}
