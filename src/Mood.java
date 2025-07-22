import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // Import for formatting
import java.util.Objects; // Import for Objects.hash()

public class Mood {
    private String name;
    private LocalDate date;
    private LocalTime time;
    private String notes;

    // --- Constructors ---

    // Full, base constructor
    public Mood(String name, LocalDate date, LocalTime time, String notes) {
        this.name = name;
        this.date = date;
        this.time = time;
        // Notes are never null to avoid NullPointerExceptions later
        this.notes = (notes == null) ? "" : notes;
    }

    // Constructor for specific date/time, no notes
    public Mood(String name, LocalDate date, LocalTime time) {
        this(name, date, time, ""); // Calls the full constructor with empty notes
    }

    // Constructor for current date/time, with notes - .withNano(0)
    public Mood(String name, String notes) {
        this(name, LocalDate.now(), LocalTime.now().withNano(0), notes); // Calls the full constructor
    }

    // Constructor for current date/time, no notes
    public Mood(String name) {
        this(name, LocalDate.now(), LocalTime.now().withNano(0), ""); // Calls the full constructor
    }

    // Constructor for specific date - default time MIDNIGHT
    public Mood(String name, LocalDate date) {
        this(name, date, LocalTime.MIDNIGHT, ""); // Calls the full constructor
    }

    // Constructor for specific date, with notes - default time MIDNIGHT
    public Mood(String name, LocalDate date, String notes) {
        this(name, date, LocalTime.MIDNIGHT, notes); // Calls the full constructor
    }


    // --- Getters ---
    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getNotes() {
        return this.notes;
    }

    // --- Setter ---
    public void setNotes(String notes) {
        this.notes = (notes == null) ? "" : notes; // Ensure notes are never null
    }

    @Override
    public String toString() {
        // Define formatters for consistent output
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formattedDate = (date != null) ? date.format(dateFormatter) : "N/A";
        String formattedTime = (time != null) ? time.format(timeFormatter) : "N/A";
        String displayNotes = (notes != null && !notes.isEmpty()) ? notes : "No notes";

        // Just a nice structure, for readability
        return "--- Mood Entry ---\n" +
                "Mood: " + name + "\n" +
                "Date: " + formattedDate + "\n" +
                "Time: " + formattedTime + "\n" +
                "Notes: " + displayNotes +
                "\n--------------------";
    }

    // --- equals() and hashCode() Methods ---

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mood otherMood = (Mood) obj;

        return Objects.equals(this.date, otherMood.date) &&
                Objects.equals(this.time, otherMood.time);

    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }
}