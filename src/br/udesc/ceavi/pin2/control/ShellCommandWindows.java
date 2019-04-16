/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.pin2.control;

/**
 *
 * @author jpedroschmitz
 */
public class ShellCommandWindows extends ShellCommand{

    @Override
    public Shell getNewShell(String commands) {
        return new ShellWindows(commands);
    }
    
}
