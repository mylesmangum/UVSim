import java.util.InputMismatchException;
import java.util.Scanner;

public class UVConsole {
    private static UVSimGUI gui = null;

    public static void setGUI(UVSimGUI guiInstance) {
        gui = guiInstance;
    }

    public static String getFile() {
        if (gui != null) {
            return ""; // File selection handled by GUI
        }
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
        if (gui != null) {
            UVConsole.displayOutput("Enter a signed four digit word: ");
            String input = gui.getInput();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new InputMismatchException("Input was not an integer");
            }
        }
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

    public static void displayOutput(String output) {
        if (gui != null) {
            gui.displayOutput(output);
        } else {
            System.out.println(output);
        }
    }
    public static void displayMalformedLine(String line) {
        System.out.printf("It seems that a line in your file is not a 4 digit word.\nThe line causing issues is:\n%s\nThis line has been skipped in case it's a comment.\n", line);
    }
}
