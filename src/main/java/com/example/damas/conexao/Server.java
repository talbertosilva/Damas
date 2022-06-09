package com.example.damas.conexao;

import com.example.damas.Interface;
import com.example.damas.Jogo;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements NetworkInterface {

    private Jogo tabuleiro;
    Socket clientSocket;
    private ServerSocket serverSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    int porta;

    public Server(Jogo tabuleiro, int porta) {
        this.tabuleiro = tabuleiro;
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

    public void lerData()
    {
        if (clientSocket != null) {
            while (true) {
                try {
                    verificarData((String) in.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    break;
                }
            }
        }
    }

    private void verificarData(String message) throws IOException, ClassNotFoundException
    {
        switch (message) {
            case "CLICK_SQUARE":
                int row = (int) in.readObject();
                int col = (int) in.readObject();
                Platform.runLater(() -> tabuleiro.clickQuadrado(row, col));
                break;
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

    @Override
    public void clickQuadrado(int row, int col) {
        System.out.println("Linha: " + row + " Coluna: " + col);
        enviarData("CLICK_SQUARE");
        enviarData(row);
        enviarData(col);
    }
}
