import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MoodTracker{
    // Mood validation
    public static boolean isMoodValid(Mood mood, ArrayList<Mood> moodsList) throws InvalidMoodException {
        for(Mood tempMood: moodsList) {
            if (tempMood.equals(mood)) {
                throw new InvalidMoodException();
            }
        }
        return true;
    }

    // Mood deletion block
    public static boolean deleteMoods(LocalDate moodDate, ArrayList<Mood> moodsList) {
        return moodsList.removeIf(tempMood -> tempMood.getDate().equals(moodDate));
    }
    public static boolean deleteMood(Mood mood, ArrayList<Mood> moodsList) {
        return moodsList.removeIf(tempMood -> tempMood.equals(mood));
    }

    // Mood editing block
    public static boolean editMood(Mood moodToEdit, ArrayList<Mood> moodsList) {
        for(Mood tempMood: moodsList) {
            if (tempMood.equals(moodToEdit)) {
                tempMood.setNotes(moodToEdit.getNotes());
                return true;
            }
        }
        return false;
    }

    // Mood Searching block (date and specific mood)
    public static void searchMoods(LocalDate moodDate, ArrayList<Mood> moodsList) {
        boolean found = false;
        for(Mood tempMood: moodsList) {
            if (tempMood.getDate().equals(moodDate)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }

    public static void searchMood(Mood mood, ArrayList<Mood> moodsList) {
        boolean found = false;

        for(Mood tempMood: moodsList) {
            if (tempMood.equals(mood)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }

    // Helper methods for the switch cases, for modularity and clean coding reasons
    private static void handleAddMood(Scanner scanner, ArrayList<Mood> moodsList) {
        System.out.println("Enter the mood name:");
        String moodName = scanner.nextLine();
        System.out.println("Are you tracking the mood for this current day and time? y/n");
        String isForCurrentDate = scanner.nextLine();
        Mood moodToAdd = null;

        if (isForCurrentDate.equalsIgnoreCase("n")) {
            try {
                System.out.println("Input the date in dd/MM/yyyy format:");
                String moodDateStr = scanner.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);

                System.out.println("Input the time in HH:mm:ss format:");
                String moodTimeStr = scanner.nextLine();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);

                System.out.println("Add notes about this mood:");
                String moodNotes = scanner.nextLine();

                if (moodNotes.strip().equalsIgnoreCase("")) {
                    moodToAdd = new Mood(moodName, moodDate, moodTime);
                } else {
                    moodToAdd = new Mood(moodName, moodDate, moodTime, moodNotes);
                }
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date or time. Cannot create mood.\n" + dfe.getMessage());
                return; // Exit method on error
            }
        } else {
            System.out.println("Add notes about this mood");
            String moodNotes = scanner.nextLine();
            if (moodNotes.strip().equalsIgnoreCase("")) {
                moodToAdd = new Mood(moodName);
            } else {
                moodToAdd = new Mood(moodName, moodNotes);
            }
        }

        try {
            if (moodToAdd != null && isMoodValid(moodToAdd, moodsList)) { // Check for null moodToAdd
                moodsList.add(moodToAdd);
                System.out.println("The mood '" + moodToAdd.getName() + "' has been added to the tracker!");
            }
        } catch (InvalidMoodException ime) {
            System.out.println("Error: " + ime.getMessage());
        }
    }

    private static void handleDeleteMood(Scanner scanner, ArrayList<Mood> moodsList) {
        System.out.println("Enter '1' to delete all moods by date.\n" +
                "Enter '2' to delete a specific mood.");
        String deleteVariant = scanner.nextLine();

        if (deleteVariant.equals("1")) {
            try {
                System.out.println("Input the date in dd/MM/yyyy format:");
                String moodDateStr = scanner.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);

                boolean areMoodsDeleted = deleteMoods(moodDate, moodsList);
                if (areMoodsDeleted) {
                    System.out.println("Moods for " + moodDate + " have been deleted.");
                } else {
                    System.out.println("No matching moods found for that date.");
                }
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date. Cannot delete mood.\n" + dfe.getMessage());
            }
        } else if (deleteVariant.equals("2")) {
            try {
                System.out.println("Enter the mood's name:");
                String moodName = scanner.nextLine(); // Redeclare moodName
                System.out.println("Input the mood's date in dd/MM/yyyy format:");
                String moodDateStr = scanner.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);

                System.out.println("Input the mood's time in HH:mm:ss format:");
                String moodTimeStr = scanner.nextLine();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);

                Mood delMood = new Mood(moodName, moodDate, moodTime);
                boolean isMoodDeleted = deleteMood(delMood, moodsList);
                if (isMoodDeleted) {
                    System.out.println("The mood has been deleted!");
                } else {
                    System.out.println("No matching mood found with specified details.");
                }
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date or time. Cannot delete mood.\n" + dfe.getMessage());
            }
        } else {
            System.out.println("Invalid delete option. Please enter '1' or '2'.");
        }
    }

    private static void handleEditMood(Scanner scanner, ArrayList<Mood> moodsList) {
        Mood moodToFindForEdit = null;
        try {
            System.out.println("Enter the tracked mood's name:");
            String moodName = scanner.nextLine();
            System.out.println("Input the tracked mood's date in dd/MM/yyyy format:");
            String moodDateStr = scanner.nextLine();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);

            System.out.println("Input the tracked mood's time in HH:mm:ss format (default is 00:00:00):");
            String moodTimeStr = scanner.nextLine();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);

            moodToFindForEdit = new Mood(moodName, moodDate, moodTime);

            Mood actualMoodInList = null;
            for (Mood m : moodsList) {
                if (m.equals(moodToFindForEdit)) {
                    actualMoodInList = m;
                    break;
                }
            }

            if (actualMoodInList != null) {
                System.out.println("Current notes for this mood: " + actualMoodInList.getNotes());
                System.out.println("Enter new notes for this mood (leave blank to keep current, type 'clear' to remove):");
                String newMoodNotes = scanner.nextLine();

                if (newMoodNotes.equalsIgnoreCase("clear")) {
                    actualMoodInList.setNotes("");
                    System.out.println("Notes cleared for the mood.");
                } else if (!newMoodNotes.strip().isEmpty()) {
                    actualMoodInList.setNotes(newMoodNotes);
                    System.out.println("The mood notes have been successfully edited!");
                } else {
                    System.out.println("No new notes entered. Notes remain unchanged.");
                }
            } else {
                System.out.println("No matching mood could be found to edit. Be sure to input all information correctly.");
            }
        } catch (DateTimeParseException dfe) {
            System.out.println("Incorrect format of date or time. Cannot edit mood.\n" + dfe.getMessage());
        }
    }

    private static void handleSearchMood(Scanner scanner, ArrayList<Mood> moodsList) {
        System.out.println("Enter '1' to search for all moods by date\n" +
                "Enter '2' to search for a specific mood");
        String searchVariant = scanner.nextLine();

        if (searchVariant.equals("1")) {
            try {
                System.out.println("Input the date in dd/MM/yyyy format:");
                String moodDateStr = scanner.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                searchMoods(moodDate, moodsList);
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date. Cannot search mood.");
            }
        } else if (searchVariant.equals("2")) {
            try {
                System.out.println("Enter the tracked mood's name:");
                String moodName = scanner.nextLine();
                System.out.println("Input the tracked mood's date in dd/MM/yyyy format:");
                String moodDateStr = scanner.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);

                System.out.println("Input the mood's tracked time in HH:mm:ss format (default 00:00:00):");
                String moodTimeStr = scanner.nextLine();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);

                Mood searchmoodObj = new Mood(moodName, moodDate, moodTime);
                searchMood(searchmoodObj, moodsList);
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date or time. Cannot search mood.");
            }
        } else { System.out.println("Invalid search option. Please enter '1' or '2'."); }
    }

    private static void handleShowAllMoods(ArrayList<Mood> moodsList) {
        if (moodsList.isEmpty()) {
            System.out.println("The mood tracker list is currently empty.");
        } else {
            System.out.println("\n--- All Tracked Moods ---");
            for(Mood moodObj: moodsList) {
                System.out.println(moodObj);
            }
        }
    }

    private static void handleExportMoods(ArrayList<Mood> moodsList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Moods.txt"))) {
            if (moodsList.isEmpty()) {
                writer.println("No moods tracked yet.");
            } else {
                for (Mood mood : moodsList) {
                    writer.println(mood + "\n\n");
                }
            }
            System.out.println("The entries are written to a .txt file inside the MoodTracker root folder.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        // Declarations
        ArrayList<Mood> moodsList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main program loop - options 1 to 6, and 'exit' to exit the program
        while(running) {
            System.out.println("Welcome to the Mood Tracker console app! Here are your options:" +
                    "\n\t'1' to add mood\n\t'2' to delete mood" +
                    "\n\t'3' to edit mood\n\t'4' to search for moods\n\t'5' to see all moods" +
                    "\n\t'6' to write the moods to a file\n >> Type 'exit' to exit the app.");
            System.out.println("Enter your choice: ");
            String option = scanner.nextLine().trim();
            // Guard for 'exit'
            if (option.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using Mood Tracker! Have a nice day!");
                running = false;
                continue;
            }

            try {
                int choice = Integer.parseInt(option);
                // options 1 to 6 - calling the helper methods
                switch (option) {
                    case "1": // ADD mood
                        handleAddMood(scanner, moodsList);
                        break;
                    case "2": // DELETE mood by date or specific
                        handleDeleteMood(scanner, moodsList);
                        break;
                    case "3": // EDIT mood
                        handleEditMood(scanner, moodsList);
                        break;
                    case "4": // SEARCH mood by date or specific
                        handleSearchMood(scanner, moodsList);
                        break;
                    case "5": // SHOW all moods
                        handleShowAllMoods(moodsList);
                        break;
                    case "6": // EXPORT to file named Moods.txt
                        handleExportMoods(moodsList);
                        break;
                    default: System.out.println("Invalid option. Only integers 1 to 6 and 'exit' are valid inputs.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number between 1 and 6, or 'exit'.");
            }
            System.out.println("\nPress Enter to continue..."); // Added pause for better UX
            scanner.nextLine(); // Consume the leftover newline
        }
        scanner.close();
    }
}