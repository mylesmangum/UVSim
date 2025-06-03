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
                try{
                int nextValue = Integer.parseInt(line);
                if(isWord(nextValue)) {
                    myArray[i] = Integer.parseInt(line);
                    i++;
                }
                }catch(NumberFormatException e) {
                    // This skips over any lines that are not acceptable words: "abcd" "10000"
                }

            }
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found, please make sure", fileName);
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

    //Using for UVCpu Testing without needing fileName
    public Memory() {
        memoryArray = new int[100];
    }
}
