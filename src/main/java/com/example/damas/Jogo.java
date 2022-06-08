package com.example.damas;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class Jogo extends Group implements EventHandler<MouseEvent>
{
    private Button btn_desistir;
    private Button btn_novoJogo;
    private Label mensagem;

    private Tabuleiro tabuleiro;
    private MovimentoPeça[] movimentoPeças;

    private int linhaSelecionada, colunaSelecionada;

    private int jogador2; // Jogador Atual
    private int jogador1; // Meu Jogador

    private boolean jogoDecorrer;

    public Jogo(Button btn_desistir, Button btn_novoJogo, Label mensagem)
    {
        this.btn_desistir = btn_desistir;
        this.btn_novoJogo = btn_novoJogo;
        this.mensagem = mensagem;
        this.btn_desistir.setOnMouseClicked(this);
        this.btn_novoJogo.setOnMouseClicked(this);
        tabuleiro = new Tabuleiro();
        comecarNovoJogo();

    }

    public void setJogoDecorrer(boolean jogoDecorrer) {
        this.jogoDecorrer = jogoDecorrer;
    }

    public void setJogador1(int jogador1) {
        this.jogador1 = jogador1;
    }

    public void setJogador2(int jogador2) {
        this.jogador2 = jogador2;
    }

    private void gameOver(String str)
    {
        mensagem.setText(str);
        btn_novoJogo.setDisable(false);
        btn_desistir.setDisable(true);
        jogoDecorrer = false;
    }

    public void comecarNovoJogo()
    {
        if(jogoDecorrer)
        {
            mensagem.setText("Tens de acabar o jogo que está a decorrer!");
            return;
        }
        tabuleiro.prepararJogo();
        jogador2 = Tabuleiro.BRANCA;
        movimentoPeças = tabuleiro.verificaMovimento(Tabuleiro.BRANCA);
        linhaSelecionada = -1;
        mensagem.setText("Jogador 1: Joga!");
        jogoDecorrer = true;
        btn_novoJogo.setDisable(true);
        btn_desistir.setDisable(false);


    }

    public void desistirJogo()
    {
        if(!jogoDecorrer)
        {
            mensagem.setText("Não há nenhum jogo a decorrer!");
            return;
        }
        if(jogador2 == Tabuleiro.BRANCA)
            gameOver("Jogador 1 desistiu. Jogador 2 ganhou");
        else
            gameOver("Jogador 2 desistiu. Jogador 1 ganhou");
    }

    @Override
    public void handle(MouseEvent mouseEvent)
    {

    }
}
