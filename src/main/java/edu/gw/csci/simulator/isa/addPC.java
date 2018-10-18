package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;

public class addPC {
    /**
     * PC = PC+1
     */
    public static void PCadder(AllRegisters registers){
        RegisterDecorator PCd = new RegisterDecorator(registers.getRegister(RegisterType.PC));
        int PC= PCd.toInt();
        PCd.setRegister(PC+1);
    }
}
