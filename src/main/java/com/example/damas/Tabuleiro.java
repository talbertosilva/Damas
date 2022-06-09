package com.example.damas;

import java.util.Vector;

public class Tabuleiro
{
    static final int
        VAZIO = 0,
        BRANCA = 1,
        BRANCA_RAINHA = 2,
        PRETA = 3,
        PRETA_RAINHA = 4;

    private int[][] tabuleiro;

    Tabuleiro()
    {
        tabuleiro = new int[DadosTabuleiro.TAMANHO_TABULEIRO][DadosTabuleiro.TAMANHO_TABULEIRO];
        prepararJogo();
    }

    void prepararJogo()
    {
        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
        {
            for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
            {
                if (linha % 2 == coluna % 2)
                {
                    if (linha < (DadosTabuleiro.TAMANHO_TABULEIRO / 2) - 1)
                        tabuleiro[linha][coluna] = PRETA;
                    else if (linha > DadosTabuleiro.TAMANHO_TABULEIRO / 2)
                        tabuleiro[linha][coluna] = BRANCA;
                    else
                        tabuleiro[linha][coluna] = VAZIO;
                }
                else
                {
                    tabuleiro[linha][coluna] = VAZIO;
                }
            }
        }
    }

    int peçaEm(int linha, int coluna)
    {
        return tabuleiro[linha][coluna];
    }

    public void colucarPeçaEm(int linha, int coluna, int peça)
    {
        tabuleiro[linha][coluna] = peça;
    }

    void moverPeça(MovimentoPeça mover)
    {
        moverPeça(mover.deLinha, mover.deColuna, mover.paraLinha, mover.paraColuna);
    }

    private void moverPeça(int deLinha, int deColuna, int paraLinha, int paraColuna)
    {
        tabuleiro[deLinha][deColuna] = tabuleiro[paraLinha][paraColuna];
        tabuleiro[deLinha][deColuna] = VAZIO;
        if (deLinha - paraLinha == 2 || deLinha - paraLinha == -2)
        {

            int moverLinha = (deLinha + paraLinha) / 2;
            int moverColuna = (deColuna + paraColuna) / 2;
            tabuleiro[moverLinha][moverColuna] = VAZIO;
        }
        if (paraLinha == 0 && tabuleiro[paraLinha][paraColuna] == BRANCA)
            tabuleiro[paraLinha][paraLinha] = BRANCA_RAINHA;
        if (paraLinha == 7 && tabuleiro[paraLinha][paraColuna] == PRETA)
            tabuleiro[paraLinha][paraColuna] = PRETA_RAINHA;
    }

    MovimentoPeça[] verificaMovimento(int jogador)
    {
        if (jogador != BRANCA && jogador != PRETA) return null;

        int jogadorRainha;
        if (jogador == BRANCA) jogadorRainha = BRANCA_RAINHA;
        else jogadorRainha = PRETA_RAINHA;

        Vector<MovimentoPeça> movimentos = new Vector<>();

        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
            for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
                guardaMovimentosCapturas(movimentos, jogador, jogadorRainha, linha, coluna);

        if (movimentos.size() == 0)
            for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
                for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
                    if (tabuleiro[linha][coluna] == jogador || tabuleiro[linha][coluna] == jogadorRainha)
                    {
                        if (podeMover(jogador, linha, coluna, linha + 1, coluna + 1))
                            movimentos.addElement(new MovimentoPeça(linha, coluna, linha + 1, coluna + 1));
                        if (podeMover(jogador, linha, coluna, linha - 1, coluna + 1))
                            movimentos.addElement(new MovimentoPeça(linha, coluna, linha - 1, coluna + 1));
                        if (podeMover(jogador, linha, coluna, linha + 1, coluna - 1))
                            movimentos.addElement(new MovimentoPeça(linha, coluna, linha + 1, coluna - 1));
                        if (podeMover(jogador, linha, coluna, linha - 1, coluna - 1))
                            movimentos.addElement(new MovimentoPeça(linha, coluna, linha - 1, coluna - 1));
                    }

        return movimentosArray(movimentos);
    }

