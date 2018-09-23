package edu.gw.csci.simulator;

import edu.gw.csci.simulator.gui.Simulator;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * Main entry into computer simulator.
     * This instantiates the GUI and also the underlying computer.
     * 
     *
     * @param args standard Java context; no arguments defined for version 1.
     */
    public static void main(String [] args){
        logger.info("Starting application...");
        Application.launch(Simulator.class, args);
    }
}
