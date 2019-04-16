package br.udesc.ceavi.pin2.control;

/**
 *
 * @author João Pedro Schmitz
 */
public abstract class Shell {

    protected final String commands;
    
    public Shell(String commands){
        this.commands = commands;
    }
    
    public abstract String runShell();
    
}
