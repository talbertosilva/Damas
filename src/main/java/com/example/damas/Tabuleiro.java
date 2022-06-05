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

    void moverPeça(MovimentoPeça mover)
    {
        moverPeça(mover.deLinha, mover.deColuna, mover.paraLinha, mover.paraColuna);
    }

    private void moverPeça(int deLinha, int deColuna, int paraLinha, int paraColuna)
    {
        // Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
        // assumed that this move is legal.  If the move is a jump, the
        // jumped piece is removed from the board.  If a piece moves
        // the last row on the opponent's side of the board, the
        // piece becomes a king.
        tabuleiro[deLinha][deColuna] = tabuleiro[paraLinha][paraColuna];
        tabuleiro[deLinha][deColuna] = VAZIO;
        if (deLinha - paraLinha == 2 || deLinha - paraLinha == -2)
        {
            // The move is a jump.  Remove the jumped piece from the board.
            int moverLinha = (deLinha + paraLinha) / 2;  // Row of the jumped piece.
            int moverColuna = (deColuna + paraColuna) / 2;  // Column of the jumped piece.
            tabuleiro[moverLinha][moverColuna] = VAZIO;
        }
        if (paraLinha == 0 && tabuleiro[paraLinha][paraColuna] == BRANCA)
            tabuleiro[paraLinha][paraLinha] = BRANCA_RAINHA;
        if (paraLinha == 7 && tabuleiro[paraLinha][paraColuna] == PRETA)
            tabuleiro[paraLinha][paraColuna] = PRETA_RAINHA;
    }
}
