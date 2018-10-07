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
package projectexceptions;

public class MemoryOutOfBounds extends Exception {
    //CONSTRUCTORS
    public MemoryOutOfBounds(String exception_string) {
        // Call constructor of parent Exception
        super(exception_string);
    }
}

