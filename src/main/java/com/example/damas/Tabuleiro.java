package com.example.damas;

import java.util.Vector;

/**
 *<p>Tabuleiro - Construção do tabuleiro puro (array, posições, etc)</p>
 * A classe tabuleiro é responsável pela construção do tabuleiro puro.
 * É criado o array com o devido tamanho (8 por 8) e preenchido todas
 * as posições com uma simples conta (valor % 2), que por sua vez, faz
 * uma atribução exatamente como um tabuleiro de damas.
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */

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

    /**
     * O metodo prepararJogo é responsável pela atribuição de cada posição às peças
     * dos jogadores. É dividido o tabuleiro em dois, e na parte de cima, posicionado as peças
     * pretas, e ao contrário, as peças brancas.
     */
    public void prepararJogo()
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

    void moverPeça(MovimentoPeca mover)
    {
        moverPeça(mover.deLinha, mover.deColuna, mover.paraLinha, mover.paraColuna);
    }

    /**
     * O metodo moverPeça é responsável por fazer o movimento de X posição para Y posição.
     * Caso o movimento seja uma captura, é feito um movimento para a diferença de 2 casas
     * e atribuido à peça intermediaria a posição de vazio.
     * Caso a peça se mova para a última linha do oponente, é atualizado o estatuto dessa peça,
     * de peça normal, para peça rainha.
     */
    public void moverPeça(int deLinha, int deColuna, int paraLinha, int paraColuna)
    {
        tabuleiro[paraLinha][paraColuna] = tabuleiro[deLinha][deColuna];
        tabuleiro[deLinha][deColuna] = VAZIO;
        if (deLinha - paraLinha == 2 || deLinha - paraLinha == -2){
            int moverLinha = (deLinha + paraLinha) / 2;
            int moverColuna = (deColuna + paraColuna) / 2;
            tabuleiro[moverLinha][moverColuna] = VAZIO;
        }
        if (paraLinha == 0 && tabuleiro[paraLinha][paraColuna] == BRANCA)
            tabuleiro[paraLinha][paraColuna] = BRANCA_RAINHA;
        if (paraLinha == 7 && tabuleiro[paraLinha][paraColuna] == PRETA)
            tabuleiro[paraLinha][paraColuna] = PRETA_RAINHA;
    }

    /**
     * O metodo verificaMovimento retorna uma array com todos os movimentos
     * possíveis para o jogador em especifico.
     * Caso o jogador não tenha movimentos possíveis, é retornado null.
     */
    public MovimentoPeca[] verificaMovimento(int jogador)
    {
        if (jogador != BRANCA && jogador != PRETA) return null;

        int jogadorRainha;
        if (jogador == BRANCA) jogadorRainha = BRANCA_RAINHA;
        else jogadorRainha = PRETA_RAINHA;

        Vector<MovimentoPeca> movimentos = new Vector<>();

        /**
         * É analisado o tabuleiro inteiro, caso exista uma peça do jogador atual,
         * é verificado se pode capturar alguma peça nas direções todas. Caso exista,
         * é colocado esse movimento no vector dos movimentos.
         */
        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
            for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++){
                guardaMovimentosCapturas(movimentos, jogador, jogadorRainha, linha, coluna);
            }

        /**
         * Caso o vector dos movimentos (capturas) esteja vazio, é verificado por movimentos
         * normais, caso existam movimentos normais, é guardado no vector de movimentos.
         */
        if (movimentos.size() == 0)
            for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
                for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
                    if (tabuleiro[linha][coluna] == jogador || tabuleiro[linha][coluna] == jogadorRainha)
                    {
                        if (podeMover(jogador, linha, coluna, linha + 1, coluna + 1))
                            movimentos.addElement(new MovimentoPeca(linha, coluna, linha + 1, coluna + 1));
                        if (podeMover(jogador, linha, coluna, linha - 1, coluna + 1))
                            movimentos.addElement(new MovimentoPeca(linha, coluna, linha - 1, coluna + 1));
                        if (podeMover(jogador, linha, coluna, linha + 1, coluna - 1))
                            movimentos.addElement(new MovimentoPeca(linha, coluna, linha + 1, coluna - 1));
                        if (podeMover(jogador, linha, coluna, linha - 1, coluna - 1))
                            movimentos.addElement(new MovimentoPeca(linha, coluna, linha - 1, coluna - 1));
                    }

        return movimentosArray(movimentos);
    }

    /**
     * O metodo verificaCaptura é responsável por verificar as capturas
     * possíveis da peça selecionada.
     */
    public MovimentoPeca[] verificaCaptura(int jogador, int linha, int coluna)
    {
        if (jogador != BRANCA && jogador != PRETA)
            return null;
        int jogadorRainha;
        if (jogador == BRANCA)
            jogadorRainha = BRANCA_RAINHA;
        else
            jogadorRainha = PRETA_RAINHA;
        Vector<MovimentoPeca> movimentos = new Vector<>();
        guardaMovimentosCapturas(movimentos, jogador, jogadorRainha, linha, coluna);
        return movimentosArray(movimentos);
    }

    private MovimentoPeca[] movimentosArray(Vector<MovimentoPeca> movimentos)
    {
        if (movimentos.size() == 0)
            return null;
        else
        {
            MovimentoPeca[] movimentoArray = new MovimentoPeca[movimentos.size()];
            for (int i = 0; i < movimentos.size(); i++) {
                movimentoArray[i] = movimentos.elementAt(i);
            }
            return movimentoArray;
        }
    }

    /**
     * O metodo podeCapturar é usado nos métodos anteriores para realizar a verificação
     * se uma peça pode capturar, tendo em conta as 3 posições: posição da peça selecionada (l1, c1),
     * posição da peça intermédia (l2, c2) e posição da peça para onde será realizado o movimento (l3, c3).
     */
    public boolean podeCapturar(int jogador, int l1, int c1, int l2, int c2, int l3, int c3)
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

    /**
     * O metodo podeMover verifica se a peça pode se mover da posição (l1, c1) para a posição (l2, c2),
     * tendo em conta se a posição (l2, c2) está fora do tabuleiro ou não e se a posição (l2, c2) é uma
     * posição próxima da posição inicial (l1, c1).
     */
    public boolean podeMover(int jogador, int l1, int c1, int l2, int c2)
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

    private void guardaMovimentosCapturas(Vector<MovimentoPeca> movimementos, int jogador, int jogadorRainha, int linha, int coluna)
    {
        if (tabuleiro[linha][coluna] == jogador || tabuleiro[linha][coluna] == jogadorRainha)
        {
            if (podeCapturar(jogador, linha, coluna, linha + 1, coluna + 1, linha + 2, coluna + 2))
                movimementos.addElement(new MovimentoPeca(linha, coluna, linha + 2, coluna + 2));
            if (podeCapturar(jogador, linha, coluna, linha - 1, coluna + 1, linha - 2, coluna + 2))
                movimementos.addElement(new MovimentoPeca(linha, coluna, linha - 2, coluna + 2));
            if (podeCapturar(jogador, linha, coluna, linha + 1, coluna - 1, linha + 2, coluna - 2))
                movimementos.addElement(new MovimentoPeca(linha, coluna, linha + 2, coluna - 2));
            if (podeCapturar(jogador, linha, coluna, linha - 1, coluna - 1, linha - 2, coluna - 2))
                movimementos.addElement(new MovimentoPeca(linha, coluna, linha - 2, coluna - 2));
        }
    }

}