    MovimentoPeça[] verificaCaptura(int jogador, int linha, int coluna)
    {
        if (jogador != BRANCA && jogador != PRETA)
            return null;
        int jogadorRainha;
        if (jogador == BRANCA)
            jogadorRainha = BRANCA_RAINHA;
        else
            jogadorRainha = PRETA_RAINHA;
        Vector<MovimentoPeça> movimentos = new Vector<>();
        guardaMovimentosCapturas(movimentos, jogador, jogadorRainha, linha, coluna);
        return movimentosArray(movimentos);
    }

    private MovimentoPeça[] movimentosArray(Vector<MovimentoPeça> movimentos)
    {
        if (movimentos.size() == 0)
            return null;
        else
        {
            MovimentoPeça[] movimentoArray = new MovimentoPeça[movimentos.size()];
            for (int i = 0; i < movimentos.size(); i++)
                movimentoArray[i] = movimentos.elementAt(i);
            return movimentoArray;
        }
    }

    private boolean podeCapturar(int jogador, int l1, int c1, int l2, int c2, int l3, int c3)
    {
        if (l3 < 0 || l3 >= DadosTabuleiro.TAMANHO_TABULEIRO || c3 < 0 || c3 >= DadosTabuleiro.TAMANHO_TABULEIRO)
            return false;

        if (tabuleiro[l3][c3] != VAZIO)
            return false;

        if (jogador == BRANCA)
        {
            return !(tabuleiro[l1][c1] == BRANCA && l3 > l1) && !(tabuleiro[l2][c2] != PRETA && tabuleiro[l2][c2] != PRETA_RAINHA);
        }
        else
        {
            return !(tabuleiro[l1][c1] == PRETA && l3 < l1) && !(tabuleiro[l2][c2] != BRANCA && tabuleiro[l2][c2] != BRANCA_RAINHA);
        }
    }

    private boolean podeMover(int jogador, int l1, int c1, int l2, int c2)
    {
        if (l2 < 0 || l2 >= DadosTabuleiro.TAMANHO_TABULEIRO || c2 < 0 || c2 >= DadosTabuleiro.TAMANHO_TABULEIRO)
            return false;  // (r2,c2) is off the board.

        if (tabuleiro[l2][c2] != VAZIO)
            return false;  // (r2,c2) already contains a piece.

        if (jogador == BRANCA)
        {
            return !(tabuleiro[l1][c1] == BRANCA && l2 > l1);
        }
        else
        {
            return !(tabuleiro[l1][c1] == PRETA && l2 < l1);
        }
    }

    private void guardaMovimentosCapturas(Vector<MovimentoPeça> movimementos, int jogador, int jogadorRainha, int linha, int coluna)
    {
        if (tabuleiro[linha][coluna] == jogador || tabuleiro[linha][coluna] == jogadorRainha)
        {
            if (podeCapturar(jogador, linha, coluna, linha + 1, coluna + 1, linha + 2, coluna + 2))
                movimementos.addElement(new MovimentoPeça(linha, coluna, linha + 2, coluna + 2));
            if (podeCapturar(jogador, linha, coluna, linha - 1, coluna + 1, linha - 2, coluna + 2))
                movimementos.addElement(new MovimentoPeça(linha, coluna, linha - 2, coluna + 2));
            if (podeCapturar(jogador, linha, coluna, linha + 1, coluna - 1, linha + 2, coluna - 2))
                movimementos.addElement(new MovimentoPeça(linha, coluna, linha + 2, coluna - 2));
            if (podeCapturar(jogador, linha, coluna, linha - 1, coluna - 1, linha - 2, coluna - 2))
                movimementos.addElement(new MovimentoPeça(linha, coluna, linha - 2, coluna - 2));
        }
    }

}
