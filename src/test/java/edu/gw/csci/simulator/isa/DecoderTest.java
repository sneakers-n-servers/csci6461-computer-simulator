package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.utils.BitConversion;
import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

public class DecoderTest {

    @Test
    public void testDecode(){
        String binary = InstructionType.HLT.getBinary();
        BitSet b = BitConversion.convert(binary);
        Decoder decoder = new Decoder();
        Instruction instruction = decoder.getInstruction(b);
        Assert.assertEquals(InstructionType.HLT, instruction.getInstructionType());
    }

    @Test
    public void testNewObjectCreation(){
        String binary = InstructionType.HLT.getBinary();
        BitSet b = BitConversion.convert(binary);
        Decoder decoder = new Decoder();
        Instruction instruction1 = decoder.getInstruction(b);
        Instruction instruction2 = decoder.getInstruction(b);
        Assert.assertNotEquals(instruction1, instruction2);
    }

}