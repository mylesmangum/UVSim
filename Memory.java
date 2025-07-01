import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    int[] memoryArray = new int[100];
    private UVSimGUI gui;

    public Memory(String fileName, UVSimGUI gui) {
        this.gui = gui; // 👈 save the reference
        if (fileName.endsWith(".txt")) {
            memoryArray = readText(fileName);
        } else {
            throw new RuntimeException("Invalid file format");
        }
    }
    public int read(int address) {
//        checkIndex(address);
        return memoryArray[address];
    }
    public void write(int address, int value) {
//        checkIndex(address);
        memoryArray[address] = value;
    }
    public int[] readText(String fileName) {
        int[] myArray = new int[100];
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try{
                //int nextValue = Integer.parseInt(line); Changed to use just the string
                if(isWord(line)) {
                    myArray[i] = Integer.parseInt(line);
                    i++;
                }
                else {
                    gui.displayMalformedLine(line);
                    //System.out.printf("in else");

                }
                }catch(NumberFormatException e) {
                    gui.displayMalformedLine(line);
                    //System.out.printf("in catch");

                }

            }
        } catch (FileNotFoundException e) {
            gui.displayOutput("File not found" + fileName);
        }
        return myArray;
    }



    public static boolean isWord(String value) { //changed to take a string, match it to be 5 chars


        return value.length() == 5;
    }

    //Using for UVCpu Testing without needing fileName
    public Memory() {
        memoryArray = new int[100];
    }
}
