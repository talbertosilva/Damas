package com.example.damas.conexao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Server {

    private String ip;

    public Client(String ip, int porta){
        super(porta);
        this.ip = ip;
    }

    @Override
    public boolean conectar(){
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(ip, porta), 5000);
            streams();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
