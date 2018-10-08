package edu.gw.csci.simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Instantiate the main GUI screen and serve as parent target for user events.
 *
 * @version 20180918
 */
public class Simulator extends Application {

    private static final int APP_WIDTH = 1500, APP_HEIGHT = 1000;
    private static final String TITLE = "CSCI 6461 Simulator";
    private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/simulator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle(TITLE);
        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/gui/simulator.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        LOGGER.info("Starting application...");
        launch(args);
    }
}
