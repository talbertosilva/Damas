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
    Text txt_ip, txt_porta;
    @FXML
    Label estadoConexao;
    @FXML
    TextField input_ip, input_porta;
    @FXML
    Button btn_resign, btn_newGame;

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
        GridPane grid = ((GridPane) input_ip.getParent().getParent().getParent());
        grid.add(DadosTabuleiro.OPEN ? tabuleiro = new Jogo(btn_resign, btn_newGame, estadoConexao) : null, 0, 1);
        txt_porta.setVisible(false);
    }

    public void criarJogo(MouseEvent mouseEvent) {
        int porta = 6666;
        servidor = new Server(tabuleiro, porta);
        new Thread(() -> {
            boolean state = servidor.conectar();
            if(state){
                Platform.runLater(() -> {
                    estadoConexao.setText("Adversário conectado");
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
            }
        }).start();
    }
}
