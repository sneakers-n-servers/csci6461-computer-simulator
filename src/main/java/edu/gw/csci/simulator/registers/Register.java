package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.Bits;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.BitSet;

/**
 * Leverages the defined register type to extend properties to other registers.
 *
 * @version 20180918
 */
public class Register implements Bits {

    private final RegisterType registerType;
    private ObjectProperty<BitSet> data;

    public Register(RegisterType registerType) {
        if (registerType.getSize() > 64) {
            throw new IllegalArgumentException("Can't instantiate register size larger than 64 bits");
        }
        this.registerType = registerType;
        this.data = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize() {
        BitSet bitSet = new BitSet(registerType.getSize());
        data.set(bitSet);
    }

    @Override
    public BitSet getData() {
        return data.get();
    }

    @Override
    public int getSize() {
        return registerType.getSize();
    }

    public String getName() {
        return registerType.toString();
    }

    public String getDescription() {
        return registerType.getDescription();
    }

    public RegisterType getRegisterType(){
        return this.registerType;
    }

    public void setData(BitSet data) {
        if (data.length() > getSize()) {
            String mess = String.format(
                    "Binary value %s is larger than maximum %d",
                    BitConversion.toBinaryString(data, data.length()),
                    getSize()
            );
            throw new IllegalValue(mess);
        }
        this.data.setValue(data);
    }

    ObjectProperty<BitSet> getBitSetProperty() {
        return data;
    }
}
