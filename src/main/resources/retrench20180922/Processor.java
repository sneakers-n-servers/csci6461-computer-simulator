/*
Project 1 - Processor

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180922
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180922
        Modified:   
    -Completed all cases for all five instructions;
    -however, still need to separate ST/LD into different methods

%%%     20180921
        Modified:   
    -Got pipeline working

%%%     20180920
        Modified:   
    -Moved definitions to InstructionSet class

%%%     20180916
        Author: Group 9    
        Project:    CSCI_6461_F18_Project1
    -Initial release

%%%%%%%%%%
*/

//IMPORT
//import edu.gw.csci.simulator.exceptions.*;

import projectexceptions.*;

import java.util.Arrays;

//BEGIN
public class Processor {
    //CONSTANTS
    private static final int DEFAULT_MAR = 6;
    // debug constants
    private static final int MODULE_DEBUG_FLAG = 1;

    //VARIABLES
    // interpreted instruction
    int[] instruction_register_fields;
    // controller signals
    // >0 for hold; 0 for incrementing
    private static int hold_PC;
    // 1 for set; 0 for get
    private static int set_memory;
    // temporary storage for MAR/MBR values
    private static int next_MAR;
    private static int next_MBR;

    private static int remaining_ticks;
    private static int new_instruction_flag;
    private static int branch_id;
    private static int allow_IR_set_flag;
    private static int decoded_IR_valid_flag;
    private static int IR_valid_flag;


    // declare ISA
    public InstructionSet instruction_set;
    // declare components to be "connected"
    // external memory
    private Memory memory;
    // internal registers
    public Registers registers;

