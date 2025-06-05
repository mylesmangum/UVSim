UVSim 
==========================================

DESCRIPTION:
UVSim is a virtual machine simulator that executes programs written in BasicML. The simulator includes a CPU, accumulator register, 
and 100-word memory.

PREREQUISITES:
- Java Development Kit (JDK) 8 or higher
- JUnit 4 or higher for running tests

COMPILATION:
javac *.java

RUNNING THE APPLICATION:
java Main

The application will prompt you to enter a file path for the BasicML program.
Alternatively, you can modify Main.java to specify a different input file.

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
- Overflow (values > 9999 or < -9999) throws Overflow exception
- Invalid memory access throws IndexOutOfBoundsException
- Invalid file format lines are skipped during loading

LIMITATIONS:
- Memory is limited to 100 words (addresses 00-99)
- Word values must be 4-digit signed decimals (-9999 to +9999)
- Programs must start at memory address 00