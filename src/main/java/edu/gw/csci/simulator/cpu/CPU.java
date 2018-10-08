package edu.gw.csci.simulator.cpu;

import edu.gw.csci.simulator.isa.*;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.registers.AllRegisters;

public class CPU {

    private final AllMemory memory;
    private final AllRegisters registers;
    private Program program;

    private Decoder decoder = new Decoder();

    public CPU(Memory memory, AllRegisters registers){
        this.memory = new AllMemory(memory, registers);
        this.registers = registers;
    }

    public void setProgram(Program program){
        this.program = program;
    }

    public void execute(){
        for(String line : program.getLines()){
            String typeString = line.substring(0, 6);
            String dataString = line.substring(6);
            InstructionType instructionType = InstructionType.getInstructionType(typeString);
            InstructionFactory instructionFactory = decoder.getInstructionFactory(instructionType);

            Instruction instruction;
            try {
                instruction = instructionFactory.create(dataString);
            } catch (Exception e) {
                //At this point the only thing that can go wrong are reflection errors
                //We should handle the exception here, but it programatic, and unrelated to
                //the machine's state
                e.printStackTrace();
                return;
            }
            instruction.execute(memory, registers);
        }
    }
}
