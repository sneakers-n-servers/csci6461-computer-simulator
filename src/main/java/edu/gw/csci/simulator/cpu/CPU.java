package edu.gw.csci.simulator.cpu;

import edu.gw.csci.simulator.gui.Program;
import edu.gw.csci.simulator.isa.*;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.memory.MemoryCache;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;
import java.util.List;


public class CPU {

    private static final Logger LOGGER = LogManager.getLogger(CPU.class);

    private final AllMemory memory;
    private final AllRegisters registers;

    private Program program;
    private TextArea consoleInput;

    private Decoder decoder = new Decoder();

    public CPU(Memory memory, AllRegisters registers, MemoryCache memoryCache){
        this.memory = new AllMemory(memory, registers, memoryCache);
        this.registers = registers;
    }

    public void setProgram(Program program){
        this.program = program;
    }

    public void setTextArea(TextArea textArea){
        this.consoleInput = textArea;
    }

    public void execute(){
        for(String line : program.getLines()){
            String typeString = line.substring(0, 6);
            String dataString = line.substring(6);
            InstructionType instructionType = InstructionType.getInstructionType(typeString);
            InstructionFactory instructionFactory = decoder.getInstructionFactory(instructionType);

            Instruction instruction = instructionFactory.create(dataString);
            instruction.execute(memory, registers);
        }
    }

    public void loadProgram(){
        int defaultLoadLocation = 32;
        BitSet programCounter = BitConversion.convert(defaultLoadLocation);
        List<String> lines = program.getLines();
        for (String line : lines) {
            LOGGER.info("Setting Line: " + line);
            BitSet convert = BitConversion.convert(line);
            memory.store(defaultLoadLocation, convert);
            defaultLoadLocation++;
        }
        registers.setRegister(RegisterType.PC, programCounter);
    }
}
