package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.utils.ConsoleAppender;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Arrays;

public class LogController {

    @FXML
    private ComboBox<String> logLevels;

    @FXML
    private TextFlow developerLog;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void initialize() {
        initializeLogs(Level.INFO);
        ConsoleAppender.setTextFlow(developerLog);
        ConsoleAppender.setScrollPane(scrollPane);
    }

    /**
     * This function initializes logging fort the application by setting the drop down
     * list of logging levels: ALL, DEBUG, TRACE, etc. The text area used by the
     * log4j logger is statically bound. When the log level changes, a bound function
     * sets the new global log level, as specifed by the user.
     *
     * @param level The desired logging level
     */
    private void initializeLogs(Level level) {
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
    }

    @FXML
    private void clear() {
        ConsoleAppender.clear();
    }

}
