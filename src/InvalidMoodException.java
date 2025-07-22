public class InvalidMoodException extends Exception {
    public InvalidMoodException() {
        super("Invalid mood operation.");
    }
    public InvalidMoodException(String message) {
        super(message);
    }

}