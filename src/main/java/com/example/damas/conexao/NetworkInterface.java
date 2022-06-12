package com.example.damas.conexao;

public interface NetworkInterface {
    void comecarNovoJogo();

    void desistirJogo();
    void onClickQuadrado(int linha, int coluna);
}