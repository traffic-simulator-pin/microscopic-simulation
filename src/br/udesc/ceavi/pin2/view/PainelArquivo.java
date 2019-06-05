package br.udesc.ceavi.pin2.view;

import br.udesc.ceavi.pin2.SimulacaoMicroscopica;
import br.udesc.ceavi.pin2.exceptions.ExtensaoArquivoInvalida;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import br.udesc.ceavi.pin2.control.IControleInicial;
import br.udesc.ceavi.pin2.control.ObservadorInicial;
import br.udesc.ceavi.pin2.exceptions.LogException;
import java.awt.Font;

/**
 * Painel para seleção do arquivo de configurações.
 * @author Bruno Galeazzi Rech, Gustavo Jung, Igor Martins, Jeferson Penz, João Pedro Schmitz
 */
public class PainelArquivo extends JPanel implements ObservadorInicial{
    
    private JFileChooser     arquivo;
    private JLabel           arquivoAtual;
    private JButton          botaoBuscar;
    private IControleInicial controller;
    
    /**
     * Cria um novo painel para seleção do arquivo de configurações.
     * @param controller
     */
    public PainelArquivo(IControleInicial controller){
        super();
        this.controller = controller;
        this.iniciaPropriedades();
        this.iniciaComponentes();
    }
    
    /**
     * Realiza a inicialização das propriedades do painel.
     */
    private void iniciaPropriedades(){
        this.setBackground(SimulacaoMicroscopica.COR_FUNDO);
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Realiza a inicialização dos componentes da tela.
     */
    private void iniciaComponentes() {
        this.controller.anexaObservador(this);
        
        this.arquivo = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de Simulação", SimulacaoMicroscopica.EXTENSAO_ARQUIVO);
        this.arquivo.setFileFilter(filter);
        
        this.arquivoAtual = new JLabel("Clique no botão para selecionar o arquivo de simulação.");
        this.arquivoAtual.setFont(new Font(this.arquivoAtual.getFont().getName(), Font.BOLD, 10));
        this.botaoBuscar  = new JButton("...");
        this.botaoBuscar.addActionListener((ActionEvent e) -> {
            iniciaBuscaNovoArquivo();
        });
        this.add(this.arquivoAtual, BorderLayout.WEST);
        this.add(this.botaoBuscar,  BorderLayout.EAST);
    }
    
    /**
     * Inicia a janela de busca para um novo arquivo de configurações.
     */
    private void iniciaBuscaNovoArquivo() {
        int retorno = this.arquivo.showOpenDialog(this);
        if(retorno == JFileChooser.APPROVE_OPTION) {
            try {
                this.controller.iniciaCarregamentoNovoArquivo(this.arquivo.getSelectedFile());
            }
            catch (ExtensaoArquivoInvalida ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void arquivoCarregado(File arquivo) {
        this.arquivoAtual.setText(arquivo.getName());
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void inicioGeracaoRede() {
        this.arquivoAtual.setText("Criando Rede.");
        this.botaoBuscar.setEnabled(false);
    }

    @Override
    /**
     * {@inheritdoc}
     */
    public void sucessoGeracaoRede() {}

    @Override
    /**
     * {@inheritdoc}
     */
    public void erroGeracaoRede(LogException ex) {
        this.arquivoAtual.setText("Houve um erro na criação da rede. Consulte a documentação.");
        this.botaoBuscar.setEnabled(true);
    }

}
