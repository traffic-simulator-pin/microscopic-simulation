package br.udesc.ceavi.pin2.exceptions;

import java.io.IOException;
import javax.swing.JComponent;

/**
 * Excessão para campos de configuração desconhecidos
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz 
 */
public class CampoDesconhecidoException extends Exception{
    
    private JComponent campo;

    /**
     * Cria uma nova excessão para campos de configuração desconhecidos.
     * @param campo
     */
    public CampoDesconhecidoException(JComponent campo) {
        super();
        this.campo = campo;
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public String getMessage() {
        return "Campo do tipo " + campo.getClass().getCanonicalName() + " é desconhecido pelo sistema.";
    }
    
}
