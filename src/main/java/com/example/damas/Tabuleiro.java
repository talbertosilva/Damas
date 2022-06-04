package com.example.damas;

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
}
