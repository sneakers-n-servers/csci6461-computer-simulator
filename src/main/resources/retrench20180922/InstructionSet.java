/*
Project 1 - Instruction Set

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180920
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180920
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
public class InstructionSet {
  //CONSTANTS
  private static final int INSTRUCTION_SIZE_INBITS = 16;
  private static final int OPCODE_SIZE_INBITS = 6;
  // TBD - combine with a later instruction class; 39d --> opcode 39936
  public static final int OPCODE_NOP = 39;
  public static final String OPCODE_MNEMONIC_NOP = "NOP";
  public static final int NOP_VALUE = 39936;
  // debug constants
  private static final int MODULE_DEBUG_FLAG = 0;

  //VARIABLES
  // declare convenience-listing
  public String[] opcode_mnemonics;
  // declare a dummy/template instance of each type
  public InstructionLoadStore dummy_instruction_ldst;

  //CONSTRUCTORS
  public InstructionSet() {
    // compare to a byte, just in case somebody got too creative with the defaults
    if (OPCODE_SIZE_INBITS > 8) {
      System.out.println("TBD too many opcodes; need to raise an exception: " + OPCODE_SIZE_INBITS + ".");
      System.exit(-1);
    }
    // instantiate a dummy/template instance of each opcode
    try {
      InstructionLoadStore dummy_instruction_ldst = new InstructionLoadStore();
    } catch(projectexceptions.IllegalOpcode opcode_error) {
      opcode_error.printStackTrace();
      System.out.println("Couldn't even create a dummy " + "InstructionLDA" + "!!!");
      System.exit(-1);
    }
    // initialize convenience-listing
    //   and populate implemented codes - TBD clean this up maybe with a key-value pair
    opcode_mnemonics = new String[(int)Math.pow(2, OPCODE_SIZE_INBITS)];
    // TBD - this is a cheat
    opcode_mnemonics[OPCODE_NOP] = OPCODE_MNEMONIC_NOP;
    opcode_mnemonics[dummy_instruction_ldst.OPCODE_LDR] = dummy_instruction_ldst.OPCODE_MNEMONIC_LDR;
    opcode_mnemonics[dummy_instruction_ldst.OPCODE_STR] = dummy_instruction_ldst.OPCODE_MNEMONIC_STR;
    opcode_mnemonics[dummy_instruction_ldst.OPCODE_LDA] = dummy_instruction_ldst.OPCODE_MNEMONIC_LDA;
    opcode_mnemonics[dummy_instruction_ldst.OPCODE_LDX] = dummy_instruction_ldst.OPCODE_MNEMONIC_LDX;
    opcode_mnemonics[dummy_instruction_ldst.OPCODE_STX] = dummy_instruction_ldst.OPCODE_MNEMONIC_STX;
    if (MODULE_DEBUG_FLAG == 1) {
      System.out.println("Opcodes: " + Arrays.toString(opcode_mnemonics) + ".");
    }
  }

  // decode an instruction word into its fields
  public int[] decodeInstruction(int instruction) throws projectexceptions.IllegalOpcode {
    //VARIABLES
    int[] returned_instruction = {InstructionSet.NOP_VALUE};
    int error = 0;
    String exception_string = "No exception information.";
    // TBD - update with proper class methods for NOP
    if (instruction == InstructionSet.NOP_VALUE) {
      returned_instruction[0] = InstructionSet.OPCODE_NOP;
      // optionally display debug info
      if (MODULE_DEBUG_FLAG == 1) {
        System.out.println("Instruction is a " + InstructionSet.OPCODE_MNEMONIC_NOP + ".");
      }
    } else {
      // try LDST family
      try {
        dummy_instruction_ldst = new InstructionLoadStore(instruction);
        returned_instruction = new int[5];
        returned_instruction[0] = dummy_instruction_ldst.opcode;
        returned_instruction[1] = dummy_instruction_ldst.general_purpose_register;
        returned_instruction[2] = dummy_instruction_ldst.index_register;
        returned_instruction[3] = dummy_instruction_ldst.indirect_flag;
        returned_instruction[4] = dummy_instruction_ldst.address;
      } catch(projectexceptions.IllegalOpcode opcode_error) {
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("Not an LDST either so instruction not found.");
        }
        throw opcode_error;
      }
    }
    return returned_instruction;
  }

  // encode an instruction word from its fields
  public int encodeInstruction(int[] instruction_fields) throws projectexceptions.IllegalOpcode {
    //VARIABLES
    int returned_instruction = 0;
    int error = 0;
    String exception_string = "No exception information.";
    try {
      dummy_instruction_ldst = new InstructionLoadStore(instruction_fields);
      returned_instruction = dummy_instruction_ldst.instruction_word;
    } catch(projectexceptions.IllegalOpcode opcode_error) {
      // optionally display debug info
      if (MODULE_DEBUG_FLAG == 1) {
        System.out.println("Not an LDA.");
      }
      throw opcode_error;
    }
    return returned_instruction;
  }

  //CLASSES for instruction types
  /** Classes for load/store instructions; i.e., LD# and ST# */
  private class InstructionLoadStore {
    //CONSTANTS
    // TBD - eventually replace this with an Enum or a Map
    private static final int OPCODE_LDR = 1;
    private static final String OPCODE_MNEMONIC_LDR = "LDR";
    private static final int OPCODE_STR = 2;
    private static final String OPCODE_MNEMONIC_STR = "STR";
    private static final int OPCODE_LDA = 3;
    private static final String OPCODE_MNEMONIC_LDA = "LDA";
    private static final int OPCODE_LDX = 33;
    private static final String OPCODE_MNEMONIC_LDX = "LDX";
    private static final int OPCODE_STX = 34;
    private static final String OPCODE_MNEMONIC_STX = "STX";
    private final int[] OPCODES_LDST = {OPCODE_LDR, 
                                          OPCODE_STR, 
                                          OPCODE_LDA, 
                                          OPCODE_LDX, 
                                          OPCODE_STX};
    private String[] OPCODE_MNEMONICS_LDST = {OPCODE_MNEMONIC_LDR, 
                                              OPCODE_MNEMONIC_STR, 
                                              OPCODE_MNEMONIC_LDA, 
                                              OPCODE_MNEMONIC_LDX, 
                                              OPCODE_MNEMONIC_STX};

    // size of each element in the instruction
    // listed in order from MSB to LSB
    // note that this will be big endian
    private static final int OPCODE_SIZE_INBITS = 6;
    private static final int GPR_SPEC_SIZE_INBITS = 2;
    private static final int IX_SPEC_SIZE_INBITS = 2;
    private static final int INDIRECT_SPEC_SIZE_INBITS = 1;
    private static final int ADDRESS_SIZE_INBITS = 5;
    // the code is based on this assumption of order
    private final int[] INSTRUCTION_FIELD_SIZE_ORDERED = {
      OPCODE_SIZE_INBITS, 
      GPR_SPEC_SIZE_INBITS, 
      IX_SPEC_SIZE_INBITS, 
      INDIRECT_SPEC_SIZE_INBITS, 
      ADDRESS_SIZE_INBITS
    };

    //VARIABLES
    private int instruction_word;
    private int opcode;
    private int general_purpose_register;
    private int index_register;
    private int indirect_flag;
    private int address;

    //CONSTRUCTORS
    // constructor for "template" instruction
    public InstructionLoadStore() throws projectexceptions.IllegalOpcode {
      String exception_string = "No exception information.";
      // simple check that the definition is not ludicrous
      int total_instruction_size = 0;
      for (int i = 0; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        total_instruction_size += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      if (total_instruction_size != InstructionSet.INSTRUCTION_SIZE_INBITS) {
        // create exception message
        exception_string = "Illegal opcode!!!\n"
          + "The definition itself is problematic.";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // now proceed with construction
      instruction_word = 3072;
      opcode = OPCODE_LDA;
      general_purpose_register = 0;
      index_register = 0;
      indirect_flag = 0;
      address = 0;
    }
    // constructor for interpreting an instruction
    public InstructionLoadStore(int instruction) 
        throws projectexceptions.IllegalOpcode {
      //debug
      int LOCAL_DEBUG = 0;
      //VARIABLES
      int error = 0;
      String exception_string = "No exception information.";
      int field_value;
      int field_index;
      int field_shift;
      int field_mask;
      //BEGIN
      // isolate opcode to check for error
      field_index = 0;
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value = instruction >>> field_shift;
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      if ((MODULE_DEBUG_FLAG & LOCAL_DEBUG) == 1) {
        System.out.println("Index: " + field_index 
          + "; Shift = " + field_shift
          + "; Mask = " + field_mask
          + "\nValue = " + field_value 
          + "; Original = " + instruction + ".");
      }
      opcode = field_value;
      // check that the opcode is valid
      for (int i = 0; i < OPCODES_LDST.length; i++) {
        if (opcode == OPCODES_LDST[i]) {
          // found opcode :-)
          error = 0;
          break;
        } else {
          error++;
        }
      }
      if (error > 0) {
        // create exception message
        exception_string = "Assumed the wrong opcode!\n"
          + "Computed opcode is " + opcode + ";"
          + " but attemped to interpret it as a LD#/ST# type." 
          + "\nInstruction is " + instruction + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // isolate register and check for error
      field_index = 1;
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value = instruction >>> field_shift;
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      general_purpose_register = field_value;
      if (general_purpose_register < 0) {
        // create exception message
        exception_string = "Wrong index for general purpose register!\n"
          + "Computed index is " + general_purpose_register + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // isolate index register and check for error
      field_index = 2;
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value = instruction >>> field_shift;
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      index_register = field_value;
      if (index_register < 0) {
        // create exception message
        exception_string = "Wrong index for index register!\n"
          + "Computed index is " + index_register + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // isolate indirection flag and check for error
      field_index = 3;
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value = instruction >>> field_shift;
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      indirect_flag = field_value;
      if ((indirect_flag < 0) || (indirect_flag > 1)) {
        // create exception message
        exception_string = "Invalid value for indirection flag!\n"
          + "Computed value is " + indirect_flag + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // isolate address and check for error
      field_index = 4;
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value = instruction >>> field_shift;
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      address = field_value;
      if (address < 0) {
        // create exception message
        exception_string = "Memory out of bounds!\n"
          + "Computed address is " + address + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
    }
    // constructor for creating an instruction
    public InstructionLoadStore(int[] instruction_fields) 
        throws projectexceptions.IllegalOpcode {
      //debug
      int LOCAL_DEBUG = 1;
      //VARIABLES
      int error = 0;
      String exception_string = "No exception information.";
      int field_value;
      int field_index;
      int field_shift;
      int field_mask;
      int opcode;
      //BEGIN
      // initialize output
      instruction_word = 0;
      // check for odd number of fields
      if (instruction_fields.length != INSTRUCTION_FIELD_SIZE_ORDERED.length) {
        // create exception message
        exception_string = "Invalid number of fields!\n"
          + "Opcode is " + instruction_fields[0] + ";"
          + " expected " + INSTRUCTION_FIELD_SIZE_ORDERED.length + " fields."
          + " received " + instruction_fields.length + " fields.";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      // check for error and bundle opcode
      field_index = 0;
      field_value = instruction_fields[field_index];
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      opcode = field_value;
      // check that the opcode is valid
      for (int i = 0; i < OPCODES_LDST.length; i++) {
        if (opcode == OPCODES_LDST[i]) {
          // found opcode :-)
          error = 0;
          break;
        } else {
          error++;
        }
      }
      if (error > 0) {
        // create exception message
        exception_string = "Assumed the wrong opcode!\n"
          + "Computed opcode is " + opcode + ";"
          + " but attemped to interpret it as a LD#/ST# type.";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value <<= field_shift;
      instruction_word += field_value;
      // check for error and bundle register
      field_index = 1;
      field_value = instruction_fields[field_index];
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      general_purpose_register = field_value;
      if (general_purpose_register < 0) {
        // create exception message
        exception_string = "Wrong index for general purpose register!\n"
          + "Computed index is " + general_purpose_register + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value <<= field_shift;
      instruction_word += field_value;
      // check for error and bundle index register
      field_index = 2;
      field_value = instruction_fields[field_index];
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      index_register = field_value;
      if (index_register < 0) {
        // create exception message
        exception_string = "Wrong index for index register!\n"
          + "Computed index is " + index_register + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value <<= field_shift;
      instruction_word += field_value;
      // check for error and bundle indirection flag
      field_index = 3;
      field_value = instruction_fields[field_index];
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      indirect_flag = field_value;
      if ((indirect_flag < 0) || (indirect_flag > 1)) {
        // create exception message
        exception_string = "Invalid value for indirection flag!\n"
          + "Computed value is " + indirect_flag + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value <<= field_shift;
      instruction_word += field_value;
      // check for error and bundle address
      field_index = 4;
      field_value = instruction_fields[field_index];
      // apply mask
      field_mask = (int)Math.pow(2, INSTRUCTION_FIELD_SIZE_ORDERED[field_index]) - 1;
      field_value &= field_mask;
      address = field_value;
      if (address < 0) {
        // create exception message
        exception_string = "Memory out of bounds!\n"
          + "Computed address is " + address + ".";
        // optionally display debug info
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println(exception_string);
        }
        // tag error flag
        error = 1;
        throw new projectexceptions.IllegalOpcode("\n" + exception_string);
      }
      field_shift = 0;
      for (int i = field_index + 1; i < INSTRUCTION_FIELD_SIZE_ORDERED.length; i++) {
        field_shift += INSTRUCTION_FIELD_SIZE_ORDERED[i];
      }
      field_value <<= field_shift;
      instruction_word += field_value;
    }
  }
}

