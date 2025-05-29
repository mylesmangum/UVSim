import java.util.InputMismatchException;
import java.util.Scanner;

public class UVConsole {
    public static String getFile() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter UV file path: ");
        String inputFile = userInput.nextLine();
        return inputFile;
    }
    public static int userInputInt() {
        Scanner userInput = new Scanner(System.in);
        int integerInput;
        try {
            integerInput = userInput.nextInt();
        } catch (Exception e) {
            throw new InputMismatchException("Input was not an integer");
        }
        if (!Memory.isWord(integerInput)) {
            throw new InputMismatchException("Input values must be between -9999 and 9999, and 4 digits");
        }
        return integerInput;
    }
}
