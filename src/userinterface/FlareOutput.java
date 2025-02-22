package userinterface;

import data.models.Entry;
import data.models.Project;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FlareOutput {

    private final Scanner input = new Scanner(System.in);

    public void success() {
        System.out.println("\n█ Action SUCCESS █\n");
    }

    public void successWithMessage(String message) {
        System.out.printf(">>> Action SUCCESS: %s <<<%n", message);
    }

    public void failure() {
        System.out.println("> Action FAILURE <");
    }

    public void failureWithMessage(String message) {
        System.out.printf("> Action FAILURE: %s <%n", message);
    }

    public void pause() {
        System.out.println();
        System.out.println();
        System.out.print("Press any key to continue...");
        input.nextLine();
    }

    public String prompt(String prompt) {
        System.out.printf("%s >", prompt);
        return input.nextLine();
    }

    public String emptyPrompt() {
        System.out.print("> ");
        return input.nextLine();
    }

    public void printProjectList(ArrayList<Project> projects) {
        System.out.println();
        if (projects.isEmpty()) {
            System.out.println("None found.");
            return;
        }
        System.out.printf(
                "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                "%-40s" + "│" + " ".repeat(1) +
                "%-20s"  + "│" + " ".repeat(1) +
                "%-100s" +
                "%n" ,
                "ID",
                "Title",
                "Language",
                "Description"
        );
        System.out.println("━".repeat(160));
        for (Project project : projects) {
            System.out.printf(
                    "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                    "%-40s" + "│" + " ".repeat(1) +
                    "%-20s"  + "│" + " ".repeat(1) +
                    "%-100s" + "│" + " ".repeat(1) +
                    "%n" ,
                    project.id,
                    project.title,
                    project.language,
                    project.description);
        }
        System.out.println();
    }

    public void printProjectList(ArrayList<Project> projects, String title) {
        int titleWidth = title.length();
        System.out.println();
        System.out.println("━".repeat(titleWidth + 6));
        System.out.printf("┇" + " ".repeat(3) +  "%s"  + " " .repeat(3) + "┇%n", title);
        System.out.println("━".repeat(titleWidth + 6));
        System.out.println();
        System.out.println();
        if (projects.isEmpty()) {
            System.out.println("None found.");
        }
        System.out.printf(
                "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                        "%-40s" + "│" + " ".repeat(1) +
                        "%-20s"  + "│" + " ".repeat(1) +
                        "%-100s" +
                        "%n" ,
                "ID",
                "Title",
                "Language",
                "Description"
        );
        System.out.println("━".repeat(160));
        for (Project project : projects) {
            System.out.printf(
                    "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                            "%-40s" + "│" + " ".repeat(1) +
                            "%-20s"  + "│" + " ".repeat(1) +
                            "%-100s" + "│" + " ".repeat(1) +
                            "%n" ,
                    project.id,
                    project.title,
                    project.language,
                    project.description);
        }
        System.out.println();
    }

    public void printProjectUrls(ArrayList<Project> projects) {
        for (Project project : projects) {
            System.out.printf(
                    "%s --- %s%n",
                    project.title,
                    project.url
            );
        }
    }

    public void printEntryList(ArrayList<Entry> entries) {
        System.out.printf(
                "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                "%-80s" + "│" + " ".repeat(1) +
                "%-10s"  + "│" + " ".repeat(1) +
                "%-30s" +
                "%n" ,
                "ID",
                "Notes",
                "Duration",
                "Project"
        );
        System.out.println("━".repeat(140));
        for (Entry entry : entries) {
            System.out.printf(
                    "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                    "%-80s" + "│" + " ".repeat(1) +
                    "%-10s"  + "│" + " ".repeat(1) +
                    "%-30s" + "│" + " ".repeat(1) +
                     "%n" ,
                    entry.id,
                    entry.notes,
                    entry.duration,
                    entry.parentProjectName);
        }
    }

    public void printEntryListWithDetails(ArrayList<Entry> entries) {
        System.out.printf(
                "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                        "%-80s" + "│" + " ".repeat(1) +
                        "%-5s"  + "│" + " ".repeat(1) +
                        "%-25s" + "│" + " ".repeat(1) +
                        "%-80s" +
                        "%n" ,
                "ID",
                "Notes",
                "Dur.",
                "Project",
                "Files"
        );
        System.out.println("━".repeat(140));
        for (Entry entry : entries) {
            System.out.printf(
                    "%-5s" + " ".repeat(5) + "│" + " ".repeat(2) +
                    "%-80s" + "│" + " ".repeat(1) +
                    "%-5s"  + "│" + " ".repeat(1) +
                    "%-25s" + "│" + " ".repeat(1) +
                    "%-80s"+ "│" + " ".repeat(1) +
                    "%n" ,
                    entry.id,
                    entry.notes,
                    entry.duration,
                    entry.parentProjectName,
                    (entry.modifiedFiles == null) ? "None" : (entry.modifiedFiles.size() > 5) ? "%s Files".formatted(entry.modifiedFiles.size()) : entry.modifiedFiles.toString());
        }
    }
}
