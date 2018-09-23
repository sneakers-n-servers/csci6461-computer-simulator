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

public class IllegalOpcode extends SimulatorException {

    /**
     * Emulation of the error when an illegal opcode is encountered.
     * @param message A string that will be appended to the default backtrace.
     * @version 20180916
     */
    public IllegalOpcode(String message) {
        super(message);
    }

}

