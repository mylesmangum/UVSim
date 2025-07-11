UVSim 
==========================================

DESCRIPTION:
UVSim is a virtual machine simulator that executes programs written in BasicML.
The simulator includes a CPU, accumulator register, and 100-word memory.
Users can load, edit, and run BasicML programs within a user-friendly GUI.

FEATURES:
- Java-based desktop GUI with program, memory, and output panes.
- Load, edit, and save BasicML programs.
- Step-free execution with real-time output.
- Input prompt for READ instructions.
- Restart functionality for quick reloads.
- GUI customization (color themes persist across runs).
- Visual memory + CPU state updates.
- Error handling for invalid input, malformed lines, and overflows.
- JUnit test suite included.

PREREQUISITES:
- Java JDK 8 or higher
- JUnit 4 (for testing)

COMPILATION:
The application has been precompiled into a jar file that is within the repo.
The file named "UVSim.jar" is what you need to run the program.

RUNNING THE APPLICATION:
1. Ensure `UVSim.jar` and any `.txt` BasicML files are in the same folder.
2. Run the program:
    - **Double-click** `UVSim.jar`, or
    - Use terminal: `java -jar UVSim.jar`
3. The GUI will launch.

### GUI Overview

#### Top Buttons
- **Open Program File**: Load a `.txt` BasicML file from any file system.
- **Run Program**: Execute loaded or edited instructions via file selected by user.
- **Restart Program**: Reload the last opened file.
- **Save As**: Save the current program (with format validation).
- **Clear Output**: Clears the output pane.
- **Customize**: Opens color selection tools (primary/secondary).

#### Program Pane
Edit or review the loaded code. Each line must be a signed 4-digit word.

#### Memory Pane
Displays non-zero memory values and their addresses.

#### Output Pane
Displays output from write instructions and other system messages.

#### Input Field
Prompts for user input when a read instruction is reached.

#### CPU Status
Shows the current accumulator and the program counter values in real time.

## File Format:
- Programs must be .txt files
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
- Test1.txt: addition
- Test2.txt: comparison
- Test3.txt: overflow
- Test4.txt: branching
- Test5.txt: error handling

ERROR HANDLING:
- Division by zero throws ArithmeticException
- Overflow (values > 9999 or < -9999) displays an error and requests input again.
- Invalid memory access throws IndexOutOfBoundsException
- Invalid file format lines are skipped during loading, and the user is notified.

LIMITATIONS:
- Memory is limited to 100 words (addresses 00-99)
- Word values must be 4-digit signed decimals (-9999 to +9999)
- Programs must start at memory address 00
