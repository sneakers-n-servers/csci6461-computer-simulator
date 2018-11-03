package edu.gw.csci.simulator.isa;

import javafx.scene.control.TextArea;

public interface IOInstruction extends Instruction {

    void setConsole(TextArea textArea);
}
