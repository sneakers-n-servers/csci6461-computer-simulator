package edu.gw.csci.simulator.registers;


import edu.gw.csci.simulator.isa.SetCC;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Collects all registers to define the processor's full internal memory - except for the future caches.
 *
 * @version 20180918
 */
public class AllRegisters {

    private static final Logger LOGGER = LogManager.getLogger(AllRegisters.class);

    private HashMap<RegisterType, Register> registerMap;

    public AllRegisters() {
        registerMap = new LinkedHashMap<>();
        for (RegisterType registerType : RegisterType.values()) {
            registerMap.put(registerType, new Register(registerType));
        }
    }

    public void initialize() {
        for (Register register : registerMap.values()) {
            LOGGER.debug(String.format("Initialing register %s", register.getName()));
            register.initialize();
        }
    }

    public void bindTableView(TableView tableView) {
        for (Register register : registerMap.values()) {
            register.getBitSetProperty().addListener((observable, oldValue, newValue) -> tableView.refresh());
        }
    }

    public Set<Map.Entry<RegisterType, Register>> getRegisters() {
        return registerMap.entrySet();
    }


    public void setRegister(RegisterType registerType, BitSet bitSet) {
        Register register = registerMap.get(registerType);
        register.setData(bitSet);
    }

    public Register getRegister(RegisterType registerType) {
        return registerMap.get(registerType);
    }

    public void OVERFLOW() {
        Register CC = getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(3);
        CC.setData(bits);
        LOGGER.warn("OVERFLOW");
    }

    public void UNDERFLOW() {
        Register CC = getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(2);
        CC.setData(bits);
        LOGGER.warn("UNDERFLOW");
    }

    public void DIVZERO() {
        Register CC = getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(1);
        CC.setData(bits);
        LOGGER.warn("DIVIDE 0");
    }

    public void EQUALORNOT(boolean equal) {
        Register CC = getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        if (equal) {
            bits.set(0);
            LOGGER.info("EQUAL");
        } else {
            bits.clear(0);
            LOGGER.info("NOTEQUAL");
        }
        CC.setData(bits);
    }

    public boolean checkOverUnderFlow(int value) {
        if (value > SetCC.MaxValue || value < SetCC.MinValue) {
            OVERFLOW();
            return true;
        } else {
            return false;
        }
    }

    public boolean checkExtendOverUnderFlow(int value) {
        if (value > SetCC.ExtendMaxValue || value < SetCC.ExtendMinValue) {
            OVERFLOW();
            return true;
        } else {
            return false;
        }
    }
}
