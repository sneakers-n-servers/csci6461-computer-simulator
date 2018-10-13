package edu.gw.csci.simulator.cpu;

import edu.gw.csci.simulator.gui.Program;
import edu.gw.csci.simulator.isa.*;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.registers.AllRegisters;
import javafx.scene.control.TextArea;


public class CPU {

    private final AllMemory memory;
    private final AllRegisters registers;
    private Program program;
    private TextArea consoleInput;

    private Decoder decoder = new Decoder();

    public CPU(Memory memory, AllRegisters registers){
        this.memory = new AllMemory(memory, registers);
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
}
