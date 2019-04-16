package br.udesc.ceavi.pin2.control;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell para execução de comandos no Windows.
 * @author João Pedro Schmitz
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            reader.lines().forEach((String line) -> {
                output.append(line).append("\n");
            });

            int exitVal = process.waitFor();
            if (exitVal != 0) {
                throw new ErroExecucaoCommando(comando, new IllegalStateException("O retorno da aplicação(" + exitVal + ") é inválido:\n" + output.toString()));
            }
            return output.toString();
        } catch (IOException | InterruptedException ex) {
            throw new ErroExecucaoCommando(comando, ex);
        }
    }
    
}
