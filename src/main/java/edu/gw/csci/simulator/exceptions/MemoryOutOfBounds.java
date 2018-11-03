/*
Project 1 - Exception : MemoryOutOfBounds

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

//PACKAGE MEMBERSHIP
package edu.gw.csci.simulator.exceptions;

/**
 * Emulation of the error when non-existent memory is addressed.
 * The idea is that, if caught, the response would set MFR or equivalent.
 *
 * @version 20180916
 */
public class MemoryOutOfBounds extends SimulatorException {

    public static final int OP_CODE = 8;

    /**
     * Instantiate an error for memory out of bounds.
     *
     * @param message A string that will be appended to the default backtrace.
     */

    public MemoryOutOfBounds(String message) {
        super(message);
    }
    public MemoryOutOfBounds(String message, boolean runRoutine) {
        super(message, runRoutine);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}

