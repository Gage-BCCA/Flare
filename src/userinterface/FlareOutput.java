package userinterface;

import data.models.Entry;
import data.models.Project;

import java.util.ArrayList;
import java.util.Scanner;

public class FlareOutput {

    private final Scanner input = new Scanner(System.in);

    public void success() {
        System.out.println(">>> Action SUCCESS <<<\n\n");
    }

    public void sucessWithMessage(String message) {
        System.out.printf(">>> Action SUCCESS: %s <<<%n%n", message);
    }

    public void failure() {
        System.out.println("> Action FAILURE <");
    }

    public void failureWithMessage(String message) {
        System.out.printf("> Action FAILURE: %s <%n", message);
    }

    public void pause() {
        System.out.println("Press Enter to continue...");
        input.nextLine();
    }

    public String prompt(String prompt) {
        System.out.println(prompt);
        System.out.print(">");
        return input.nextLine();
    }

    public void printProjectList(ArrayList<Project> projects) {
        for (Project project : projects) {
            System.out.printf(
                    "%s --- %s --- %s --- %s%n",
                    project.id,
                    project.title,
                    project.language,
                    project.description);
        }
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
        for (Entry entry : entries) {
            System.out.printf(
                    "%s --- %s --- %s --- %s%n",
                    entry.id,
                    entry.notes,
                    entry.duration,
                    entry.parentProjectName);
        }
    }
}
