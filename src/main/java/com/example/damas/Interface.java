package com.example.damas;

import com.example.damas.conexao.Client;
import com.example.damas.conexao.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

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

    private Server servidor;
    private Client cliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            txt_ip.setText(ip.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        txt_porta.setVisible(false);
    }

    public void criarJogo(MouseEvent mouseEvent) {
        int porta = 6666;
        servidor = new Server(porta);
        new Thread(() -> {
            boolean state = servidor.conectar();
            if(state){
                Platform.runLater(() -> {
                    estadoConexao.setText("Adversário conectado");
                });
            }
        }).start();
    }

    public void entrarJogo(MouseEvent mouseEvent) {
        String ip = this.input_ip.getText();
        String porta = this.input_porta.getText();

        cliente = new Client(ip, parseInt(porta));

        new Thread(() -> {
            boolean state = cliente.conectar();
            if(state){
                Platform.runLater(() -> {
                    estadoConexao.setText("Conectado a " + ip + ":" + porta);
                });
            }
        }).start();
    }
}
