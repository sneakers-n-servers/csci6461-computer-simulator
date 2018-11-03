package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.gui.BitDecorator;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.beans.property.SimpleStringProperty;

import java.util.BitSet;

/**
 * Provides abstraction of support operations for interaction between the simulated registers and the GUI framework.
 *
 * @version 20180918
 */
public class RegisterDecorator extends BitDecorator<Register> {


    public RegisterDecorator(Register register) {
        super(register);
    }

    public SimpleStringProperty getRegisterName() {
        Register register = super.getBitType();
        return new SimpleStringProperty(register.getName());
    }

    public SimpleStringProperty getRegisterDescription(){
        Register register = super.getBitType();
        return new SimpleStringProperty(register.getDescription());
    }
}
