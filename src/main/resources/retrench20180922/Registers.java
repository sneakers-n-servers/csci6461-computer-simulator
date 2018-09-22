/*
Project 1 - CPU Registers

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180920
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180921
        Modified:   
    -Changed short to int

%%%     20180920
        Modified:   
    -Too many changes to keep track of

%%%     20180916
        Author: Group 9    
        Project:    CSCI_6461_F18_Project1
    -Initial release

%%%%%%%%%%
*/

package crap;


//IMPORT
import edu.gw.csci.simulator.exceptions.*;


//BEGIN
public class Registers {
  //CONSTANTS
  public static final int FAULT_MEMORY_OVER = 0x8;
  public static final int FAULT_ILLEGAL_OPCODE = 0x4;
  private static final int DEFAULT_PC_SIZE_INBITS = 12;
  private static final int DEFAULT_MEMORY_WORD_SIZE_INBITS = 16;
  private static final int DEFAULT_MEMORY_MASK = (int)Math.pow(2, DEFAULT_MEMORY_WORD_SIZE_INBITS) - 1;
  private static final int DEFAULT_GPR_WORDS = 4;
  // one more just to keep the indexing easy
  private static final int DEFAULT_IX_WORDS = 4;
  // debug constants
  private static final int MODULE_DEBUG_FLAG = 0;

  //VARIABLES
  // tick clock
  private long clock_value;
  // Logic Result Register (16 bits, since it's a buffer for GPR)
  private int LRR;
  // General Purpose Registers (16 bits)
  public int[] R;
  // Index Registers (IX) (16 bits)
  public int[] X;
  // Program Counter (12 bits)
  private int PC;
  // Condition Code (4 bits)
  private int CC;
  // Instruction Register (16 bits)
  private int IR;
  // Memory Address Register (16 bits)
  private int MAR;
  // Memory Buffer Register (16 bits)
  private int MBR;
  // Machine Status Register (16 bits)
  private int MSR;
  // Machine Fault Register (4 bits)
  private int MFR;

  //CONSTRUCTORS
  public Registers() {
    clock_value = 0;
    LRR = 0;
    R = new int[DEFAULT_GPR_WORDS];
    X = new int[DEFAULT_IX_WORDS];
    PC = 0;
    CC = 0;
    IR = 0;
    MAR = 0;
    MBR = 0;
    MSR = 0;
    MFR = 0;
    if (MODULE_DEBUG_FLAG == 1) {
      System.out.println("Registers cleared.");
    }
  }

  //ACCESS
  // clock
  public long tick() {
    clock_value++;
    return clock_value;
  }

