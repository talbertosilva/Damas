package com.example.damas.conexao;

import com.example.damas.Jogo;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *<p>Server - Criação do servidor</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */

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

    /**
     * O metodo conectar inicializa o socket do servidor e com o método accept
     * fica a aguardar uma conexão.
     */
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

    /**
     * O metodo streams cria os respectivos streams de Input e Output
     */
    void streams()
    {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * O metodo enviarData, é responsável por enviar dados na devida
     * conexão.
     */
    private void enviarData(Object message) {
        if (clientSocket != null) {
            try {
                out.writeObject(message);
                System.out.println(message);
                out.flush();
            } catch (IOException e) {
                cancelarConexao();
            }
        }
    }

    /**
     * O metodo lerData, ao contrário da enviarData, é responsável por
     * receber esses dados e lê-los.
     */
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

    /**
     * O metodo verificarData, recebe uma String com a devida ação e caso
     * a ação seja CLICAR_QUADRADO, é verificado apenas duas variáveis, que
     * acabam por ser a posição onde o jogador clicou.
     */
    private void verificarData(String message) throws IOException, ClassNotFoundException
    {
        switch (message) {
            case "NOVO_JOGO":
                Platform.runLater(() -> tabuleiro.comecarNovoJogo());
                break;
            case "DESISTIR":
                Platform.runLater(() -> tabuleiro.desistirJogo());
                break;
            case "CLICAR_QUADRADO":
                int linha = (int) in.readObject();
                int coluna = (int) in.readObject();
                Platform.runLater(() -> tabuleiro.clickQuadrado(linha, coluna));
                break;
        }
    }

    /**
     * O metodo cancelarConexão, encerra os sockets do servidor e do cliente.
     */
    public void cancelarConexao()
    {
        try {
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void comecarNovoJogo()
    {
        enviarData("NOVO_JOGO");
    }

    @Override
    public void desistirJogo()
    {
        enviarData("DESISTIR");
    }

    @Override
    public void onClickQuadrado(int linha, int coluna) {
        enviarData("CLICAR_QUADRADO");
        enviarData(linha);
        enviarData(coluna);
    }
}