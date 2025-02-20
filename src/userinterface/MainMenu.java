package userinterface;

import data.models.Entry;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private final Scanner input;
    final private String logo = """
             _______  __          ___      .______       _______  _______  .______  \s
            |   ____||  |        /   \\     |   _  \\     |   ____||       \\ |   _  \\ \s
            |  |__   |  |       /  ^  \\    |  |_)  |    |  |__   |  .--.  ||  |_)  |\s
            |   __|  |  |      /  /_\\  \\   |      /     |   __|  |  |  |  ||   _  < \s
            |  |     |  `----./  _____  \\  |  |\\  \\----.|  |____ |  '--'  ||  |_)  |\s
            |__|     |_______/__/     \\__\\ | _| `._____||_______||_______/ |______/ \s
                                                                                    \s
            Track your time spent coding.
            """;

    public MainMenu(Scanner input) {
        this.input = input;
    }

    public void printLogo() {
        System.out.println(logo);
    }

    public void printMainMenu() {
        System.out.println("============================================");
        System.out.println("|               MAIN MENU                   |");
        System.out.println("| 1 - Show Most Recent Entries              |");
        System.out.println("| 2 - Show All Project Urls                 |");
        System.out.println("| 3 - Create an entry                       |");
        System.out.println("| 4 - Create a Project                      |");
        System.out.println("| 5 - Search for Projects By Language       |");
        System.out.println("| 6 - Search for Entries By Project Title   |");
        System.out.println("| 7 - Delete an Entry by ID                 |");
        System.out.println("| 8 - Delete a Project by ID                |");
        System.out.println("| 0 - Exit Program                          |");
        System.out.println("============================================");
        System.out.print("> ");
    }

    public void printEntries(ArrayList<Entry> entries) {
        for (Entry entry : entries) {
            //System.out.println(entry.language + " ---- " + entry.notes);
        }
    }

    public void printMostRecentEntry(Entry entry) {
        //System.out.println(entry.language + " ---- " + entry.notes);
    }
}
