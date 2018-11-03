package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterType;

public class SetCC {

    public static final int MaxValue = (int) Math.pow(2, 15) - 1;
    public static final int MinValue = (int) -Math.pow(2, 15);
    public static final int ExtendMaxValue = (int) Math.pow(2, 31) - 1;
    public static final int ExtendMinValue = (int) -Math.pow(2, 31);
    private static AllRegisters registers = new AllRegisters();
    private static Register CC = registers.getRegister(RegisterType.CC);

//    private static final Logger LOGGER = LogManager.getLogger(SetCC.class);
//
//    /**
//     * SET CC OVERFLOW,UNDERFLOW,DIVZERO,EQUALORNOT
//     */
//    public static void OVERFLOW(){
//        //Register CC = registers.getRegister(RegisterType.CC);
//        BitSet bits = CC.getData();
//        bits.set(3);
//        CC.setData(bits);
//        LOGGER.warn("OVERFLOW");
//    }
//
//    public static void UNDERFLOW(AllRegisters registers){
//        Register CC = registers.getRegister(RegisterType.CC);
//        BitSet bits = CC.getData();
//        bits.set(2);
//        CC.setData(bits);
//        LOGGER.warn("UNDERFLOW");
//    }
//
//    public static void DIVZERO(AllRegisters registers){
//        Register CC = registers.getRegister(RegisterType.CC);
//        BitSet bits = CC.getData();
//        bits.set(1);
//        CC.setData(bits);
//        LOGGER.warn("DIVIDE 0");
//    }
//
//    public static void EQUALORNOT(AllRegisters registers, boolean equal){
//        Register CC = registers.getRegister(RegisterType.CC);
//        BitSet bits = CC.getData();
//        if(equal){
//            bits.set(0);
//            LOGGER.info("EQUAL");
//        }
//        else{
//            bits.clear(0);
//            LOGGER.info("NOTEQUAL");
//        }
//        CC.setData(bits);
//    }
//
//    public static boolean checkOverUnderFlow(int value,AllRegisters registers){
//        if(value>MaxValue||value<MinValue){
//            OVERFLOW();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    public static boolean checkExtendOverUnderFlow(int value,AllRegisters registers){
//        if(value>ExtendMaxValue||value<ExtendMinValue){
//            OVERFLOW();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
}
