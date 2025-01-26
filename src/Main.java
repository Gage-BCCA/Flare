import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserInterface ui = new UserInterface();
        Scanner input = new Scanner(System.in);
        DatabaseContext db = new DatabaseContext();

        ArrayList<Entry> entries = db.generateDummyData();

        ui.printLogo();

        boolean programActive = true;
        while (programActive) {
            ui.printMainMenu();
            String option = input.nextLine();
            switch (option) {
                case "1":
                    handlePrintAllEntries(ui, entries);
                    break;
                case "2":
                    handlePrintMostRecentEntry(ui, entries.getFirst());
                    break;
                case "3":
                    handleCreateEntry(ui);
                    break;
                case "4":
                    handleSearchByLanguage(entries);
                    break;
                case "0":
                    programActive = false;
            }

        }
    }

    public static void handlePrintAllEntries(UserInterface ui, ArrayList<Entry> entries) {
        // Will eventually have logic to grab all entries from Database
        ui.printEntries(entries);
    }

    public static void handlePrintMostRecentEntry(UserInterface ui, Entry entry){
        // Will eventually have logic to query most recent logic
        ui.printMostRecentEntry(entry);
    }

    public static boolean handleCreateEntry(UserInterface ui){
        Scanner input = new Scanner(System.in);
        System.out.print("> Enter Language: ");
        String language = input.nextLine();

        System.out.print("> Enter Notes: ");
        String notes = input.nextLine();

        System.out.print("> Enter Start Date (YYYY-MM-DD HH:SS): ");
        String startDateString = input.nextLine();

        System.out.print("> Enter End Date (YYYY-MM-DD HH:SS): ");
        String endDateString = input.nextLine();

        Date endDate;
        Date startDate;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:SS");
        try {
            endDate = formatter.parse(endDateString);
        }
        catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        try {
            startDate = formatter.parse(startDateString);
        }
        catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        Entry newEntry = new Entry(
                language,
                notes,
                startDate,
                endDate
        );

        System.out.println(newEntry.language + "---------" + newEntry.notes);
        System.out.println(newEntry.startTime + "---------" + newEntry.endTime);
        return true;
    }

    public static void handleSearchByLanguage(ArrayList<Entry> entries) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Language to Search: ");
        System.out.print("> ");
        String targetLanguage = input.nextLine().toLowerCase();

        ArrayList<Entry> targetEntries = new ArrayList<>();

        for (Entry entry: entries) {
            if (entry.language.toLowerCase().equals(targetLanguage)) {
                targetEntries.add(entry);
            }
        }

        System.out.println("Number of Entries Found: " + targetEntries.size());
        for (Entry entry : targetEntries) {
            System.out.println(entry.language + "---------" + entry.notes);
        }
    }

}