  // IX
  // get values from the index registers IX
  public int getX(int first_index) throws projectexceptions.MemoryOutOfBounds {
    int error = 0;
    String exception_string = "No exception information.";
    if (first_index >= X.length) {
      // create exception message
      exception_string = "Memory out of bounds!!!\n"
        + "Attempting to address index register X[" + first_index + "]."
        + "Valid values are X[1] through X[" + (X.length - 1) + "].";
      // optionally display debug info
      if (MODULE_DEBUG_FLAG == 1) {
        System.out.println(exception_string);
      }
      // tag error flag
      error = 1;
    }
    if (error == 1) {
      throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string);
    }
    return X[first_index];
  }
  // put values into the index registers IX
  public void setX(int source, int first_index) throws projectexceptions.MemoryOutOfBounds {
    //VARIABLES
    int error = 0;
    String exception_string = "No exception information.";
    // take the opportunity to keep the dummy register clean, regardless of error
    X[0] = 0;
    // check for bound error
    if ((first_index == 0) || (first_index >= X.length)) {
      // create exception message
      exception_string = "Memory out of bounds!!!\n"
        + "Attempting to address index register X[" + first_index + "]."
        + "Valid values are X[1] through X[" + (X.length - 1) + "].";
      // optionally display debug info
      if (MODULE_DEBUG_FLAG == 1) {
        System.out.println(exception_string);
      }
      // tag error flag
      error = 1;
    }
    if (error == 1) {
      throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string);
    } else {
      // all good... set the new value
      X[first_index] = (source & DEFAULT_MEMORY_MASK);
    }
  }

  // GPR
  // get values from the GPR
  public void getR(int[] destination, int first_index, int number_of_words) 
    throws projectexceptions.MemoryOutOfBounds{
    //VARIABLES
    int error = 0;
    String exception_string = "No exception information.";
    // check for index errors
    if ((first_index < 0) || (number_of_words < 0) || (first_index + number_of_words > R.length)) {
      // create exception message
      exception_string = "Memory out of bounds!!!\n"
        + "(First index = " + first_index
        + ") + (Number of words = " + number_of_words + ") assumes " + (first_index + number_of_words)
        + " word(s) but max available would be " + (R.length - first_index) 
        + " word(s) from that index.";
      // optionally display debug info
      if (MODULE_DEBUG_FLAG == 1) {
        System.out.println(exception_string);
      }
      // tag error flag
      error = 1;
    }
    // handle the error case in which at least the first index is valid
    if (error == 1) {
      if ((first_index >= 0) && (first_index < R.length)) {
        // modify the upper bound
        number_of_words = R.length - first_index;
      } else {
        // even first index is bad so just quit
        throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string);
      }
    }
    // finally... now actually get the data
    try {
      System.arraycopy(R, first_index, destination, 0, number_of_words);
    } catch(ArrayIndexOutOfBoundsException bound_error) {
      // in case there was a weird out of bounds
      throw new projectexceptions.MemoryOutOfBounds("\n\nSort of MemoryOutOfBounds but we missed it\n\n.");
    }
    // don't forget to throw exception if only some data were copied
    if (error == 1) {
      throw new projectexceptions.MemoryOutOfBounds("\n" + exception_string 
        + "\nCopied " + number_of_words + " word(s) before throw.");
    }
  }
  // put values into the GPR
  public void setR(int[] source, int first_index) throws projectexceptions.MemoryOutOfBounds {
    int number_of_words = source.length;
    System.arraycopy(source, 0, R, first_index, number_of_words);
  }
  
  // MFR
  public void setMFR(int fault_code) {
    // mask the code to 4 bits
    fault_code &= 0x000F;
    MFR |= fault_code;
  }
  public int getMFR() {
    return MFR;
  }
  // MAR
  public void setMAR(int address_to_fetch) {
    MAR = address_to_fetch & DEFAULT_MEMORY_MASK;
  }
  public int getMAR() {
    return MAR;
  }
  // MBR
  public void setMBR(int word) {
    MBR = word & DEFAULT_MEMORY_MASK;
  }
  public int getMBR() {
    return MBR;
  }
  // LRR
  public void setLRR(int word) {
    LRR = word & DEFAULT_MEMORY_MASK;
  }
  public int getLRR() {
    return LRR;
  }
  public void setPC(int address_of_next_instruction) {
    //TBD might need some bound checking here
    // copy "raw" value
    PC = address_of_next_instruction;
    // apply mask
    PC &= ((int)Math.pow(2, DEFAULT_PC_SIZE_INBITS) - 1);
  }
  public void incrementPC() {
    //TBD implement a modulo here and probably bound-check
    PC += 1;
    // apply mask
    PC &= ((int)Math.pow(2, DEFAULT_PC_SIZE_INBITS) - 1);
  }
  public void decrementPC() {
    //TBD implement a modulo here and probably bound-check
    if (PC >=1 ) {
      PC -= 1;
    }
    // apply mask
    PC &= ((int)Math.pow(2, DEFAULT_PC_SIZE_INBITS) - 1);
  }
  public int getPC() {
    return PC;
  }
  public void setIR(int current_instruction) {
    IR = current_instruction;
  }
  public int getIR() {
    return IR;
  }
}

