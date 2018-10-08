package edu.gw.csci.simulator.isa;

import org.junit.Assert;
import org.junit.Test;

public class InstructionTypeTest {

    @Test
    public void testDecodeLDR(){
        String binaryOpCode = "000001";
        InstructionType it = InstructionType.getInstructionType(binaryOpCode);
        Assert.assertEquals(it, InstructionType.LDR);
    }

    @Test
    public void testNull(){
        String binaryOpCode = "";
        InstructionType it = InstructionType.getInstructionType(binaryOpCode);
        Assert.assertNull(it);
    }
}