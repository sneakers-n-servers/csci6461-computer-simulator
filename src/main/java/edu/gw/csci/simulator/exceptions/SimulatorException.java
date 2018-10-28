package edu.gw.csci.simulator.exceptions;

import edu.gw.csci.simulator.Bits;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.memory.MemoryChunk;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;

/**
 * Catch-all for exceptions from the application rather than the simulated computer.
 *
 * @version 20180916
 */
public abstract class SimulatorException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger(SimulatorException.class);

    private static AllMemory allMemory;
    private static AllRegisters allRegisters;
    private static TableView<? extends Bits> registerTable, memoryTable;

    public static final int HALT_LOCATION = 6;

    public SimulatorException(String message) {
        LOGGER.error(message);
        setFault();
    }

    public static void setAllMemory(AllMemory allMemory){
        SimulatorException.allMemory = allMemory;
        SimulatorException.allRegisters = allMemory.getAllRegisters();
    }

    public static void setTables(TableView<Register> registerTable, TableView<MemoryChunk> memoryTable){
        SimulatorException.registerTable = registerTable;
        SimulatorException.memoryTable = memoryTable;
    }

    abstract int getOpcode();

    /**
     *
     */
    public void setFault(){
        int opCode = this.getOpcode();
        Register machineFaultRegister = allRegisters.getRegister(RegisterType.MFR);
        new RegisterDecorator(machineFaultRegister).setValue(opCode);

        Register pcRegister = allRegisters.getRegister(RegisterType.PC);
        RegisterDecorator pcDecorator = new RegisterDecorator(pcRegister);
        allMemory.store(2, pcDecorator.toInt(), false);

        BitSet haltData = allMemory.fetch(1, false);
        pcDecorator.setValue(BitConversion.convert(haltData));

        if(registerTable != null){
            registerTable.edit(-1, null);
            registerTable.refresh();
        }
        if(memoryTable != null){
            memoryTable.edit(-1, null);
            memoryTable.refresh();
        }
    }
}
