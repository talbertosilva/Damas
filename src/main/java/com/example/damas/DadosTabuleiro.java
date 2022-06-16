package com.example.damas;

/**
 *<p>Dados Tabuleiro - Responsável por variáveis do tamanho do tabuleiro, etc...</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */
public class DadosTabuleiro
{
    static final int TAMANHO_TABULEIRO = 8;
    private static final double multiplicador = 3;
    static final int RADIUS = (int) (8 * multiplicador);
    public static final int TAMANHO = (int) (20 * multiplicador);
    public static boolean OPEN = false;
}