    //CONSTRUCTORS
    public Processor(Memory memory) {
        // "connect" external memory
        this.memory = memory;
        // initialize registers
        registers = new Registers();
        // prepare ISA
        instruction_set = new InstructionSet();
        // initialize control signals
        hold_PC = 0;
        set_memory = 0;

        remaining_ticks = 0;
        new_instruction_flag = 0;
        branch_id = 0;
        allow_IR_set_flag = 0;
        IR_valid_flag = 0;
        decoded_IR_valid_flag = 0;


        // TBD - this is a cheat bootloader
        next_MAR = DEFAULT_MAR;
        registers.setMAR(next_MAR);
        registers.setPC(DEFAULT_MAR);
        next_MBR = instruction_set.NOP_VALUE;
        registers.setMBR(next_MBR);
        registers.setIR(next_MBR);
        // optional debug info
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Computer rebooted.");
            System.out.println("PC = 0x" + Integer.toHexString(registers.getPC())
                    + "; MAR = 0x" + Integer.toHexString(registers.getMAR())
                    + "; MBR = 0x" + Integer.toHexString(registers.getMBR())
                    + "; IR = " + registers.getIR() + ".");
            System.out.println("R: " + Arrays.toString(registers.R) + ".");
        }
    }

    //BEGIN
    public void step() throws projectexceptions.IllegalOpcode {
        //VARIABLES
        int[] current_instruction;
        //TBD int[] control_flags;
        long current_tick;
        //BEGIN
        // start clock cycle
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("\nvvvvv     vvvvv     vvvvv     vvvvv     vvvvv");
            System.out.println("Starting a clock cycle.");
            if ((instruction_register_fields != null) && (instruction_register_fields.length > 0)) {
                if ((decoded_IR_valid_flag == 1) || (remaining_ticks > 0)) {
                    System.out.println("Executing on opcode = " + instruction_register_fields[0] + ".");
                    System.out.println("Last IR decoded = " + registers.getIR() + ".");
                } else {
                    System.out.println("Last IR loaded = " + registers.getIR() + ".");
                    System.out.println("Wasted clock cycle?");
                }
            }
        }
        // exectue instruction
        if ((instruction_register_fields != null) && (instruction_register_fields.length > 0)
                && (instruction_register_fields[0] != instruction_set.OPCODE_NOP)
                && ((decoded_IR_valid_flag == 1) || (remaining_ticks > 0))) {
            executeInstructionLoadStore(instruction_register_fields);
            // remind itself that it needs a new value, unless it's finishing what it's doing
            decoded_IR_valid_flag = 0;
        } else {
            // TBD - possibly replace this with a fault handler
            remaining_ticks = 0;
        }
        // decode instruction
        if ((remaining_ticks < 1) && (IR_valid_flag == 1)) {
            new_instruction_flag = 1;
            try {
                instruction_register_fields = instruction_set.decodeInstruction(registers.getIR());
                decoded_IR_valid_flag = 1;
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Decoded IR: opcode = " + instruction_register_fields[0]
                            + ", corresponding to "
                            + instruction_set.opcode_mnemonics[instruction_register_fields[0]]
                            + " from instruction " + registers.getIR() + ".");
                }
            } catch (projectexceptions.IllegalOpcode opcode_error) {
                // announce that the value is bad
                IR_valid_flag = 0;
                // set fault register
                registers.setMFR(registers.FAULT_ILLEGAL_OPCODE);
                // optional debug info
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Memory Fault Register = 0x" + Integer.toHexString(registers.getMFR()) + ".");
                    opcode_error.printStackTrace();
                }
            } finally {
                // set to invalid to wait for a new value
                IR_valid_flag = 0;
            }
        }
        // fetch instruction; i.e., update IR
        if ((set_memory <= 0) && (remaining_ticks <= 0) && (allow_IR_set_flag == 0)) {
            registers.setIR(next_MBR);
            System.out.println("\n>>>> TBD - setIR " + remaining_ticks + ", " + allow_IR_set_flag + ", " + set_memory + ".");
            allow_IR_set_flag--;
            IR_valid_flag = 1;
        } else {
            System.out.println("\n>>>> TBD - not set " + remaining_ticks + ", " + allow_IR_set_flag + ", " + set_memory + ".");
        }
        // access memory; i.e., update MBR
        try {
            if (set_memory == 1) {
                registers.setMBR(next_MBR);
                taskMemoryControlUnit_set(memory);
                allow_IR_set_flag = -2;
            } else {
                taskMemoryControlUnit_get(memory);
                next_MBR = registers.getMBR();
            }
        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
            System.out.println("Memory Fault Register = 0x" + Integer.toHexString(registers.getMFR()) + ".");
            bound_error.printStackTrace();
        } finally {
            set_memory = 0;
        }
        // update MAR
        if (remaining_ticks <= 0) {
            next_MAR = registers.getPC();
            allow_IR_set_flag++;
        } else {
            next_MAR = next_MAR;
        }
        registers.setMAR(next_MAR);
        // update PC
        if (remaining_ticks <= 0) {
            // only advance on last cycle, otherwise the prior instruction will be skipped
            // advance PC - TBD this will have to get more sophisticated for a jump
            registers.incrementPC();
        }
        // one clock cycle
        current_tick = registers.tick();
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("\nThe time is now " + current_tick + " ticks.");
            System.out.println("PC = 0x" + Integer.toHexString(registers.getPC())
                    + "; MAR = 0x" + Integer.toHexString(registers.getMAR())
                    + "; MBR = 0x" + Integer.toHexString(registers.getMBR())
                    + "; IR = 0x" + Integer.toHexString(registers.getIR()) + ".");
            System.out.println("PC = " + registers.getPC()
                    + "; MAR = " + registers.getMAR()
                    + "; MBR = " + registers.getMBR()
                    + "; IR = " + registers.getIR() + ".");
            System.out.println("LRR = " + registers.getLRR() + ".");
            System.out.println("R: " + Arrays.toString(registers.R) + ".");
            System.out.println("X: " + Arrays.toString(registers.X) + ".");
        }
    }

    private void executeInstructionLoadStore(int[] instruction_fields) {
        int effective_address = 0;
        // TBD - clean up in submission 2 with proper abstraction
        int opcode = instruction_fields[0];
        int general_purpose_register = instruction_fields[1];
        int index_register = instruction_fields[2];
        int indirect_flag = instruction_fields[3];
        int address_field = instruction_fields[4];
        // temporary store for values to/fro memory
        int[] memory_words = new int[1];
        // no indirection
        // compute address
        try {
            effective_address = computeEffectiveAddress(0, instruction_fields[2],
                    instruction_fields[4], 0);
        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
        }
        switch (opcode) {
            case 1:
            case 33:
                // LDR LDX
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Executing LDR/X: R/X[" + instruction_fields[1]
                            + "] <-- MEM[" + address_field + "].");
                    System.out.println("Ticks remaining = " + remaining_ticks + ".");
                }
                remaining_ticks = executeLDO(instruction_fields, remaining_ticks, new_instruction_flag);
                if (remaining_ticks == 0) {
                    // TBD kluge cycle adjustment
                    allow_IR_set_flag--;
                    allow_IR_set_flag--;
                    //registers.incrementPC();
                }
                break;
            case 2:
            case 34:
                // STR STX
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Executing STR/X: R/X[" + general_purpose_register
                            + "] --> MEM[" + address_field + "].");
                    System.out.println("Ticks remaining = " + remaining_ticks + ".");
                }
                remaining_ticks = executeSTO(instruction_fields, remaining_ticks, new_instruction_flag);
                break;
            case 3:
                // LDA
                memory_words[0] = effective_address;
                try {
                    registers.setR(memory_words, general_purpose_register);
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Executing LDA: R[" + general_purpose_register
                                + "] <-- " + address_field + ".");
                    }
                } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                } finally {
                    remaining_ticks = 0;
                    new_instruction_flag = 0;
                }
                System.out.println("Ticks remaining = " + remaining_ticks + ".");
                break;
            default:
                remaining_ticks = 0;
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Opcode " + opcode + " is not yet implemented.");
                }
                break;
            // end opcode switch
        }
        if ((new_instruction_flag > 0) && (remaining_ticks > 0)) {
            // rewind the PC by two positions to account for the pipeline
            // i.e., by the time this instruction started, PC was two ahead of IR
            // PC --> MAR |--(-2)--> MBR --(-1)--> IR (now)
            registers.decrementPC();
            registers.decrementPC();
        }
        new_instruction_flag = 0;
    }

    private int executeLDO(int[] instruction_fields, int remaining_ticks, int new_instruction_flag) {
        // temporary store for values to/fro memory
        int[] memory_words = new int[1];
        // initialize
        int remaining_ticks_return = remaining_ticks;
        // compute number of clock cycles required to complete the operation
        if (new_instruction_flag > 0) {
            //first pass
            // compute the total clock cycles
            if (instruction_fields[3] == 0) {
                // direct addressing
                if (instruction_fields[2] == 0) {
                    // no indirection; no indexing
                    // R --> LRR --> MAR |--> MBR --> R
                    remaining_ticks_return = 4;
                    branch_id = 0;
                } else {
                    // no indirection; yes indexing
                    // R --> LRR --> LRR + idx --> MAR |--> MBR --> R
                    remaining_ticks_return = 5;
                    branch_id = 1;
                }
            } else {
                // indirect addressing
                if (instruction_fields[2] == 0) {
                    // yes indirection; no indexing
                    // R --> LRR --> MAR |--> MBR --> LRR --> MAR |--> MBR --> R
                    remaining_ticks_return = 7;
                    branch_id = 2;
                } else {
                    // yes indirection; yes indexing
                    // R --> LRR --> MAR |--> MBR --> LRR --> LRR + idx --> MAR |--> MBR --> R
                    remaining_ticks_return = 8;
                    branch_id = 3;
                }
            }
            //endif for new instruction flag
        }
        // now actually do stuff
        switch (branch_id) {
            case 0:
                // no indirection; no indexing
                if (remaining_ticks_return > 3) {
                    // IR(addy) --> LRR
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    // this takes an extra clock
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (instruction_fields[0] == 1) {
                        //OPCODE_LDR = 1;
                        try {
                            registers.setR(memory_words, instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 33) {
                        //OPCODE_LDX = 33;
                        try {
                            registers.setX(memory_words[0], instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                        // see 'finally' for corresponding: remaining_ticks_return--;
                    }
                }
                break;
            case 1:
                // no indirection; yes indexing
                if (remaining_ticks_return > 4) {
                    // IR(addy) --> LRR
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR/X had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 3) {
                    // LRR + idx --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR+idx = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    // this takes an extra clock
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (instruction_fields[0] == 1) {
                        //OPCODE_LDR = 1;
                        try {
                            registers.setR(memory_words, instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 33) {
                        //OPCODE_LDX = 33;
                        try {
                            registers.setX(memory_words[0], instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                        // see 'finally' for corresponding: remaining_ticks_return--;
                    }
                }
                break;
            case 2:
                // yes indirection; no indexing
                if (remaining_ticks_return > 6) {
                    // IR(addy) --> LRR
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 6) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 5) {
                    // MAR |--> MBR
                    registers.setLRR(next_MBR);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // MBR --> LRR
                    // this really just marks the extra clock time
                    registers.setLRR(next_MBR);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    memory_words[0] = next_MBR;
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (instruction_fields[0] == 1) {
                        //OPCODE_LDR = 1;
                        try {
                            registers.setR(memory_words, instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 33) {
                        //OPCODE_LDX = 33;
                        try {
                            registers.setX(memory_words[0], instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                        // see 'finally' for corresponding: remaining_ticks_return--;
                    }
                }
                break;
            case 3:
                // yes indirection; yes indexing
                if (remaining_ticks_return > 7) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 7) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 6) {
                    // MAR |--> MBR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    registers.setLRR(next_MBR);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 5) {
                    // MBR --> LRR
                    // this really just marks the extra clock time
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (MBR = " + next_MBR + ").");
                    }
                    registers.setLRR(next_MBR);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR/X had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 3) {
                    // LRR + idx --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR+idx = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    // TBD!! for some reason had to give it another clock
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    memory_words[0] = next_MBR;
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set R|X[] <-- (MBR = " + next_MBR + ").");
                    }
                    if (instruction_fields[0] == 1) {
                        //OPCODE_LDR = 1;
                        try {
                            registers.setR(memory_words, instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 33) {
                        //OPCODE_LDX = 33;
                        try {
                            registers.setX(memory_words[0], instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                        // see 'finally' for corresponding: remaining_ticks_return--;
                    }
                }
                break;
            default:
                remaining_ticks = 0;
                set_memory = 0;
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("LDR/X of type " + branch_id + " is not valid.");
                }
                break;
            // end branch switch
        }
        return remaining_ticks_return;
    }

    private int executeLDA(int[] instruction_fields, int remaining_ticks, int new_instruction_flag) {
        // temporary store for values to/fro memory
        int[] memory_words = new int[1];
        // initialize
        int remaining_ticks_return = remaining_ticks;
        // compute number of clock cycles required to complete the operation
        if (new_instruction_flag > 0) {
            //first pass
            // compute the total clock cycles
            if (instruction_fields[3] == 0) {
                // direct addressing
                if (instruction_fields[2] == 0) {
                    // no indirection; no indexing
                    // R --> R
                    remaining_ticks_return = 1;
                    branch_id = 0;
                } else {
                    // no indirection; yes indexing
                    // R --> LRR --> LRR + idx --> R
                    remaining_ticks_return = 3;
                    branch_id = 1;
                }
            } else {
                // indirect addressing
                if (instruction_fields[2] == 0) {
                    // yes indirection; no indexing
                    // R --> LRR --> MAR |--> MBR --> R
                    remaining_ticks_return = 4;
                    branch_id = 2;
                } else {
                    // yes indirection; yes indexing
                    // R --> LRR --> MAR |--> MBR --> LRR --> LRR + idx --> MAR |--> MBR --> R
                    remaining_ticks_return = 8;
                    branch_id = 3;
                }
            }
            //endif for new instruction flag
        }
        // now actually do stuff
        switch (branch_id) {
            case 0:
                // no indirection; no indexing
                if (remaining_ticks_return > 0) {
                    // IR(addy) --> R
                    memory_words[0] = instruction_fields[4];
                    try {
                        registers.setR(memory_words, instruction_fields[1]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                }
                break;
            case 1:
                // no indirection; yes indexing
                if (remaining_ticks_return > 2) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR/X had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 1) {
                    // LRR + idx --> R
                    memory_words[0] = instruction_fields[4];
                    try {
                        registers.setR(memory_words, instruction_fields[1]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                }
                break;
            case 2:
                // yes indirection; no indexing
                if (remaining_ticks_return > 3) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    registers.setLRR(next_MBR);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set R|X[] <-- (MBR = " + next_MBR + ").");
                    }
                    try {
                        registers.setR(memory_words, instruction_fields[1]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    // see 'finally' for corresponding: remaining_ticks_return--;
                }
                break;
            case 3:
                // yes indirection; yes indexing
                if (remaining_ticks_return > 7) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 7) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 6) {
                    // MAR |--> MBR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    registers.setLRR(next_MBR);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 5) {
                    // MBR --> LRR
                    // this really just marks the extra clock time
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (MBR = " + next_MBR + ").");
                    }
                    registers.setLRR(next_MBR);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("LDR/X had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 3) {
                    // LRR + idx --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR+idx = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // MAR |--> MBR
                    // TBD!! for some reason had to give it another clock
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    memory_words[0] = next_MBR;
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MBR --> R
                    memory_words[0] = next_MBR;
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set R|X[] <-- (MBR = " + next_MBR + ").");
                    }
                    if (instruction_fields[0] == 1) {
                        //OPCODE_LDR = 1;
                        try {
                            registers.setR(memory_words, instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 33) {
                        //OPCODE_LDX = 33;
                        try {
                            registers.setX(memory_words[0], instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("LDX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                        // see 'finally' for corresponding: remaining_ticks_return--;
                    }
                }
                break;
            default:
                remaining_ticks = 0;
                set_memory = 0;
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("LDA of type " + branch_id + " is not valid.");
                }
                break;
            // end branch switch
        }
        return remaining_ticks_return;
    }

    private int executeSTO(int[] instruction_fields, int remaining_ticks, int new_instruction_flag) {
        // temporary store for values to/fro memory
        int[] memory_words = new int[1];
        // initialize
        int remaining_ticks_return = remaining_ticks;
        // compute number of clock cycles required to complete the operation
        if (new_instruction_flag > 0) {
            //first pass
            // compute the total clock cycles
            if (instruction_fields[3] == 0) {
                // direct addressing
                if (instruction_fields[2] == 0) {
                    // no indirection; no indexing
                    // R --> LRR --> MAR |--> MBR
                    remaining_ticks_return = 3;
                    branch_id = 0;
                } else {
                    // no indirection; yes indexing
                    // R --> LRR --> LRR + idx --> MAR |--> MBR
                    remaining_ticks_return = 4;
                    branch_id = 1;
                }
            } else {
                // indirect addressing
                if (instruction_fields[2] == 0) {
                    // yes indirection; no indexing
                    // R --> LRR --> MAR |--> MBR --> LRR --> MAR |--> MBR
                    remaining_ticks_return = 6;
                    branch_id = 2;
                } else {
                    // yes indirection; yes indexing
                    // R --> LRR --> MAR |--> MBR --> LRR --> LRR + idx --> MAR |--> MBR
                    remaining_ticks_return = 7;
                    branch_id = 3;
                }
            }
            //endif for new instruction flag
        }
        // now actually do stuff
        switch (branch_id) {
            case 0:
                // no indirection; no indexing
                if (remaining_ticks_return > 2) {
                    // IR(addy) --> LRR
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MAR |--> MBR
                    if (instruction_fields[0] == 2) {
                        //OPCODE_STR = 2;
                        try {
                            registers.getR(memory_words, instruction_fields[1], 1);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 34) {
                        //OPCODE_STX = 34;
                        try {
                            memory_words[0] = registers.getX(instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    }
                    next_MBR = memory_words[0];
                    set_memory = 1;
                    // see 'finally' for corresponding: remaining_ticks_return--;
                }
                break;
            case 1:
                // no indirection; yes indexing
                if (remaining_ticks_return > 3) {
                    // IR(addy) --> LRR
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("STR/X had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 2) {
                    // LRR + idx --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR+idx = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MAR |--> MBR
                    if (instruction_fields[0] == 2) {
                        //OPCODE_STR = 2;
                        try {
                            registers.getR(memory_words, instruction_fields[1], 1);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 34) {
                        //OPCODE_STX = 34;
                        try {
                            memory_words[0] = registers.getX(instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    }
                    next_MBR = memory_words[0];
                    set_memory = 1;
                    // see 'finally' for corresponding: remaining_ticks_return--;
                }
                break;
            case 2:
                // yes indirection; no indexing
                if (remaining_ticks_return > 5) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 5) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // MAR |--> MBR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    memory_words[0] = next_MBR;
                    registers.setLRR(memory_words[0]);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // MBR --> LRR
                    // this really just marks the extra clock time
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (MBR = " + next_MBR + ").");
                    }
                    registers.setLRR(next_MBR);
                    //TBD prior - registers.setLRR(registers.getLRR());
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 2) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MAR |--> MBR
                    if (instruction_fields[0] == 2) {
                        //OPCODE_STR = 2;
                        try {
                            registers.getR(memory_words, instruction_fields[1], 1);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 34) {
                        //OPCODE_STX = 34;
                        try {
                            memory_words[0] = registers.getX(instruction_fields[2]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STX had problem accessing X[" + instruction_fields[2] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    }
                    next_MBR = memory_words[0];
                    set_memory = 1;
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set MBR <-- (R|X[] = " + next_MBR + ").");
                    }
                    // see 'finally' for corresponding: remaining_ticks_return--;
                }
                break;
            case 3:
                // yes indirection; yes indexing
                if (remaining_ticks_return > 6) {
                    // IR(addy) --> LRR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
                    }
                    registers.setLRR(instruction_fields[4]);
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 6) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 5) {
                    // MAR |--> MBR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Wait one cycle for Mem Ctrl Unit.");
                    }
                    memory_words[0] = next_MBR;
                    registers.setLRR(memory_words[0]);
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 4) {
                    // MBR --> LRR
                    // this really just marks the extra clock time
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set LRR <-- (MBR = " + next_MBR + ").");
                    }
                    registers.setLRR(next_MBR);
                    // TBD prior - registers.setLRR(registers.getLRR());
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 3) {
                    // LRR --> LRR + idx
                    try {
                        memory_words[0] = registers.getX(instruction_fields[2]);
                    } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                        if (MODULE_DEBUG_FLAG == 1) {
                            System.out.println("STR had problem accessing X[" + instruction_fields[2] + "].");
                            bound_error.printStackTrace();
                        }
                    } finally {
                        remaining_ticks_return--;
                    }
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = "
                                + memory_words[0] + " ) + " + registers.getLRR() + ".");
                    }
                    // add the index to the address already on the register
                    registers.setLRR(memory_words[0] + registers.getLRR());
                } else if (remaining_ticks_return == 2) {
                    // LRR --> MAR
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("(LRR = " + registers.getLRR() + ") --> MAR.");
                        System.out.println("MAR was " + registers.getMAR()
                                + ", with MBR of " + registers.getMBR() + ".");
                    }
                    next_MAR = registers.getLRR();
                    set_memory = 0;
                    remaining_ticks_return--;
                } else if (remaining_ticks_return == 1) {
                    // MAR |--> MBR
                    if (instruction_fields[0] == 2) {
                        //OPCODE_STR = 2;
                        try {
                            registers.getR(memory_words, instruction_fields[1], 1);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STR had problem accessing R[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    } else if (instruction_fields[0] == 34) {
                        //OPCODE_STX = 34;
                        try {
                            memory_words[0] = registers.getX(instruction_fields[1]);
                        } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                            if (MODULE_DEBUG_FLAG == 1) {
                                System.out.println("STX had problem accessing X[" + instruction_fields[1] + "].");
                                bound_error.printStackTrace();
                            }
                        } finally {
                            remaining_ticks_return--;
                        }
                    }
                    next_MBR = memory_words[0];
                    set_memory = 1;
                    if (MODULE_DEBUG_FLAG == 1) {
                        System.out.println("Set MBR <-- (R|X[] = " + next_MBR + ").");
                    }
                    // see 'finally' for corresponding: remaining_ticks_return--;
                }
                break;
            default:
                remaining_ticks = 0;
                set_memory = 0;
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("STR/X of type " + branch_id + " is not valid.");
                }
                break;
            // end branch switch
        }
        return remaining_ticks_return;
    }

    private int computeEffectiveAddress(int indirect_addressing, int index_register,
                                        int address_field, int memory_contents)
            throws projectexceptions.MemoryOutOfBounds {
        int effective_address = 0;
        if (indirect_addressing == 0) {
            try {
                // compute effective address for no indirect addressing; works regardless of indexing
                effective_address = address_field + registers.getX(index_register);
            } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Weirdness simply reading from X[" + index_register + "].");
                    bound_error.printStackTrace();
                }
                throw bound_error;
            }
        } else if (indirect_addressing == 1) {
            try {
                // compute effective address with indirect addressing; works regardless of indexing
                effective_address = memory_contents + registers.getX(index_register);
            } catch (projectexceptions.MemoryOutOfBounds bound_error) {
                if (MODULE_DEBUG_FLAG == 1) {
                    System.out.println("Weirdness simply reading from X[" + index_register
                            + "] during indirection.");
                    bound_error.printStackTrace();
                }
                throw bound_error;
            }
        }
        return effective_address;
    }

    // memory control unit
    public void taskMemoryControlUnit_get(Memory memory) throws projectexceptions.MemoryOutOfBounds {
        int error = 0;
        String exception_string = "No exception information.";
        int memory_size = memory.getSize();
        int memory_address_value;
        int[] memory_words = new int[1];
        // read intended address from register
        memory_address_value = registers.getMAR();
        if (memory_address_value >= memory_size) {
            // create exception message
            exception_string = "Memory out of bounds!!!\n"
                    + "Requested address = " + memory_address_value
                    + " but max available would be " + (memory_size - 1) + ".";
            // optionally display debug info
            if (MODULE_DEBUG_FLAG == 1) {
                System.out.println(exception_string);
            }
            // tag error flag
            error = 1;
        }
        if (error == 0) {
            // fetch from memory
            memory.getWords(memory_words, memory_address_value, 1);
            // this should need no further error checking
            registers.setMBR(memory_words[0]);
        } else {
            // dummy value
            registers.setMBR(0);
            // set fault register
            registers.setMFR(registers.FAULT_MEMORY_OVER);
            throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string);
        }
    }

    public void taskMemoryControlUnit_set(Memory memory) throws projectexceptions.MemoryOutOfBounds {
        int error = 0;
        String exception_string = "No exception information.";
        int memory_size = memory.getSize();
        int memory_address_value;
        int[] memory_words = new int[1];
        // read intended address from register
        memory_address_value = registers.getMAR();
        if (memory_address_value >= memory_size) {
            // create exception message
            exception_string = "Memory out of bounds!!!\n"
                    + "Requested address = " + memory_address_value
                    + " but max available would be " + (memory_size - 1) + ".";
            // optionally display debug info
            if (MODULE_DEBUG_FLAG == 1) {
                System.out.println(exception_string);
            }
            // tag error flag
            error = 1;
        }
        if (error == 0) {
            memory_words[0] = registers.getMBR();
            // put into memory
            memory.setWords(memory_words, memory_address_value);
            // this should need no further error checking
        } else {
            // set fault register
            registers.setMFR(registers.FAULT_MEMORY_OVER);
            throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string);
        }
    }
}
