package edu.gw.csci.simulator.isa;

import org.junit.Assert;
import org.junit.Test;

public class InstructionTypeTest {

    @Test
    public void testOctal(){
        InstructionType it = InstructionType.LDX;
        Assert.assertEquals(51, it.getOpCode());
    }

    @Test
    public void testDecodeLDR(){
        String binaryOpCode = "110011";
        InstructionType it = InstructionType.getInstructionType(binaryOpCode);
        Assert.assertEquals(it, InstructionType.LDX);
    }

    @Test
    public void testNull(){
        String binaryOpCode = "";
        InstructionType it = InstructionType.getInstructionType(binaryOpCode);
        Assert.assertNull(it);
    }
}