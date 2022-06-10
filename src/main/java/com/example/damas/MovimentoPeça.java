package com.example.damas;

class MovimentoPeça {
    int deLinha, deColuna;
    int paraLinha, paraColuna;

    MovimentoPeça(int l1, int c1, int l2, int c2)
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
