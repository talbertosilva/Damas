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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

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

    public void criarJogo(MouseEvent mouseEvent) throws UnknownHostException {
        int porta = Integer.parseInt(this.input_portaHost.getText());
        InetAddress ip = InetAddress.getLocalHost();

        servidor = new Server(tabuleiro, porta);
        estadoConexao.setText("A hospedar no ip " + ip.getHostAddress() + " na porta " + porta);
        btn_criarJogo.setDisable(true);
        btn_entrarJogo.setDisable(true);
        input_portaHost.setDisable(true);
        new Thread(() -> {
            boolean state = servidor.conectar();
            if(state){
                Platform.runLater(() -> {
                    estadoConexao.setText("AdversÃ¡rio conectado");
                    tabuleiro.setJogoDecorrer(false);
                    tabuleiro.setJogador(1);
                    tabuleiro.comecarNovoJogo();
                });
                tabuleiro.setNetworkInterface(this.servidor);
                servidor.lerData();
            }
        }).start();
    }

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
            boolean state = cliente.conectar();
            if(state){
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