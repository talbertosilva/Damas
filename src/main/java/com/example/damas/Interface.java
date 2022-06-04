package com.example.damas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Interface implements Initializable {

    @FXML
    Text txt_ip, txt_porta;

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
}
