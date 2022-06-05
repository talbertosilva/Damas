package com.example.damas.conexao;

import com.example.damas.Interface;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements NetworkInterface {

    Socket clientSocket;
    private ServerSocket serverSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    int porta;

    public Server(int porta) {
        this.porta = porta;
    }

    public boolean conectar()
    {
        try
        {
            serverSocket = new ServerSocket(porta);
            clientSocket = serverSocket.accept();
            streams();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    void streams()
    {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarData(Object message) {
        if (clientSocket != null) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                cancelarConexao();
            }
        }
    }

    public void cancelarConexao()
    {
        try {
            serverSocket.close();
            clientSocket.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
