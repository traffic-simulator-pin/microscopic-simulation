package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;

/**
 * Classe para execução de tarefas com múltiplas etapas.
 * @author Jeferson Penz
 */
public abstract class ExecucaoMultiEtapas implements Runnable, ShellListener{
    
    private int etapaAtual;
    private String retorno;
    private LogException erro;

    /**
     * {@inheritdoc}
     */
    @Override
    public void run() {
        this.etapaAtual = 1;
        this.executaEtapa(this.etapaAtual);
    }
    
    /**
     * Realiza a execução da etapa especificada.
     * @param etapa
     */
    protected abstract void executaEtapa(int etapa);

    /**
     * {@inheritdoc}
     */
    @Override
    public synchronized void onCommandException(ErroExecucaoCommando ex) {
        this.erro = ex;
        this.etapaAtual = -1;
        this.executaEtapa(this.etapaAtual);
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public synchronized void onCommandSucess(String retorno) {
        this.retorno = retorno;
        this.etapaAtual++;
        this.executaEtapa(this.etapaAtual);
    }
    
    /**
     * Retorna se a execução foi finalizada.
     * @return 
     */
    public synchronized boolean getExecucaoFinalizada(){
        return this.etapaAtual < 1;
    }

    /**
     * Retorna o último retorno da aplicação.
     * @return 
     */
    public String getRetorno() {
        return retorno;
    }

    /**
     * Retorna o último erro caso existente.
     * @return 
     */
    public LogException getErro() {
        return erro;
    }
    
    /**
     * Retorna se ocorreu um erro na execução.
     * @return 
     */
    public synchronized boolean getExecucaoErro(){
        return this.etapaAtual == -1;
    }
    
}
