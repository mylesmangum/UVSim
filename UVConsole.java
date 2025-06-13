import java.util.InputMismatchException;
import java.util.Scanner;

public class UVConsole {
    public static String getFile() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter UV relative file path: ");
        String inputFile = userInput.nextLine();
        if (!inputFile.endsWith(".txt")) {
            System.out.println("Invalid file format, please try again\n");
            return getFile();
        }
        return inputFile;
    }
    public static int userInputInt() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter a signed four digit word: ");
        int integerInput;
        try {
            integerInput = userInput.nextInt();
        } catch (Exception e) {
            System.out.println("Please enter a valid integer");
            return userInputInt();
        }
        return integerInput;
    }
    public static void displayMalformedLine(String line) {
        System.out.printf("It seems that a line in your file is not a 4 digit word.\nThe line causing issues is:\n%s\nThis line has been skipped in case it's a comment.\n", line);
    }
}
