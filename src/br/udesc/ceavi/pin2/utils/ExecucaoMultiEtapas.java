package br.udesc.ceavi.pin2.utils;

import br.udesc.ceavi.pin2.exceptions.ErroExecucaoCommando;
import br.udesc.ceavi.pin2.exceptions.LogException;
import br.udesc.ceavi.pin2.utils.shell.ShellListener;

/**
 * Classe para execução de tarefas com múltiplas etapas.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schimitz
 */
public abstract class ExecucaoMultiEtapas implements Runnable, ShellListener{
    
    private int etapaAtual;
    private String retorno;
    private LogException erro;

    public ExecucaoMultiEtapas() {
        this.etapaAtual = 1;
        this.retorno    = "";
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public void run() {
        this.etapaAtual = 1;
        this.retorno    = "";
        this.erro       = null;
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
        this.notificaErroExecucaoComando(ex);
    }
    
    /**
     * Notifica de que houve um erro na execução do comando.
     * @param excessao 
     */
    protected synchronized void notificaErroExecucaoComando(LogException excessao){
        this.erro = excessao;
        this.etapaAtual = -1;
        this.executaEtapa(this.etapaAtual);
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public synchronized void onCommandSucess(String retorno) {
        this.notificaSucessoExecucaoComando(retorno);
    }
    
    /**
     * Notifica de que um comando foi executado com sucesso.
     * @param retorno 
     */
    protected synchronized void notificaSucessoExecucaoComando(String retorno){

        this.retorno += retorno;
        if(this.etapaAtual < this.getTotalEtapas()){
            this.etapaAtual++;
            this.executaEtapa(this.etapaAtual);
        }
        else {
            this.etapaAtual = 0;
        }
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
    public synchronized String getRetorno() {
        return retorno;
    }

    /**
     * Retorna o último erro caso existente.
     * @return 
     */
    public synchronized LogException getErro() {
        return erro;
    }
    
    /**
     * Retorna se ocorreu um erro na execução.
     * @return 
     */
    public synchronized boolean getExecucaoErro(){
        return this.etapaAtual == -1;
    }
    
    /**
     * Retorna o total de etapas para execução
     * @return 
     */
    protected abstract int getTotalEtapas();
    
}
