UVSim 
==========================================

DESCRIPTION:
UVSim is a virtual machine simulator that executes programs written in BasicML.
The simulator includes a CPU, accumulator register, and 250-word memory.
Users can load, edit, and run BasicML programs within a user-friendly GUI.

### 🎥 Demo
[![UVSim demo](https://img.youtube.com/vi/VojEco3saF8/maxresdefault.jpg)](https://youtu.be/VojEco3saF8)

FEATURES:
- Java-based desktop GUI with program, memory, and output panes.
- Load, edit, and save BasicML programs.
- Swap between multiple text files to run individually.
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
- **Convert 4->6**: Converts a `.txt` file with 4 digit words to one with 6 digits instead.  Select the file that you would like to change, and then after selected save it as any other name to convert it.  To run the converted file, you will need to reopen the file with the Open button.
- **Save As**: Save the current program (with format validation).
- **Clear Output**: Clears the output pane.
- **Customize**: Opens color selection tools (primary/secondary).

#### Program Pane
This is displayed on the top-left hand side of the GUI, after a file has been selected.  Within
this specific pane, the file is editable before running the program.  To save the edits, select the
"Save As" button in the top section of the GUI.
In addition to allowing edits, the user can also open multiple files at one time.  To swap between files,
there are labels above the program pane that can be selected.  On selection, the chosen file will be displayed.

#### Memory Pane
This is displayed on the bottom-left hand side of the GUI, after a file has been selected.  It is a 
visual representation of the virtual memory storing all the code above it.  As the program runs, the memory
will update in real time based on any inputs of the user.

#### Output Pane
This is displayed on the right hand side of the GUI.  Displays output from write instructions and other system messages.

#### Input Field
Underneath the GUI is the area where user's can input based on what their code actually does. 
Prompts for user input will display above in the output pane.

#### CPU Status
Shows the current accumulator and the program counter values in real time.

#### Customize Pane
On the far right of the top panel is a button labeled "Customize"
On selection, the GUI will extend to have two additional buttons explained below
- **Primary Color**:  Background blank space can be modified to any selected color based on the choice of the user
- **Secondary Color**:  All buttons' background color can be modified based on the choice of the user
When these buttons are selected, a secondary window will display with several ways for the user to change the color of their GUI
The color will change as soon as the choice is confirmed in this secondary window by the user.
The new preference will be kept between launches of the GUI so the user doesn't need to select their preference every time.

## File Format:
- Programs must be .txt files
- Each line contains a signed 6-digit decimal number
- Instructions start at memory location 000
- Format: +XXXYYY where XXX is the operation code and YYY is the memory address

BASICML INSTRUCTION SET:
I/O Operations:
- 010XXX: READ - Read from keyboard to memory address XXX
- 011XXX: WRITE - Write from memory address XXX to screen

Load/Store Operations:
- 020XXX: LOAD - Load from memory address XXX to accumulator
- 021XXX: STORE - Store accumulator to memory address XXX

Arithmetic Operations:
- 030XXX: ADD - Add memory address XXX to accumulator
- 031XXX: SUBTRACT - Subtract memory address XXX from accumulator
- 032XXX: DIVIDE - Divide accumulator by memory address XXX
- 033XXX: MULTIPLY - Multiply accumulator by memory address XXX

Control Operations:
- 040XXX: BRANCH - Jump to memory address XXX
- 041XXX: BRANCHNEG - Jump to address XXX if accumulator is negative
- 042XXX: BRANCHZERO - Jump to address XXX if accumulator is zero
- 043XXX: HALT - Stop program execution

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
- Overflow (values > 999999 or < -999999) displays an error and requests input again.
- Invalid memory access throws IndexOutOfBoundsException
- Invalid file format lines are skipped during loading, and the user is notified.

LIMITATIONS:
- Memory is limited to 250 words (addresses 00-249)
- Word values must be 6-digit signed decimals (-999999 to +999999)
- Programs must start at memory address 000
