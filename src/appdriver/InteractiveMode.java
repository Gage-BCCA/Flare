package appdriver;

import data.DatabaseContext;
import data.models.Entry;
import userinterface.MainMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class InteractiveMode implements AppModeInterface {

    private Scanner input;
    private DatabaseContext db;
    private boolean programActive;

    public InteractiveMode(Scanner input, DatabaseContext db) {
        this.input = input;
        this.db = db;
        this.programActive = true;
    }

    public void Run() {
        ArrayList<Entry> entries = db.generateDummyData();
        MainMenu ui = new MainMenu(input);

        ui.printLogo();

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

    private void handlePrintAllEntries(MainMenu ui, ArrayList<Entry> entries) {
        // Will eventually have logic to grab all entries from Database
        ui.printEntries(entries);
    }

    private void handlePrintMostRecentEntry(MainMenu ui, Entry entry){
        // Will eventually have logic to query most recent logic
        ui.printMostRecentEntry(entry);
    }

    private boolean handleCreateEntry(MainMenu ui){
        Scanner input = new Scanner(System.in);
        System.out.print("> Enter Language: ");
        String language = input.nextLine();

        System.out.print("> Enter Notes: ");
        String notes = input.nextLine();

        System.out.print("> Enter Duration: ");
        int duration = input.nextInt();

        Entry newEntry = new Entry(
                notes,
                duration
        );

        return true;
    }

    private void handleSearchByLanguage(ArrayList<Entry> entries) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Language to Search: ");
        System.out.print("> ");
        String targetLanguage = input.nextLine().toLowerCase();

        ArrayList<Entry> targetEntries = new ArrayList<>();

//        for (Entry entry: entries) {
//            if (entry.language.toLowerCase().equals(targetLanguage)) {
//                targetEntries.add(entry);
//            }
//        }

//        System.out.println("Number of Entries Found: " + targetEntries.size());
//        for (Entry entry : targetEntries) {
//            System.out.println(entry.language + "---------" + entry.notes);
//        }
    }


}
