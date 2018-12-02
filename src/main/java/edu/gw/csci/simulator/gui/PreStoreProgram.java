package edu.gw.csci.simulator.gui;

public class PreStoreProgram {
    public static void SetProgramLS(Program program) {
        program.appendLine("0000110000011001");//LDA R0,0,25 0000110000011001 R0=25
        program.appendLine("0000100000010100");//STR R0,0,20 0000100000010100 LOC(20)=25, MBR=25
        program.appendLine("0000110000000011");//LDA R0,0,3 0000110000000011 R0=3
        program.appendLine("0000100000011110");//STR R0,0,30 0000100000011110 LOC(30)=3, MBR=3
        program.appendLine("1000010001011110");//LDX X1,0,30 1000010001011110 X1=3, MAR=30, MBR=3
        program.appendLine("0000111100011101");//LDA R3,0,29 0000111100011101 R3=29
        program.appendLine("0000101101010110");//STR R3,1,22 0000101101010110 LOC(22+X1)=29, LOC(25)=29, MBR=29
        program.appendLine("0000011000110100");//LDR R2,0,20,1 0000011000110100 R2=LOC(LOC(20))=29, MAR=25, MBR=29
        program.appendLine("0000101001010001");//STR R2,1,17 0000101001010001 LOC(17+X1)=29, LOC(20)=29, MBR=29
        program.appendLine("1000100001110100");//STX X1,20,1 1000100001110100 LOC(LOC(20))=3, LOC(29)=3, MAR=20, MBR=3
    }

    public static void SetProgram1(Program program) {
        program.appendLine("1000010001001000");
        program.appendLine("1000010010001001");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000000");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000001");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000010");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000011");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000100");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000101");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000110");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001000111");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001000");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001001");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001010");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001011");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001100");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001101");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001110");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001001111");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001010000");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001010001");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001010010");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001010011");
        program.appendLine("1100010000000000");
        program.appendLine("0000100001010100");
        program.appendLine("0000010001010100");
        program.appendLine("0001010001110110");
        program.appendLine("0011110010000101");
        program.appendLine("0101010000000000");
        program.appendLine("0001100000000001");
        program.appendLine("0001010001010101");
        program.appendLine("0011110010001010");
        program.appendLine("0001000001010101");
        program.appendLine("0000100001010101");
        program.appendLine("0000011101110110");
        program.appendLine("0000010101010110");
        program.appendLine("0001100100000001");
        program.appendLine("0000100101010110");
        program.appendLine("0001010101010111");
        program.appendLine("0010010110000000");
        program.appendLine("1100101100000001");
    }

    public static void SetProgram2(Program program) {
        program.appendLine("1000010001001101");
        program.appendLine("1000010010001110");
        program.appendLine("1100010000000000");
        program.appendLine("0000100000001011");
        program.appendLine("0000010100101100");
        program.appendLine("0001110100001010");
        program.appendLine("0010010101010010");
        program.appendLine("0000010100001111");
        program.appendLine("0001100100000001");
        program.appendLine("0000100100001111");
        program.appendLine("0000100100010000");
        program.appendLine("0001010100010000");
        program.appendLine("0001100100000001");
        program.appendLine("0000100100010000");
        program.appendLine("0000010100001100");
        program.appendLine("0001100100000001");
        program.appendLine("0000100100001100");
        program.appendLine("0010110001000100");
        program.appendLine("0000010100101100");
        program.appendLine("0001110100001011");
        program.appendLine("0010010101010111");
        program.appendLine("0000100100001111");
        program.appendLine("0010110010000001");
        program.appendLine("0000010000001011");
        program.appendLine("0001010000101100");
        program.appendLine("0010000010000001");
        program.appendLine("0000010100010000");
        program.appendLine("0001100100000001");
        program.appendLine("0000100100010000");
        program.appendLine("0000010100001100");
        program.appendLine("0001100100000001");
        program.appendLine("0000100100001100");
        program.appendLine("0010110001000100");
        program.appendLine("0000010100001111");
        program.appendLine("1100100100000001");
        program.appendLine("0000010100010000");
        program.appendLine("1100100100000001");
    }

  /**
   * This program satisfies the requirements for part IV by testing the new floating point and vector features.
   * It loads integers from memory, converts them to floating point, stores floating point, then  adds/subtracts.
   * It also loads a vector length/address from memory and adds/subtracts its values.
   *
   * @param program
   * @version 20181201
   */
  public static void SetProgram3(Program program) {
    // this currently assumes memory location 7 for the first instruction - see PreStoreMemoryForProgram3
    program.appendLine("1000010011010110");    // LDX r = 0, x = 3, address = 22, [I = 0];    program ix <-- memory location of program's first instruction (:START)
    program.appendLine("0000110000000001");    // LDA r = 0, x = 0, address = 1, [I = 0];    prepare toFloat conversion flag
    program.appendLine("0111110000111001");    // CNVRT r = 0, x = 0, address = 24, [I = 1];    toFloat( *(vectors length) )
    program.appendLine("1000110000011001");    // VADD r = 0, x = 0, address = 25, [I = 0];    add vectors
    program.appendLine("1001000000011001");    // VSUB r = 0, x = 0, address = 25, [I = 0];    subtract vectors; ie, back to original
    program.appendLine("1000010001010111");    // LDX r = 0, x = 1, address = 23, [I = 0];    data ix <-- memory location of first int
    program.appendLine("0000110000000001");    // LDA r = 0, x = 0, address = 1, [I = 0];    prepare toFloat conversion flag
    program.appendLine("0111110001000000");    // CNVRT r = 0, x = 1, address = 0, [I = 0];    toFloat( *(ints for float) )
    program.appendLine("1010010000010101");    // STFR r = 0, x = 0, address = 21, [I = 0];    float --> scratch
    program.appendLine("0111110001000001");    // CNVRT r = 0, x = 1, address = 1, [I = 0];    toFloat( *(ints for float + 1) )
    program.appendLine("0110110000010101");    // FADD r = 0, x = 0, address = 21, [I = 0];    += scratch
    program.appendLine("0111000000010101");    // FSUB r = 0, x = 0, address = 21, [I = 0];    -= scratch; ie, back to second float
    program.appendLine("0010110011000000");    // JMA r = 0, x = 3, address = 0, [I = 0];    goto :START
  }

}
