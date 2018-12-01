package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.gui.BitDecorator;
import edu.gw.csci.simulator.utils.BitConversion;
import edu.gw.csci.simulator.utils.FloatingPointConvert;
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

    /**
     * This method converts the register name to a {@link SimpleStringProperty}
     * for display in the console
     * @return The converted name
     */
    public SimpleStringProperty getRegisterName() {
        Register register = super.getBitType();
        return new SimpleStringProperty(register.getName());
    }

    /**
     * This method converts the register description to a {@link SimpleStringProperty}
     * for display in the console
     * @return The converted description
     */
    public SimpleStringProperty getRegisterDescription() {
        Register register = super.getBitType();
        return new SimpleStringProperty(register.getDescription());
    }

    /**
     * This method converts either to decimal, or to float depending on the register
     * being displayed. Floating point values are fixed to 5 decimal places for readability only.
     *
     * @return The converted value
     */
    public SimpleStringProperty toFloatOrIntObservableString(){
        Register register = super.getBitType();
        BitSet bitSet = register.getData();
        if(bitSet == null){
            return new SimpleStringProperty(BitDecorator.NULL_STRING);
        }

        String result;
        if(RegisterType.getFloatingPointTypes().contains(register.getRegisterType())){
            float f = FloatingPointConvert.FloatConvert(bitSet);
            String formatted = String.format("%.5f", f);
            result = formatted;
        }else{
            int i = BitConversion.convert(bitSet);
            result = Integer.toString(i);
        }
        return new SimpleStringProperty(result);
    }
}
