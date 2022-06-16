package com.example.damas;

import com.example.damas.conexao.Client;
import com.example.damas.conexao.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

/**
 *<p>Interface - Funcionalidades do ecrã de jogo</p>
 * A classe Interface contém as funcionalidades da tela de interface,
 * respetivamente a secção de criar um jogo (abrir um socket do servidor),
 * e a secção de juntar a um jogo (abrir um socket de cliente e fazer a
 * conexão com um socket de servidor).
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */

public class Interface implements Initializable {

    // Layout
    @FXML
    Text txt_ip;
    @FXML
    Label estadoConexao;
    @FXML
    TextField input_ip, input_porta, input_portaHost;
    @FXML
    Button btn_desistir, btn_novoJogo, btn_criarJogo, btn_entrarJogo;

    private Server servidor;
    private Client cliente;
    private Jogo tabuleiro;
    private Rotate rotation;
    private Translate translation;

    /**
     * O metodo initialize começa por receber o endereço de ip do jogador
     * para caso ele queira inicializar um jogo, apenas ter que escolher
     * uma porta de acesso.
     * Também é feita a atribuição do tabuleiro à devida posição na
     * interface gráfica.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            txt_ip.setText(ip.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        btn_entrarJogo.setDisable(false);
        btn_criarJogo.setDisable(false);
        input_portaHost.setDisable(false);
        GridPane grid = ((GridPane) input_ip.getParent().getParent().getParent());
        grid.add(DadosTabuleiro.OPEN ? tabuleiro = new Jogo(btn_desistir, btn_novoJogo, estadoConexao) : null, 0, 1);
    }

    /**
     * O metodo criarJogo estabelece a inicialização do socket do
     * servidor, na qual ficará a aguardar o segundo utilizador se conectar
     * e posteriormente iniciar o jogo com a devida atribuição do número de jogador.
     */
    public void criarJogo(MouseEvent mouseEvent) throws UnknownHostException {
        int porta = Integer.parseInt(this.input_portaHost.getText());
        InetAddress ip = InetAddress.getLocalHost();

        servidor = new Server(tabuleiro, porta);
        estadoConexao.setText("A hospedar no ip " + ip.getHostAddress() + " na porta " + porta);

        btn_criarJogo.setDisable(true);
        btn_entrarJogo.setDisable(true);
        input_portaHost.setDisable(true);

        new Thread(() -> {
            boolean ligado = servidor.conectar();
            if(ligado){
                Platform.runLater(() -> {
                    estadoConexao.setText("Adversario conectado");
                    tabuleiro.setJogoDecorrer(false);
                    tabuleiro.setJogador(1);
                    tabuleiro.comecarNovoJogo();
                });
                tabuleiro.setNetworkInterface(this.servidor);
                servidor.lerData();
            }
        }).start();
    }

    /**
     * O metodo entrarJogo estabelece a conexão do socket do cliente ao
     * socket do servidor. Assim que estiver conectado, é atribuido o
     * tabuleiro a ambos os jogadores e um dos jogadores é definido
     * com um tabuleiro invertido, para fazer a lógica das damas
     * (um jogador de cada lado).
     */
    public void entrarJogo(MouseEvent mouseEvent) {
        String ip = this.input_ip.getText();
        String porta = this.input_porta.getText();

        cliente = new Client(ip, parseInt(porta), tabuleiro);

        rotation = new Rotate(180, 0, 0);
        translation = new Translate(DadosTabuleiro.TAMANHO * -8, DadosTabuleiro.TAMANHO * -8);

        estadoConexao.setText("A conectar...");
        btn_criarJogo.setDisable(true);
        btn_entrarJogo.setDisable(true);
        input_portaHost.setDisable(true);
        new Thread(() -> {
            boolean ligado = cliente.conectar();
            if(ligado){
                Platform.runLater(() -> {
                    estadoConexao.setText("Conectado a " + ip + ":" + porta);
                    tabuleiro.setJogoDecorrer(false);
                    tabuleiro.setJogador(3);
                    tabuleiro.comecarNovoJogo();
                    tabuleiro.getTransforms().addAll(rotation, translation);
                });
                tabuleiro.setNetworkInterface(this.cliente);
                cliente.lerData();
            }
        }).start();
    }
}