package br.udesc.ceavi.pin2.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Pedro Schmitz
 */
public class ShellLinux extends Shell {

    public ShellLinux(String commands) {
        super(commands);
    }

    @Override
    public String runShell() {
        try {
            Process process = Runtime.getRuntime().exec(commands);
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                return output.toString();
            } else {

            }
        } catch (IOException ex) {
            Logger.getLogger(ShellLinux.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ShellLinux.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
