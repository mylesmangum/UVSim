import java.util.InputMismatchException;
import java.util.Scanner;

public class UVConsole {
    public static String getFile() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter UV relative file path: ");
        String inputFile = userInput.nextLine();
        return inputFile;
    }
    public static int userInputInt() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter a signed four digit word: ");
        int integerInput;
        try {
            integerInput = userInput.nextInt();
        } catch (Exception e) {
            throw new InputMismatchException("Input was not an integer");
        }
        return integerInput;
    }
}
