package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shell para execução dos comandos.
 * @author João Pedro Schmitz
 */
public abstract class Shell implements Runnable {

    protected final String[] comandos;
    protected ShellListener listener;
    
    /**
     * Cria um novo shell para execução dos comandos.
     * @param comandos 
     * @param listener 
     */
    public Shell(String[] comandos, ShellListener listener){
        this.comandos = comandos;
        this.listener = listener;
    }
    
    /**
     * Executa os comandos no shell.
     * @return
     * @throws ErroExecucaoCommando 
     */
    private String runShell() throws ErroExecucaoCommando{
        StringBuilder resultado = new StringBuilder();
        for (String comando : this.comandos) {
            resultado.append("> ").append(comando).append("\n").append(this.runCommand(comando));
        }
        return resultado.toString();
    }
    
    /**
     * Executa o comando no shell.
     * @param comando
     * @return
     * @throws ErroExecucaoCommando 
     */
    protected abstract String runCommand(String comando) throws ErroExecucaoCommando;

    @Override
    /**
     * {@inheritdoc}
     */
    public void run() {
        try {
            String retorno = this.runShell();
            if(this.listener != null){
                this.listener.onCommandSucess(retorno);
            }
        } catch (ErroExecucaoCommando ex) {
            if(this.listener != null){
                this.listener.onCommandException(ex);
            }
        }
    }
    
}
