# csci6461-computer-simulator
This repository houses all files used to create our similator
-------------------------------------------------------------------------------------------------------------------------------------------------------------

CS6461: Computer Architectures Project
Part1: Basic Machine
Group 9
-------------------------------------------------------------------------------------------------------------------------------------------------------------
IPL:
1.By Click Button [IPL], the Register and Memory will become the initial state.

-------------------------------------------------------------------------------------------------------------------------------------------------------------

IMPLEMENT A MEMORY/GET DATA from Memory:
1.You can use store memory to set the memory data
Input ADDR and VALUE (Decimal) and click Button [Store]
(ADDR should be a integer greater or equal to 6 and less than or equal to 2047, and VALUE should be less than 2^16, otherwise it will not work.)
2. You can use get memory to get the data in the memory.
Input ADDR (Decimal) and click Button [Get]
(ADDR should be a integer greater or equal to 0 and less than or equal to 2047, otherwise it will not work.)
And the Lable besides the input field will show the value of that index.
If the input is not correct, the label will show the error information.

-------------------------------------------------------------------------------------------------------------------------------------------------------------

INSTRUCTIONS:
1.Input a 16 bit binary number (instruction) and click the Button [Execute] to execute the instruction. Then the panel will change.
For example:
1010010011111111 (LDX,3,31,1)
1010010001010111 (LDX,1,23)
0000011100011111 (LDR,3,0,31)
Note: Execution will not be successful if the input is not 16 bit number or not binary.
2.After execution, PC+1, MAR, MBR and the R0-R3/X1-X3 used in the instruction will change.
