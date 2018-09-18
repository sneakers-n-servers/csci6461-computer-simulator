package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterController;
import edu.gw.csci.simulator.registers.RegisterType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Map;

public class Registers extends VBox {

    public Registers(){
        RegisterController registerController = new RegisterController();
        registerController.initializeRegisters();
        for(Map.Entry<RegisterType, Register> registerEntry: registerController.getRegisters()){
            Label label = new Label(registerEntry.getKey().toString());
            TextField textField = new TextField();
            this.getChildren().addAll(label, textField);
        }
    }
}
