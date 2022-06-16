package com.example.damas;

import com.example.damas.conexao.NetworkInterface;
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

/**
 *<p>Jogo -Lógica toda do jogo.</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */

public class Jogo extends Group implements EventHandler<MouseEvent>
{
    private Button btn_desistir;
    private Button btn_novoJogo;
    private Label mensagem;

    private Tabuleiro tabuleiro;
    private MovimentoPeca[] movimentoPecas;

    private NetworkInterface netInterface;

    private int linhaSelecionada, colunaSelecionada;

    private int jogador;
    private int jogadorAtual;

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

    public void setJogador(int jogador) {
        this.jogador = jogador;
    }

    public void setNetworkInterface(NetworkInterface netInterface) {
        this.netInterface = netInterface;
    }

    /**
     * O metodo comecarNovoJogo é o metodo principal para iniciar um novo jogo,
     * este cria um novo tabuleiro, atribui a primeira jogada ao jogador das
     * peças brancas, verifica os movimentos possíveis iniciais e faz toda a
     * gestão inicial dos botões.
     */
    public void comecarNovoJogo() {
        if(jogoDecorrer) {
            mensagem.setText("Tens de acabar o jogo que está a decorrer!");
            return;
        }
        tabuleiro.prepararJogo();
        jogadorAtual = Tabuleiro.BRANCA;
        movimentoPecas = tabuleiro.verificaMovimento(Tabuleiro.BRANCA);
        linhaSelecionada = -1;
        mensagem.setText("Jogador 1: Joga!");
        jogoDecorrer = true;
        btn_novoJogo.setDisable(true);
        btn_desistir.setDisable(false);
        redesenharTabuleiro();

    }

    /**
     * O metodo DesistirJogo verifica as peças que desistiram
     * e reencaminha uma mensagem para a função de gameOver, para
     * atualizar a mensagem no topo do programa.
     */
    public void desistirJogo() {
        if(jogadorAtual == Tabuleiro.BRANCA)
            gameOver("As peças brancas desistiram. Ganharam as peças pretas!");
        else
            gameOver("As peças pretas desistiram. Ganharam as peças brancas!");
    }

    /**
     * O metodo GameOver disabilita o botão de desistir e reabilita
     * o botão de novo jogo. Por sua vez, também atualiza a mensagem
     * no topo do programa e atualiza o estado do jogo para false (terminado).
     */
    private void gameOver(String str) {
        mensagem.setText(str);
        btn_novoJogo.setDisable(false);
        btn_desistir.setDisable(true);
        jogoDecorrer = false;
    }

    /**
     * O metodo clickQuadrado verifica clique efetuado, que, acaba
     * por ser a seleção da peça e a posição para onde se que quer mover.
     * Caso clique na peça errada, é avisado e não é feito a devida seleção
     * da peça. Caso clique num campo que não tenha nenhuma peça, também é
     * feito um aviso. Por último, verifica a posição da peça selecionada e a posição
     * para onde se quer mover, verificando assim se é igual a uma
     * das posições possíveis, caso se verifique, é realizado o movimento.
     */
    public void clickQuadrado(int linha, int coluna) {
        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8) return;
        for(MovimentoPeca movimentoPeca : movimentoPecas)
            if (movimentoPeca.deLinha == linha && movimentoPeca.deColuna == coluna) {
                linhaSelecionada = linha;
                colunaSelecionada = coluna;
                if(jogadorAtual == Tabuleiro.BRANCA)
                    mensagem.setText("É a vez das peças brancas.");
                else
                    mensagem.setText("É a vez das peças pretas.");
                redesenharTabuleiro();
                return;
            }

