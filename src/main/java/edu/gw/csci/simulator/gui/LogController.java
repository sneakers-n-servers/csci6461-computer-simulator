package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.utils.ConsoleAppender;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Arrays;

public class LogController {

    @FXML
    private ComboBox<String> logLevels = new ComboBox<>();

    @FXML
    private TextArea developerLog;

    @FXML
    private void initialize(){
        handleLogs(Level.INFO);
    }

    private void handleLogs(Level level){
        //Set the default list of log levels
        String[] logValues = Arrays.stream(Level.values())
                .map(Level::name)
                .toArray(String[]::new);
        Arrays.sort(logValues);
        logLevels.getItems().addAll(logValues);
        logLevels.setOnAction(event -> {
            Level currentLevel = Level.valueOf(logLevels.getValue());
            Configurator.setRootLevel(currentLevel);
        });
        logLevels.setValue(level.toString());

        //Set the text field to append to
        ConsoleAppender.setTextArea(developerLog);
    }

}
