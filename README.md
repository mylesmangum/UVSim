UVSim 
==========================================

DESCRIPTION:
UVSim is a virtual machine simulator that executes programs written in BasicML.
The simulator includes a CPU, accumulator register, and 100-word memory.

PREREQUISITES:
- Java Development Kit (JDK) 8 or higher
- JUnit 4 or higher for running tests

COMPILATION:
The application has been precompiled into a jar file that is within the repo.
The file named "UVSim.jar" is what you need to run the program.

RUNNING THE APPLICATION:
This is a desktop application that will utilize outside files.  For ease of access,
please place any BasicML text files within the same folder as your UVSim.jar file.
To launch the program, attempt to double click the UVSim.jar file, if this doesn't work,
input "java -jar UVSim.jar" into your terminal
You should see a GUI launch, with a few different possible inputs, below are instructions
on each section and button.

Open Program File
=================
Selecting this option will open up a secondary menu.  This menu will open up into your local folder, where
UVSim.jar is located.  From here, select a text file to be loaded into memory
Run Program
=================
After a file has been loaded, this button will be pressable.  Selecting this will run through your code, starting
at where the Program Counter is pointing to in memory.  To rerun the code, you will need to reopen the file with 
Open Program File.

Input
=================
The Output textbox will notify the user whenever input is required.  When input is requested by the Output, 
any text inputted into the Input box will be sent to the CPU after pressing Enter.  Inputs must be between 
-9999 and 9999 and only an integer.  Any improper inputs will display an error.

Other information
=================
Program: In the top left, there will be a textbox that contains the text file you have opened.
Memory: This is a visual representation of UVSim's memory, use this to compare with Program to ensure
all words are being parsed correctly.  If they are not you will see a message pop up in the Output
Output: This will be where any prompts for the user are displayed, any issues related to the text file,
required inputs, and the outputs of the code will be seen here.
CPU: At the bottom of the screen, the Accumulator and Program Counter for the UVSim CPU are displayed and
updated as the program runs.


FILE FORMAT:
- BasicML programs should be text files
- Each line contains a signed 4-digit decimal number
- Instructions start at memory location 00
- Format: +XXYY where XX is the operation code and YY is the memory address

BASICML INSTRUCTION SET:
I/O Operations:
- 10XX: READ - Read from keyboard to memory address XX
- 11XX: WRITE - Write from memory address XX to screen

Load/Store Operations:
- 20XX: LOAD - Load from memory address XX to accumulator
- 21XX: STORE - Store accumulator to memory address XX

Arithmetic Operations:
- 30XX: ADD - Add memory address XX to accumulator
- 31XX: SUBTRACT - Subtract memory address XX from accumulator
- 32XX: DIVIDE - Divide accumulator by memory address XX
- 33XX: MULTIPLY - Multiply accumulator by memory address XX

Control Operations:
- 40XX: BRANCH - Jump to memory address XX
- 41XX: BRANCHNEG - Jump to address XX if accumulator is negative
- 42XX: BRANCHZERO - Jump to address XX if accumulator is zero
- 43XX: HALT - Stop program execution

RUNNING TESTS:
To run the unit tests, compile with JUnit and execute:
java -cp .:junit-4.x.x.jar:hamcrest-core-x.x.x.jar org.junit.runner.JUnitCore UVSimTest

SAMPLE FILES:
- Test1.txt: input, addition, and output
- Test2.txt: branching and comparison

ERROR HANDLING:
- Division by zero throws ArithmeticException
- Overflow (values > 9999 or < -9999) displays an error and requests input again.
- Invalid memory access throws IndexOutOfBoundsException
- Invalid file format lines are skipped during loading, and the user is notified.

LIMITATIONS:
- Memory is limited to 100 words (addresses 00-99)
- Word values must be 4-digit signed decimals (-9999 to +9999)
- Programs must start at memory address 00
