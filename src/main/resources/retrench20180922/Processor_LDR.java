/*
Project 1 - Processing element for LDR

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20181007
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20181007
        Modified:   
    -Separated this into its own file.

%%%     20181007
        Author: Group 9    
        Project:    CSCI_6461_F18_Project2
    -Initial release

%%%%%%%%%%
*/

public class Processor_LDR {
  //CONSTANTS
  // debug constants
  private static final int MODULE_DEBUG_FLAG = 1;

  public static int executeLDR(int[] instruction_fields, int remaining_ticks, Processor processor) {
    int branch_id = -1;
    // temporary store for values to/fro memory
    int[] memory_words = new int[1];
    // initialize
    int remaining_ticks_return = remaining_ticks;
    // determine the type of addressing scheme in order to branch
    if (instruction_fields[3] == 0) {
      // direct addressing
      if (instruction_fields[2] == 0) {
        // no indirection; no indexing
        // R --> LRR --> MAR |--> MBR --> R
        branch_id = 0;
      } else {
        // no indirection; yes indexing
        // R --> LRR --> LRR + idx --> MAR |--> MBR --> R
        branch_id = 1;
      }
    } else {
      // indirect addressing
      if (instruction_fields[2] == 0) {
        // yes indirection; no indexing
        // R --> LRR --> MAR |--> MBR --> LRR --> MAR |--> MBR --> R
        branch_id = 2;
      } else {
        // yes indirection; yes indexing
        // R --> LRR --> MAR |--> MBR --> LRR --> LRR + idx --> MAR |--> MBR --> R
        branch_id = 3;
      }
    }
    // now actually do stuff
    switch (branch_id) {
      case 0:
        // no indirection; no indexing
        // R --> LRR --> MAR |--> MBR --> R
        if (remaining_ticks_return <= 0) {
          // first pass so specify number of clock cycles required to complete the operation
          remaining_ticks_return = 4;
        }
        if (remaining_ticks_return > 3) {
          // IR(addy) --> LRR
          processor.registers.setLRR(instruction_fields[4]);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 3) {
          // LRR --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 2) {
          // MAR |--> MBR
          // this takes an extra clock
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 1) {
          // MBR --> R
          memory_words[0] = processor.next_MBR;
          try {
            processor.registers.setR(memory_words, instruction_fields[1]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
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
        // R --> LRR --> LRR + idx --> MAR |--> MBR --> R
        if (remaining_ticks_return <= 0) {
          // first pass so specify number of clock cycles required to complete the operation
          remaining_ticks_return = 5;
        }
        if (remaining_ticks_return > 4) {
          // IR(addy) --> LRR
          processor.registers.setLRR(instruction_fields[4]);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 4) {
          // LRR --> LRR + idx
          try {
            memory_words[0] = processor.registers.getX(instruction_fields[2]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
            if (MODULE_DEBUG_FLAG == 1) {
              System.out.println("LDR/X had problem accessing X[" + instruction_fields[2] + "].");
              bound_error.printStackTrace();
            }
          } finally {
            remaining_ticks_return--;
          }
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = " 
              + memory_words[0] + " ) + " + processor.registers.getLRR() + "");
          }
          // add the index to the address already on the register
          processor.registers.setLRR(memory_words[0] + processor.registers.getLRR());
        } else if (remaining_ticks_return == 3) {
          // LRR + idx --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR+idx = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 2) {
          // MAR |--> MBR
          // this takes an extra clock
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 1) {
          // MBR --> R
          memory_words[0] = processor.next_MBR;
          try {
            processor.registers.setR(memory_words, instruction_fields[1]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
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
        // R --> LRR --> MAR |--> MBR --> LRR --> MAR |--> MBR --> R
        if (remaining_ticks_return <= 0) {
          // first pass so specify number of clock cycles required to complete the operation
          remaining_ticks_return = 7;
        }
        if (remaining_ticks_return > 6) {
          // IR(addy) --> LRR
          processor.registers.setLRR(instruction_fields[4]);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 6) {
          // LRR --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 5) {
          // MAR |--> MBR
          processor.registers.setLRR(processor.next_MBR);
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 4) {
          // MBR --> LRR
          // this really just marks the extra clock time
          processor.registers.setLRR(processor.next_MBR);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 3) {
          // LRR --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 2) {
          // MAR |--> MBR
          memory_words[0] = processor.next_MBR;
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 1) {
          // MBR --> R
          memory_words[0] = processor.next_MBR;
          try {
            processor.registers.setR(memory_words, instruction_fields[1]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
            if (MODULE_DEBUG_FLAG == 1) {
              System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
              bound_error.printStackTrace();
            }
          } finally {
            remaining_ticks_return--;
          }
        }
        break;
      case 3:
        // yes indirection; yes indexing
        // R --> LRR --> MAR |--> MBR --> LRR --> LRR + idx --> MAR |--> MBR --> R
        if (remaining_ticks_return <= 0) {
          // first pass so specify number of clock cycles required to complete the operation
          remaining_ticks_return = 8;
        }
        if (remaining_ticks_return > 7) {
          // IR(addy) --> LRR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Set LRR <-- (address = " + instruction_fields[4] + ").");
          }
          processor.registers.setLRR(instruction_fields[4]);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 7) {
          // LRR --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 6) {
          // MAR |--> MBR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Wait one cycle for Mem Ctrl Unit.");
          }
          processor.registers.setLRR(processor.next_MBR);
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 5) {
          // MBR --> LRR
          // this really just marks the extra clock time
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Set LRR <-- (MBR = " + processor.next_MBR + ").");
          }
          processor.registers.setLRR(processor.next_MBR);
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 4) {
          // LRR --> LRR + idx
          try {
            memory_words[0] = processor.registers.getX(instruction_fields[2]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
            if (MODULE_DEBUG_FLAG == 1) {
              System.out.println("LDR had problem accessing X[" + instruction_fields[2] + "].");
              bound_error.printStackTrace();
            }
          } finally {
            remaining_ticks_return--;
          }
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Memory address will be ( X[" + instruction_fields[2] + "] = " 
              + memory_words[0] + " ) + " + processor.registers.getLRR() + "");
          }
          // add the index to the address already on the register
          processor.registers.setLRR(memory_words[0] + processor.registers.getLRR());
        } else if (remaining_ticks_return == 3) {
          // LRR + idx --> MAR
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("(LRR+idx = " + processor.registers.getLRR() + ") --> MAR.");
            System.out.println("MAR was " + processor.registers.getMAR()
              + ", with MBR of " + processor.registers.getMBR() + "");
          }
          processor.next_MAR = processor.registers.getLRR();
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 2) {
          // MAR |--> MBR
          // TBD!! for some reason had to give it another clock
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Wait one cycle for Mem Ctrl Unit.");
          }
          memory_words[0] = processor.next_MBR;
          processor.set_memory = 0;
          remaining_ticks_return--;
        } else if (remaining_ticks_return == 1) {
          // MBR --> R
          memory_words[0] = processor.next_MBR;
          if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Set R[] <-- (MBR = " + processor.next_MBR + ").");
          }
          try {
            processor.registers.setR(memory_words, instruction_fields[1]);
          } catch(projectexceptions.MemoryOutOfBounds bound_error) {
            if (MODULE_DEBUG_FLAG == 1) {
              System.out.println("LDR had problem accessing R[" + instruction_fields[1] + "].");
              bound_error.printStackTrace();
            }
          } finally {
            remaining_ticks_return--;
          }
        }
        break;
      default:
        remaining_ticks = 0;
        processor.set_memory = 0;
        if (MODULE_DEBUG_FLAG == 1) {
          System.out.println("LDR of type " + branch_id + " is not valid.");
        }
        break;
    // end branch switch
    }
    return remaining_ticks_return;
  }
}
