import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    MemoryRegister[] memoryArray;
    private UVSimGUI gui;

    public Memory(String fileName, UVSimGUI gui) {
        this.gui = gui; // 👈 save the reference
        if (fileName.endsWith(".txt")) {
            memoryArray = readText(fileName);
        } else {
            throw new RuntimeException("Invalid file format");
        }
    }
    public MemoryRegister read(int address) {
//        checkIndex(address);
        return memoryArray[address];
    }
    public void write(int address, int value) {
//        checkIndex(address);
        memoryArray[address].setValue(value);
    }
    //250 is mem size
    public MemoryRegister[] readText(String fileName) {
        MemoryRegister[] myArray = new MemoryRegister[250];
        for (int i = 0; i < 250; i++) {
            myArray[i] = new MemoryRegister();
        }
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try{
                if(isWord(line)) {
                    myArray[i].setValue(Integer.parseInt(line));
                    i++;
                }
                else {
                    gui.displayMalformedLine(line);
                }
                }catch(NumberFormatException e) {
                    gui.displayMalformedLine(line);
                }
                if (i >= 250 && scanner.hasNextLine()) {
                    gui.displayOutput("Error: File has more than 250 valid instructions. Only the first 250 were loaded.");
                }
            }
        } catch (FileNotFoundException e) {
            gui.displayOutput("File not found" + fileName);
        }
        return myArray;
    }

    public static boolean isWord(String value) {
        return value.matches("[-+][0-9]{4}") || value.matches("[-+][0-9]{6}");
    }

    //Using for UVCpu Testing without needing fileName
    public Memory() {
        memoryArray = new MemoryRegister[250];
        for (int i = 0; i < 250; i++) {
            memoryArray[i] = new MemoryRegister();
        }
    }
}
