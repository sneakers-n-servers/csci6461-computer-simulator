package edu.gw.csci.simulator.registers;


import edu.gw.csci.simulator.gui.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class AllRegisters {

    private static final Logger logger = LogManager.getLogger(AllRegisters.class);

    private HashMap<RegisterType, Register> registerMap;

    public AllRegisters() {
        registerMap = new LinkedHashMap<>();
        for(RegisterType registerType : RegisterType.values()){
            registerMap.put(registerType, new Register(registerType));
        }
    }

    public void initializeRegisters(){
        for(Register register: registerMap.values()){
            register.initialize();
        }
    }

    public void bindTableView(TableView tableView){
        for(Register register: registerMap.values()){
            register.getBitSetProperty().addListener(new ChangeListener<BitSet>() {
                @Override
                public void changed(ObservableValue<? extends BitSet> observable, BitSet oldValue, BitSet newValue) {
                    tableView.refresh();
                }
            });
        }
    }
    
    public Set<Map.Entry<RegisterType, Register>> getRegisters(){
        return registerMap.entrySet();
    }

    public List<Register> getRegisterList(){
        Collection<Register> registerCollection = registerMap.values();
        return new ArrayList<>(registerCollection);
    }

    public void setRegister(RegisterType registerType, int value){
        Register register = registerMap.get(registerType);
        RegisterDecorator rd = new RegisterDecorator(register);
        rd.setRegister(value);
    }

    public void setRegister(RegisterType registerType, BitSet bitSet){
        Register register = registerMap.get(registerType);
        register.setData(bitSet);
    }

    public Register getRegister(RegisterType registerType){
        return registerMap.get(registerType);
    }

    public void logRegisters(){
        for(Register register : registerMap.values()) {
            RegisterDecorator rd = new RegisterDecorator(register);
            String mess = String.format("%s: %d", register.getName(), rd.toInt());
            logger.info(mess);
        }
    }
}
