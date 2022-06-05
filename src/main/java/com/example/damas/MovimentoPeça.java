package com.example.damas;

public class MovimentoPeça
{
    // A CheckersMove object represents a move in the game of CheckersController.
    // It holds the row and column of the piece that is to be moved
    // and the row and column of the square to which it is to be moved.
    // (This class makes no guarantee that the move is legal.)
    int deLinha, deColuna;  // Position of piece to be moved.
    int paraLinha, paraColuna;      // Square it is to move to.

    MovimentoPeça(int l1, int c1, int l2, int c2) {
        // Constructor.  Just set the values of the instance variables.
        deLinha = l1;
        deColuna = c1;
        paraLinha = l2;
        paraColuna = c2;
    }

    boolean comerPeça() {
        // Main whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
        return (deLinha - paraLinha == 2 || deLinha - paraLinha == -2);
    }
}
