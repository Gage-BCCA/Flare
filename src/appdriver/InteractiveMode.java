package appdriver;

import data.models.Entry;
import userinterface.MainMenu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;

public class InteractiveMode implements AppModeInterface {

    private Scanner input;
    private Connection context;
    private boolean programActive;

    public InteractiveMode(Scanner input, Connection db) {
        this.input = input;
        this.context = db;
        this.programActive = true;
    }

    public void Run() {
        //ArrayList<Entry> entries = db.generateDummyData();
        MainMenu ui = new MainMenu(input);

        ui.printLogo();

        while (programActive) {
            ui.printMainMenu();
            String option = input.nextLine();
            switch (option) {
                case "1":
                    handlePrintRecentEntries(ui, context);
                    break;
                case "2":
                    //handlePrintMostRecentEntry(ui);
                    break;
                case "3":
                   // handleCreateEntry(ui);
                    break;
                case "4":
                    //handleSearchByLanguage(entries);
                    break;
                case "0":
                    programActive = false;
            }
        }
    }

    private void handlePrintRecentEntries(MainMenu ui, Connection context) {
        String sql = """
                SELECT  entries.id, entries.notes, entries.duration, entries.created_at,
                        projects.title
                FROM    entries
                INNER   JOIN projects on projects.id = entries.parent_project_id
                ORDER   BY entries.created_at DESC
                LIMIT   5
                """;
        try {
            Statement stmt = context.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Notes: " + rs.getString("notes"));
                System.out.println("Duration: " + rs.getInt("duration"));
                System.out.println("Created At: " + rs.getTimestamp("created_at"));
                System.out.println("Parent Project: " + rs.getString("title"));
                System.out.println("------------++" +
                        "-+/----------------------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Bad things happened.");
        }
    }

//    private void handlePrintMostRecentEntry(MainMenu ui){
//        // Will eventually have logic to query most recent logic
//        ui.printMostRecentEntry(entry);
//    }

//    private boolean handleCreateEntry(MainMenu ui){
//        Scanner input = new Scanner(System.in);
//        System.out.print("> Enter Language: ");
//        String language = input.nextLine();
//
//        System.out.print("> Enter Notes: ");
//        String notes = input.nextLine();
//
//        System.out.print("> Enter Duration: ");
//        int duration = input.nextInt();
//
//        Entry newEntry = new Entry(
//                notes,
//                duration
//        );
//
//        return true;
//    }

//    private void handleSearchByLanguage(ArrayList<Entry> entries) {
//        Scanner input = new Scanner(System.in);
//        System.out.println("Enter Language to Search: ");
//        System.out.print("> ");
//        String targetLanguage = input.nextLine().toLowerCase();
//
//        ArrayList<Entry> targetEntries = new ArrayList<>();
//
////        for (Entry entry: entries) {
////            if (entry.language.toLowerCase().equals(targetLanguage)) {
////                targetEntries.add(entry);
////            }
////        }
//
////        System.out.println("Number of Entries Found: " + targetEntries.size());
////        for (Entry entry : targetEntries) {
////            System.out.println(entry.language + "---------" + entry.notes);
////        }
//    }
//
//


}
