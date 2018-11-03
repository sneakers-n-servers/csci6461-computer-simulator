/*
Project 1 - Exception : IllegalOpcode

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180916
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180916
        Modified:   
    -

%%%     20180916
        Author: Group 9    
        Project:    CSCI_6461_F18_Project1
    -Initial release

%%%%%%%%%%
*/


package edu.gw.csci.simulator.exceptions;

/**
 * Emulation of the error when an illegal opcode is encountered.
 * The idea is that, if caught, the response would set MFR or equivalent.
 *
 * @version 20180916
 */
public class IllegalOpcode extends SimulatorException {

    public static final int OP_CODE = 4;

    /**
     * Instantiate an error for illegal opcode.
     *
     * @param message A string that will be appended to the default backtrace.
     */
    public IllegalOpcode(String message) {
        super(message);
    }
    public IllegalOpcode(String message, boolean runRoutine) {
        super(message, runRoutine);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }

}

