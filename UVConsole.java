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
        String message = String.format(
                "Skipped malformed line: %s\nOnly signed 4-digit numbers are allowed.\n", line);

        if (gui != null) {
            UVConsole.displayOutput(message);
        } else {
            System.out.print(message);
        }

    }
}
