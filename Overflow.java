/**
 * Overflow exception to check for exceeding value allowed by program
 */
public class Overflow extends RuntimeException {
    public Overflow(String message) {
        super(message);
    }
}
