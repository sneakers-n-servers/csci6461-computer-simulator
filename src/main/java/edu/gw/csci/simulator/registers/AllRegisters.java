package edu.gw.csci.simulator.registers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AllRegisters {

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

    public Set<Map.Entry<RegisterType, Register>> getRegisters(){
        return registerMap.entrySet();
    }
}
