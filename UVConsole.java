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
        return inputFile;
    }
    public static int userInputInt() {
        if (gui != null) {
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
            throw new InputMismatchException("Input was not an integer");
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
}
