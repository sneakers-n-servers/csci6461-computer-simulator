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

  private static final int IR_WAIT_CYCLES = 2;

  //VARIABLES
  // interpreted instruction
  int[] instruction_register_fields;
  // controller signals
  // >0 for hold; 0 for incrementing
  private static int increment_PC_flag;
  // 1 for set; 0 for get
  public static int set_memory;
  // temporary storage for MAR/MBR values
  public static int next_MAR;
  public static int next_MBR;
  
  private static int remaining_ticks;
  private static int branch_id;
  private static int decoded_IR_valid_flag;
  private static int IR_valid_flag;
  public static int pipeline_MAR_available;
  private int[] pipeline_IR_valid_flag;
  
  
  
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
    increment_PC_flag = 1;
    set_memory = 0;
    
    remaining_ticks = 0;
    branch_id = 0;
    IR_valid_flag = 0;
    decoded_IR_valid_flag = 0;

    pipeline_MAR_available = 1;
    // each element will shift to mark validity of MBR for IR
    pipeline_IR_valid_flag = new int[IR_WAIT_CYCLES + 1];
    
    
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
    //int[] current_instruction;
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
      try {
        instruction_register_fields = instruction_set.decodeInstruction(registers.getIR());
        decoded_IR_valid_flag = 1;
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Decoded IR: opcode = " + instruction_register_fields[0] 
            + ", corresponding to " 
            + instruction_set.opcode_mnemonics[instruction_register_fields[0]] 
            + " from instruction " + registers.getIR() + ".");
        }
      } catch(projectexceptions.IllegalOpcode opcode_error) {
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
    } else {
      decoded_IR_valid_flag = 0;
    }
    // fetch instruction; i.e., update IR
    // but first shift down the validity register, bringing in a 0
    for (int i = 0; i < IR_WAIT_CYCLES; i++) {
      pipeline_IR_valid_flag[i] = pipeline_IR_valid_flag[i + 1];
    }
    pipeline_IR_valid_flag[IR_WAIT_CYCLES] = 0;
    // now actually fetch if valid
    if (pipeline_IR_valid_flag[0] > 0) {
      registers.setIR(next_MBR);
      System.out.println("\n>>>> TBD - setIR (ticks, incpc)" + remaining_ticks + ", " + increment_PC_flag + ".");
      System.out.println("Pipeline IR Flags: " + Arrays.toString(pipeline_IR_valid_flag) + ".<<<");
      IR_valid_flag = 1;
    } else {
      System.out.println("\n>>>> TBD - not set (ticks, incpc)" + remaining_ticks + ", " + increment_PC_flag + ".");
      System.out.println("Pipeline IR Flags: " + Arrays.toString(pipeline_IR_valid_flag) + ".<<<");
      IR_valid_flag = 0;
    }
    // access memory; i.e., update MBR
    try {
      if (set_memory == 1) {
        registers.setMBR(next_MBR);
        taskMemoryControlUnit_set(memory);
      } else {
        taskMemoryControlUnit_get(memory);
        next_MBR = registers.getMBR();
      }
    } catch(projectexceptions.MemoryOutOfBounds bound_error) {
      System.out.println("Memory Fault Register = 0x" + Integer.toHexString(registers.getMFR()) + "."); 
      bound_error.printStackTrace();
    } finally {
      set_memory = 0;
    }
    // update MAR
    if (pipeline_MAR_available > 0) {
      // use PC
      next_MAR = registers.getPC();
      pipeline_IR_valid_flag[IR_WAIT_CYCLES] = pipeline_MAR_available;
    } else {
      // or other memory access
      next_MAR = next_MAR;
      pipeline_IR_valid_flag[IR_WAIT_CYCLES] = pipeline_MAR_available;
    }
    registers.setMAR(next_MAR);
    // update PC
    if (increment_PC_flag > 0) {
      // advance PC - TBD this will have to get more sophisticated for a jump
      registers.incrementPC();
    }
    // one clock cycle
    current_tick = registers.tick();
    if (MODULE_DEBUG_FLAG == 1) {
      System.out.println("\nEnding the clock cycle.");
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
    int indirect_flag = instruction_fields[3];
    int address_field = instruction_fields[4];
    // temporary store for values to/fro memory
    int[] memory_words = new int[1];
    // no indirection
    // compute address
    try {
      effective_address = computeEffectiveAddress(0, instruction_fields[2],
              instruction_fields[4], 0);
    } catch(projectexceptions.MemoryOutOfBounds bound_error) {
    }
    switch (opcode) {
      case 1:
        // LDR
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Executing LDR: R[" + instruction_fields[1]
                  + "] <-- MEM[~" + address_field + "].");
        }
        remaining_ticks = Processor_LDR.executeLDR(instruction_fields, remaining_ticks, this);
        if (remaining_ticks > 1) {
          pipeline_MAR_available = 0;
          increment_PC_flag = 0;
          flushPipeline_IR_valid_flag();
        } else if (remaining_ticks == 1) {
          // rewind the PC to account for the pipeline
          // i.e., by the time this instruction started, PC was ahead of IR
          // PC --> MAR |--(-2)--> MBR --(-1)--> IR (now)
          registers.decrementPC();
          registers.decrementPC();
          registers.decrementPC();
          // signal to the pipeline that MAR is usable to PC
          pipeline_MAR_available = 1;
          increment_PC_flag = 1;
        }
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Ticks remaining = " + remaining_ticks + ".");
        }
        break;
      case 33:
        // LDX
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Executing LDX: X[" + instruction_fields[2]
                  + "] <-- MEM[~" + address_field + "].");
        }
        remaining_ticks = Processor_LDX.executeLDX(instruction_fields, remaining_ticks, this);
        if (remaining_ticks > 1) {
          pipeline_MAR_available = 0;
          increment_PC_flag = 0;
          flushPipeline_IR_valid_flag();
        } else if (remaining_ticks == 1) {
          // rewind the PC to account for the pipeline
          // i.e., by the time this instruction started, PC was ahead of IR
          // PC --> MAR |--(-2)--> MBR --(-1)--> IR (now)
          registers.decrementPC();
          registers.decrementPC();
          registers.decrementPC();
          // signal to the pipeline that MAR is usable to PC
          pipeline_MAR_available = 1;
          increment_PC_flag = 1;
        }
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Ticks remaining = " + remaining_ticks + ".");
        }
        break;
      case 2:
        // STR
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Executing STR: R[" + general_purpose_register
                  + "] --> MEM[~" + address_field + "].");
        }
        remaining_ticks = Processor_STR.executeSTR(instruction_fields, remaining_ticks, this);
        if (remaining_ticks > 1) {
          pipeline_MAR_available = 0;
          increment_PC_flag = 0;
          flushPipeline_IR_valid_flag();
        } else if (remaining_ticks <= 0) {
          // rewind the PC to account for the pipeline
          // i.e., by the time this instruction started, PC was ahead of IR
          // PC --> MAR |--(-2)--> MBR --(-1)--> IR (now)
          registers.decrementPC();
          registers.decrementPC();
          registers.decrementPC();
          // signal to the pipeline that MAR is usable to PC
          pipeline_MAR_available = 1;
          increment_PC_flag = 1;
        }
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Ticks remaining = " + remaining_ticks + ".");
        }
        break;
      case 34:
        // STX
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Executing STX: X[" + general_purpose_register
                  + "] --> MEM[~" + address_field + "].");
        }
        remaining_ticks = Processor_STX.executeSTX(instruction_fields, remaining_ticks, this);
        if (remaining_ticks > 1) {
          pipeline_MAR_available = 0;
          increment_PC_flag = 0;
          flushPipeline_IR_valid_flag();
        } else if (remaining_ticks <= 0) {
          // rewind the PC to account for the pipeline
          // i.e., by the time this instruction started, PC was ahead of IR
          // PC --> MAR |--(-2)--> MBR --(-1)--> IR (now)
          registers.decrementPC();
          registers.decrementPC();
          registers.decrementPC();
          // signal to the pipeline that MAR is usable to PC
          pipeline_MAR_available = 1;
          increment_PC_flag = 1;
        }
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Ticks remaining = " + remaining_ticks + ".");
        }
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
        } catch(projectexceptions.MemoryOutOfBounds bound_error) {
        } finally {
          remaining_ticks = 0;
          pipeline_MAR_available = 1;
          increment_PC_flag = 1;
        }
        System.out.println("Ticks remaining = " + remaining_ticks + ".");
        break;
      default:
        remaining_ticks = 0;
        pipeline_MAR_available = 1;
        increment_PC_flag = 1;
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Opcode " + opcode + " is not yet implemented.");
        }
        break;
      // end opcode switch
    }
  }

  private int computeEffectiveAddress(int indirect_addressing, int index_register, 
                                      int address_field, int memory_contents) 
    throws projectexceptions.MemoryOutOfBounds {
    int effective_address = 0;
    if (indirect_addressing == 0) {
      try {
        // compute effective address for no indirect addressing; works regardless of indexing
        effective_address = address_field + registers.getX(index_register);
      } catch(projectexceptions.MemoryOutOfBounds bound_error) {
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
      } catch(projectexceptions.MemoryOutOfBounds bound_error) {
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

  // manage pipeline_IR_valid_flag
  public void flushPipeline_IR_valid_flag() {
    for (int i = 0; i < pipeline_IR_valid_flag.length; i++){
      pipeline_IR_valid_flag[i] = 0;
    }
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