        if (!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8) return;
        if (linhaSelecionada < 0) {
            mensagem.setText("Carrega na peça que queres mover");
            return;
        }

        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8) return;
        System.out.println("Movimentos possiveis:");
        for(int i = 0; i< movimentoPecas.length; i++){
            System.out.println("Linha: " + movimentoPecas[i].paraLinha + " - Coluna: " + movimentoPecas[i].paraColuna);
        }

        for (MovimentoPeca movimentoPeca : movimentoPecas){
            if (movimentoPeca.deLinha == linhaSelecionada && movimentoPeca.deColuna == colunaSelecionada && movimentoPeca.paraLinha == linha && movimentoPeca.paraColuna == coluna) {
                realizarMovimento(movimentoPeca);
                return;
            }
        }

        mensagem.setText("Clica no quadrado que desejas que a peça se mova");
    }

    private void clickQuadrado(String x, String y)
    {
        if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8)
            return;
        clickQuadrado(Integer.parseInt(x), Integer.parseInt(y));
    }

    /**
     * O metodo realizarMovimento verifica o movimento em si,
     * caso haja uma captura depois do movimento, este movimento
     * continua e o jogador é obrigado a capturar a próxima peça.
     * Caso não haja mais nenhum movimento, é trocado de jogador
     * e imediatamente verificado os movimentos desse jogador. Se
     * esse jogador não tiver mais movimentos, é encerrado o jogo.
     */
    private void realizarMovimento(MovimentoPeca movimentoPeca) {
        tabuleiro.moverPeça(movimentoPeca);

        if (movimentoPeca.comerPeça()) {
            movimentoPecas = tabuleiro.verificaCaptura(jogadorAtual, movimentoPeca.paraLinha, movimentoPeca.paraColuna);
            if (movimentoPecas != null) {
                if (jogadorAtual == Tabuleiro.BRANCA)
                    mensagem.setText("As peças brancas são obrigadas a capturar.");
                else
                    mensagem.setText("As peças pretas são obrigadas a capturar.");
                linhaSelecionada = movimentoPeca.paraLinha;
                colunaSelecionada = movimentoPeca.paraColuna;
                redesenharTabuleiro();
                return;
            }
        }

        if (jogadorAtual == Tabuleiro.BRANCA) {
            jogadorAtual = Tabuleiro.PRETA;
            movimentoPecas = tabuleiro.verificaMovimento(jogadorAtual);
            if (movimentoPecas == null)
                gameOver("As peças brancas ganharam!");
            else if (movimentoPecas[0].comerPeça()){
                mensagem.setText("A peça preta é obrigada a capturar.");
            }
            else
                mensagem.setText("É a vez das peças pretas.");
        } else {
            jogadorAtual = Tabuleiro.BRANCA;
            movimentoPecas = tabuleiro.verificaMovimento(jogadorAtual);
            if (movimentoPecas == null)
                gameOver("As peças pretas ganharam!");
            else if (movimentoPecas[0].comerPeça()) {
                mensagem.setText("A peça branca é obrigada a capturar.");
            }
            else
                mensagem.setText("É a vez das peças brancas.");
        }

        linhaSelecionada = -1;

        redesenharTabuleiro();
    }

    /**
     * O metodo redesenharTabuleiro atualiza o tabuleiro.
     */
    private void redesenharTabuleiro()
    {
        desenhaTabuleiro();
    }

    /**
     * O metodo desenhaTabuleiro é responsável por atribuir
     * toda a parte gráfica ao tabuleiro. Como por exemplo, atribuir as imagens
     * das peças às devidas posições, atribuir cor às peças possíveis
     * a mover.
     */
    private void desenhaTabuleiro()
    {
        Image pecabranca = new Image("C:\\Users\\sorai\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaBranca.png");
        Image pecapreta = new Image("C:\\Users\\sorai\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaBlack.png");
        Image pecaescolhida = new Image("C:\\Users\\sorai\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\PecaEscolhida.png");
        Image rainhabranca = new Image("C:\\Users\\sorai\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\RainhaBranca.png");
        Image rainhapreta = new Image("C:\\Users\\sorai\\Desktop\\Damas\\src\\main\\java\\com\\example\\damas\\assets\\RainhaBlack.png");

        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++) {
            if(!DadosTabuleiro.OPEN && DadosTabuleiro.TAMANHO_TABULEIRO < 8) return;
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

        if(jogoDecorrer) {
            for(MovimentoPeca movimentoPeca : movimentoPecas) {
                retangulos[movimentoPeca.deLinha][movimentoPeca.deColuna].setFill(Color.LIGHTSALMON);
            }

            if(linhaSelecionada >= 0) {
                pecas[linhaSelecionada][colunaSelecionada].setFill(new ImagePattern(pecaescolhida, 1, 1, 1, 1, true));
                pecas[linhaSelecionada][colunaSelecionada].setStrokeWidth(3);
            }
        }

        for (int linha = 0; linha < DadosTabuleiro.TAMANHO_TABULEIRO; linha++)
            for (int coluna = 0; coluna < DadosTabuleiro.TAMANHO_TABULEIRO; coluna++) {
                retangulos[linha][coluna].setOnMouseClicked(this);
                getChildren().add(retangulos[linha][coluna]);
                if(pecas[linha][coluna] != null)
                {
                    pecas[linha][coluna].setOnMouseClicked(this);
                    getChildren().add(pecas[linha][coluna]);
                }
            }
    }

    /**
     * O metodo handle é o responsável por comunicar com a parte dos
     * sockets. Este método utiliza as três funções da interface da
     * network, caso o jogador clique em desistir, é invocado a função
     * de desistir, por outro lado, caso o jogador clique no tabuleiro,
     * é invocado a função da interface clickQuadrado, com a devida posição no tabuleiro.
     */
    @Override
    public void handle(MouseEvent mouseEvent){
        Object src = mouseEvent.getSource();
        if (src == btn_novoJogo) {
            comecarNovoJogo();
            netInterface.comecarNovoJogo();
        } else if (src == btn_desistir) {
            desistirJogo();
            netInterface.desistirJogo();
        } else if (src instanceof Rectangle) {
            String[] coordinates = ((Rectangle) src).getId().split("-");
            if (netInterface != null) {
                if (jogador != jogadorAtual) {
                    mensagem.setText("Essa peça não é tua");
                    return;
                }
                netInterface.onClickQuadrado(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
            }
            clickQuadrado(coordinates[0], coordinates[1]);
        } else if (src instanceof Circle) {
            String[] coordinates = ((Circle) src).getId().split("-");
            if (netInterface != null) {
                if (jogador != jogadorAtual) {
                    mensagem.setText("Essa peça não é tua");
                    return;
                }
                netInterface.onClickQuadrado(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
            }
            clickQuadrado(coordinates[0], coordinates[1]);
        }
    }
}