package com.example.damas;

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

public class Interface implements Initializable {

    // Layout
    @FXML
    Text txt_ip, txt_porta;
    @FXML
    Label estadoConexao;
    @FXML
    TextField input_ip, input_porta;

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
        try {
            ServerSocket ss = new ServerSocket(porta);
            Socket s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String str = (String)dis.readUTF();
            estadoConexao.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void entrarJogo(MouseEvent mouseEvent) {
        String ip = this.input_ip.getText();
        String porta = this.input_porta.getText();

        try {
            Socket s = new Socket(ip, Integer.parseInt(porta));
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.writeUTF("Conectado com sucesso!");
            estadoConexao.setText("Conectado com sucesso!");
            dout.flush();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
