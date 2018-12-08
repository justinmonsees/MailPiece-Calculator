package com.monsees.mailpiececalc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //load the FXML file
        //Parent root = FXMLLoader.load(this.getClass().getResource("resources/sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));

        Scene scene = new Scene(root,400,645);

        //load the CSS file
        scene.getStylesheets().add("/styles/Styles.css");


        primaryStage.setTitle("Mail Piece Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);

    }



}
