package com.example.damas;

/**
 *<p>Movimento Peça - Contém os valores de qualquer movimento no jogo (de onde para onde)</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */
class MovimentoPeca {
    int deLinha, deColuna;
    int paraLinha, paraColuna;

    MovimentoPeca(int l1, int c1, int l2, int c2)
    {
        deLinha = l1;
        deColuna = c1;
        paraLinha = l2;
        paraColuna = c2;
    }

    boolean comerPeça()
    {
        return (deLinha - paraLinha == 2 || deLinha - paraLinha == -2);
    }
}
