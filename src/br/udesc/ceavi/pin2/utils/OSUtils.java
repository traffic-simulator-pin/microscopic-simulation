package br.udesc.ceavi.pin2.utils;

/**
 *
 * @author Módulo 4
 */
public class OSUtils {

    private static String OS = null;

    public OSUtils() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
    }

    public boolean isWindows() {
        return OS.startsWith("Windows");
    }

    public boolean isUnix() {
        return OS.startsWith("Linux");
    }
    
}
