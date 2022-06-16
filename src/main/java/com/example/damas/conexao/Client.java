package com.example.damas.conexao;

import com.example.damas.Jogo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *<p>Cliente - Criação do cliente</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */
public class Client extends Server {

    private String ip;

    public Client(String ip, int porta, Jogo tabuleiro){
        super(tabuleiro, porta);
        this.ip = ip;
    }

    /**
     * O metodo conectar inicializa o socket do cliente e conecta-se a um servidor.
     */
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
