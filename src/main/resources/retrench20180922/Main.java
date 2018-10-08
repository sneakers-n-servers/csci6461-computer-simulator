/*
Project 1 - Main entry

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180920
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180920
        Modified:   
    -Too many changes to keep track of

%%%     20180916
        Author: Group 9    
        Project:    CSCI_6461_F18_Project1
    -Initial release

%%%%%%%%%%
*/

//IMPORT
//import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;
import projectexceptions.*;
import java.util.Arrays;

//BEGIN
public class Main {
  //CONSTANTS
  // TBD - change to 2048
  private static final int MEMORY_SIZE = 64;
  // debug constants
  private static final int MODULE_DEBUG_FLAG = 1;
  private static final int CLOCK_STEPS = 12;


  // professor's program
  public static final int PROFESSOR_PROGRAM_20180919_START_ADDRESS = 6;
  public static final int[] PROFESSOR_PROGRAM_20180919 = {  39936,
                                                              3097,
                                                              2068,
                                                              3075,
                                                              2078,
                                                              1310,
                                                              33886,
                                                              3869,
                                                              2902,
                                                              1588,
                                                              2673,
                                                              34932,
                                                              34885};

  //VARIABLES
  private Memory memory;

  //BEGIN
  public static void main(String[] args) {
    // temporary storage to display memory
    int[] memory_display = new int[MEMORY_SIZE];
    // create memory
    Memory memory = new Memory(MEMORY_SIZE);
    // prepare processor
    Processor sim_computer = new Processor(memory);

    // prepare program
    int program_length_inwords = PROFESSOR_PROGRAM_20180919.length;
    int program_start_address = PROFESSOR_PROGRAM_20180919_START_ADDRESS;
    int[] program = new int[program_length_inwords];
    System.arraycopy(PROFESSOR_PROGRAM_20180919, 0, program, 0, program_length_inwords);
    // load program into memory
    try {
      memory.setWords(program, program_start_address);
    } catch(projectexceptions.MemoryOutOfBounds bound_error) {
      bound_error.printStackTrace();
    }
    
    // demo
    sim_computer.registers.setPC(6);
    for (int i = 0; i < CLOCK_STEPS; i++) {
      /*
      if (MODULE_DEBUG_FLAG == 1) {
        if (sim_computer.registers.getPC() == program_start_address) {
          System.out.println(">>>  IR better be good 3 cycles hence.");
        } else if (sim_computer.registers.getPC() == program_start_address + 3) {
          System.out.println(">>>  IR better be good now.");
        }
      }
      */
      try {
       sim_computer.step();
        if (MODULE_DEBUG_FLAG == 1) {
          try {
            memory.getWords(memory_display, 0, memory_display.length);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
          }
          System.out.println("Memory: {" 
            + Arrays.toString(memory_display) + "}");
        }
      } catch(projectexceptions.IllegalOpcode opcode_error) {
        opcode_error.printStackTrace();
        // reset MFR
        sim_computer.registers.setMFR(0);
      }
    }
    // test program generation
    int[] generated_instruction = {2, 0, 2, 0, 25};
    int[][] generated_program = { {3, 0, 0, 0, 25}, 
                                  {2, 0, 0, 0, 20}, 
                                  {3, 0, 0, 0, 3}, 
                                  {2, 0, 0, 0, 30}, 
                                  {33, 0, 1, 0, 30}, 
                                  {3, 3, 0, 0, 29}, 
                                  {2, 3, 1, 0, 22}, 
                                  {1, 2, 0, 1, 20}, 
                                  {2, 2, 1, 1, 17}, 
                                  {34, 0, 1, 1, 20}, 
                                  {34, 0, 1, 0, 5}
                                };
    System.out.println("\n\n\n#####     #####");
    // single instruction
    try {
      System.out.print("Encoded Instruction = ");
      System.out.println(sim_computer.instruction_set.encodeInstruction(generated_instruction));
    } catch(projectexceptions.IllegalOpcode opcode_error) {
      System.out.println("Oops! Something went wrong generating the single instruction.");
      System.out.println("Remember that it can only generate Load/Store instructions currently.");
      opcode_error.printStackTrace();
    }
    // multiple instructions in program format
    try {
      System.out.print("\n\nEncoded Program = { ");
      for (int i = 0; i < generated_program.length; i++) {
        System.out.print(sim_computer.instruction_set.encodeInstruction(generated_program[i]));
        if (i < (generated_program.length - 1)) {
           System.out.println(", ");
        } else {
          System.out.println("\n};");
        }
      }
    } catch(projectexceptions.IllegalOpcode opcode_error) {
      System.out.println("Oops! Something went wrong generating the single instruction.");
      System.out.println("Remember that it can only generate Load/Store instructions currently.");
      opcode_error.printStackTrace();
    }
  }
}

