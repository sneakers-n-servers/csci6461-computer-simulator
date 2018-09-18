package edu.gw.csci.simulator.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulator extends Application {

    private static final int APP_WIDTH = 1000, APP_HEIGHT = 750;
    private final String TITLE = "CSCI 6461 Simulator";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/simulator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, APP_WIDTH, APP_HEIGHT));
        primaryStage.show();
    }
}
