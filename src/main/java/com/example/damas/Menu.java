package com.example.damas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *<p>Menu - Contém funções de cliques nos diversos botões do menu.</p>
 *
 * @author Tiago Silva, Bernardo Azevedo, Gaspar Espinheira
 */
public class Menu {

    private Stage stage;
    private Scene scene;

    public void menuJogar(MouseEvent mouseEvent) throws IOException
    {
        DadosTabuleiro.OPEN = true;
        Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
        stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void menuRegras(MouseEvent mouseEvent) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("regras.fxml"));
        stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void menuSair(MouseEvent mouseEvent) throws IOException
    {
        System.exit(0);
    }

    public void menuInstrucoes(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("instrucoes.fxml"));
        stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
