package br.udesc.ceavi.pin2.utils.shell;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell para execução de comandos no linux.
 *
 * @author João Pedro Schmitz
 */
public class ShellLinux extends Shell {

    /**
     * Cria um novo shell para execução de comandos no linux.
     *
     * @param comando
     * @param listener
     */
    public ShellLinux(String[] comando, ShellListener listener) {
        super(comando, listener);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String runCommand(String comando) throws ErroExecucaoCommando {
        try {
            Process process = Runtime.getRuntime().exec(String.format("sh -c %s", comando));
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return output.toString();
            } else {
                throw new ErroExecucaoCommando(comando, new Exception(output.toString()));
            }
        } catch (IOException | InterruptedException ex) {
            throw new ErroExecucaoCommando(comando, ex);
        }
    }

}
