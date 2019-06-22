package br.udesc.ceavi.pin2.utils.shell;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shell para execução de comandos no Windows.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class ShellWindows extends Shell {

/**
     * Cria um novo shell para execução de comandos no Windows.
     * @param comando
     * @param listener
     */
    public ShellWindows(String[] comando, ShellListener listener) {
        super(comando, listener);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String runCommand(String comando) throws ErroExecucaoCommando{
        try {
            Process process = Runtime.getRuntime().exec(String.format("cmd.exe /c %s", comando));
            
            StringBuilder output = new StringBuilder();
            StringBuilder errors = new StringBuilder();
            
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            try {
                while(process.isAlive() || process.getInputStream().available() > 0 || process.getErrorStream().available() > 0){
                    if(process.getInputStream().available() > 0){
                        outputReader.lines().forEach((String line) -> {
                            output.append(line).append("\n");
                        });
                    }
                    if(process.getErrorStream().available() > 0){
                        errorReader.lines().forEach((String line) -> {
                            errors.append(line).append("\n");
                        });
                        process.destroy();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {}
                }
            } catch(IOException exception){}
            
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                throw new ErroExecucaoCommando(comando, new IllegalStateException("O retorno da aplicação(" + exitVal + ") é inválido:\n" + output.toString()));
            }
            if(errors.length() > 0){
                errors.append("\n").append(output.toString());
                return errors.toString();
            }
            return output.toString();
        } catch (IOException | InterruptedException ex) {
            throw new ErroExecucaoCommando(comando, ex);
        }
    }
    
}
