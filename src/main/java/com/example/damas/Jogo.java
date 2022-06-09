package com.example.damas;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Jogo extends Group implements EventHandler<MouseEvent>
{
    private Button btn_desistir;
    private Button btn_novoJogo;
    private Label mensagem;

    private Tabuleiro tabuleiro;
    private MovimentoPeça[] movimentoPeças;

    private int linhaSelecionada, colunaSelecionada;

    private int jogador2; // Jogador Atual
    private int jogador1; // Meu Jogador

    private Rectangle[][] retangulos = new Rectangle[DadosTabuleiro.TAMANHO_TABULEIRO][DadosTabuleiro.TAMANHO_TABULEIRO];
    private Circle[][] pecas = new Circle[DadosTabuleiro.TAMANHO_TABULEIRO][DadosTabuleiro.TAMANHO_TABULEIRO];

    private boolean jogoDecorrer;

    public Jogo(Button btn_desistir, Button btn_novoJogo, Label mensagem)
    {
        this.btn_desistir = btn_desistir;
        this.btn_novoJogo = btn_novoJogo;
        this.mensagem = mensagem;
        this.btn_desistir.setOnMouseClicked(this);
        this.btn_novoJogo.setOnMouseClicked(this);
        tabuleiro = new Tabuleiro();
        comecarNovoJogo();

    }

    public void setJogoDecorrer(boolean jogoDecorrer) {
        this.jogoDecorrer = jogoDecorrer;
    }

    public void setJogador1(int jogador1) {
        this.jogador1 = jogador1;
    }

    public void setJogador2(int jogador2) {
        this.jogador2 = jogador2;
    }

    private void gameOver(String str)
    {
        mensagem.setText(str);
        btn_novoJogo.setDisable(false);
        btn_desistir.setDisable(true);
        jogoDecorrer = false;
    }

    public void comecarNovoJogo()
    {
        if(jogoDecorrer)
        {
            mensagem.setText("Tens de acabar o jogo que está a decorrer!");
            return;
        }
        tabuleiro.prepararJogo();
        jogador2 = Tabuleiro.BRANCA;
        movimentoPeças = tabuleiro.verificaMovimento(Tabuleiro.BRANCA);
        linhaSelecionada = -1;
        mensagem.setText("Jogador 1: Joga!");
        jogoDecorrer = true;
        btn_novoJogo.setDisable(true);
        btn_desistir.setDisable(false);
        redesenharTabuleiro();

    }

    public void desistirJogo()
    {
        if(!jogoDecorrer)
        {
            mensagem.setText("Não há nenhum jogo a decorrer!");
            return;
        }
        if(jogador2 == Tabuleiro.BRANCA)
            gameOver("Jogador 1 desistiu. Jogador 2 ganhou");
        else
            gameOver("Jogador 2 desistiu. Jogador 1 ganhou");
    }

    private void desenhaTabuleiro()
    {
        Image pecabranca = new Image("C:\\Users\\berna\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaBranca.png");
        Image pecapreta = new Image("C:\\Users\\berna\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaBlack.png");
        Image pecaescolhida = new Image("C:\\Users\\berna\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaEscolhida.png");
        Image rainhabranca = new Image("C:\\Users\\berna\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\RainhaBranca.png");
        Image rainhapreta = new Image("C:\\Users\\berna\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\RainhaBlack.png");

        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
        {
            if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
                return;
            for(int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
            {
                Rectangle retangulo = new Rectangle(coluna * DadosTabuleiro.TAMANHO, linha * DadosTabuleiro.TAMANHO, DadosTabuleiro.TAMANHO, DadosTabuleiro.TAMANHO);
                retangulo.setId(linha + "-" + coluna);

                if (linha % 2 == coluna % 2)
                    retangulo.setFill(Color.GRAY);
                else
                    retangulo.setFill(Color.WHITE);
                retangulos[linha][coluna] = retangulo;

                Circle peca = new Circle(coluna * DadosTabuleiro.TAMANHO + (DadosTabuleiro.TAMANHO / 2), linha * DadosTabuleiro.TAMANHO + (DadosTabuleiro.TAMANHO / 2), DadosTabuleiro.RADIUS);
                peca.setId(linha + "-" + coluna);
                switch (tabuleiro.peçaEm(linha, coluna))
                {
                    case Tabuleiro.BRANCA:
                        peca.setFill(new ImagePattern(pecabranca, 1, 1, 1, 1, true));
                        pecas[linha][coluna] = peca;
                        break;
                    case Tabuleiro.PRETA:
                        peca.setFill(new ImagePattern(pecapreta, 1, 1, 1, 1, true));
                        pecas[linha][coluna] = peca;
                        break;
                    case Tabuleiro.BRANCA_RAINHA:
                        peca.setFill(new ImagePattern(rainhabranca, 1, 1, 1, 1, true));
                        pecas[linha][coluna] = peca;
                        break;
                    case Tabuleiro.PRETA_RAINHA:
                        peca.setFill(new ImagePattern(rainhapreta, 1, 1, 1, 1, true));
                        pecas[linha][coluna] = peca;
                        break;
                    default:
                        pecas[linha][coluna] = null;
                        break;
                }
            }
            if (!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
                return;
        }
        if(jogoDecorrer)
        {
            for(MovimentoPeça movimentoPeça : movimentoPeças)
                retangulos[movimentoPeça.deLinha][movimentoPeça.deColuna].setFill(Color.LIGHTSALMON);

            if(linhaSelecionada >= 0)
            {
                pecas[linhaSelecionada][colunaSelecionada].setFill(new ImagePattern(pecaescolhida, 1, 1, 1, 1, true));
                pecas[linhaSelecionada][colunaSelecionada].setStrokeWidth(3);
            }
        }

        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
            for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++)
            {
                retangulos[linha][coluna].setOnMouseClicked(this);
                getChildren().add(retangulos[linha][coluna]);
                if(pecas[linha][coluna] != null)
                {
                    pecas[linha][coluna].setOnMouseClicked(this);
                    getChildren().add(pecas[linha][coluna]);
                }
            }
    }

    private void redesenharTabuleiro()
    {
        desenhaTabuleiro();
    }

    public void clickQuadrado(int linha, int coluna)
    {
        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
            return;
        for(MovimentoPeça movimentoPeça : movimentoPeças)
            if (movimentoPeça.deLinha == linha && movimentoPeça.deColuna == coluna)
            {
                linhaSelecionada = linha;
                colunaSelecionada = coluna;
                if(jogador2 == Tabuleiro.BRANCA)
                    mensagem.setText("Jogador 1: JOGA!");
                else
                    mensagem.setText("Jogador 2: JOGA!");
                return;
            }

        if (!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
            return;
        if (linhaSelecionada < 0)
        {
            mensagem.setText("Carrega na peça que queres mover");
            return;
        }

        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
            return;
        for (MovimentoPeça movimentoPeça : movimentoPeças)
            if (movimentoPeça.deLinha == linhaSelecionada && movimentoPeça.deColuna == colunaSelecionada
                    && movimentoPeça.paraLinha == linha && movimentoPeça.paraColuna == coluna)
            {
                realizarMovimento(movimentoPeça);
                return;
            }

        mensagem.setText("Clica no quadrado que desejas que a peça se mova");
    }

    private void clickQuadrado(String x, String y)
    {
        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
            return;
        clickQuadrado(Integer.parseInt(x), Integer.parseInt(y));
    }

    private void realizarMovimento(MovimentoPeça movimentoPeça) {
        tabuleiro.moverPeça(movimentoPeça);

        if (movimentoPeça.comerPeça()) {
            movimentoPeças = tabuleiro.verificaCaptura(jogador2, movimentoPeça.paraLinha, movimentoPeça.paraColuna);
            if (movimentoPeças != null) {
                if (jogador2 == Tabuleiro.BRANCA)
                    mensagem.setText("Jogador 1: Tens de continuar a capturar");
                else
                    mensagem.setText("Jogador 2: Tens de continuar a capturar");
                linhaSelecionada = movimentoPeça.paraLinha;
                colunaSelecionada = movimentoPeça.paraColuna;
                redesenharTabuleiro();
                return;
            }
        }

        if (jogador2 == Tabuleiro.BRANCA) {
            jogador2 = Tabuleiro.PRETA;
            movimentoPeças = tabuleiro.verificaMovimento(jogador2);
            if (movimentoPeças == null)
                gameOver("Jogador 2 sem jogadas.  Jogador 1 ganhou.");
            else if (movimentoPeças[0].comerPeça())
                mensagem.setText("Jogador 2:  JOGA!.  Tens de capturar");
            else
                mensagem.setText("Jogador 2:  JOGA!.");
        } else {
            jogador2 = Tabuleiro.BRANCA;
            movimentoPeças = tabuleiro.verificaMovimento(jogador2);
            if (movimentoPeças == null)
                gameOver("Jogador 1 sem jogadas.  Jogador 2 ganhou.");
            else if (movimentoPeças[0].comerPeça())
                mensagem.setText("Jogador 1:  JOGA!.  Tens de capturar");
            else
                mensagem.setText("Jogador 1:  JOGA!.");
        }

        linhaSelecionada = -1;

        if (movimentoPeças != null) {
            boolean mesmoQuadrado = true;
            for (int i = 1; i < movimentoPeças.length; i++)
                if (movimentoPeças[i].deLinha != movimentoPeças[0].deLinha
                        || movimentoPeças[i].deColuna != movimentoPeças[0].deColuna) {
                    mesmoQuadrado = false;
                    break;
                }
            if (mesmoQuadrado) {
                linhaSelecionada = movimentoPeças[0].deLinha;
                colunaSelecionada = movimentoPeças[0].deColuna;
            }
        }


        redesenharTabuleiro();
    }

    @Override
    public void handle(MouseEvent mouseEvent)
    {

    }
}
