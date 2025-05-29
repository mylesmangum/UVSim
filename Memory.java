import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    int[] memoryArray = new int[100];
    public Memory(String fileName) {
        if (fileName.endsWith(".txt")) {
            memoryArray = readText(fileName);
        }
    }
    public int read(int address) {
        checkIndex(address);
        return memoryArray[address];
    }
    public void write(int address, int value) {
        // TODO, refactor to print to screen instead of writing to memory?
        // I have two ideas in my mind on what this does and I want to clairify
        checkIndex(address);
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
                int nextValue = Integer.parseInt(line);
                if(isWord(nextValue)) {
                    myArray[i] = Integer.parseInt(line);
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found", fileName);
            e.printStackTrace();
        }
        return myArray;
    }

    private void checkIndex(int address) {
        if (address > 99 || address < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static boolean isWord(int value) {
        return String.valueOf(Math.abs(value)).length() == 4;
    }
}
