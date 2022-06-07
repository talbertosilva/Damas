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

    public Jogo(Button btn_desistir, Button btn_novoJogo, Label mensagem)
    {
        this.btn_desistir = btn_desistir;
        this.btn_novoJogo = btn_novoJogo;
        this.mensagem = mensagem;
        this.btn_desistir.setOnMouseClicked(this);
        this.btn_novoJogo.setOnMouseClicked(this);
        tabuleiro = new Tabuleiro();

    }

    @Override
    public void handle(MouseEvent mouseEvent)
    {

    }
}
