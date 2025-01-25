import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserInterface ui = new UserInterface();
        Scanner input = new Scanner(System.in);

        Entry fakeEntry = new Entry(
                "C#",
                "C# is better than Java",
                new Date(),
                new Date(2010, 1, 3)
        );
        ui.printLogo();

        boolean programActive = true;
        while (programActive) {
            ui.printMainMenu();
            String option = input.nextLine();
            switch (option) {
                case "1":
                    handlePrintAllEntries(ui);
                    break;
                case "2":
                    handlePrintMostRecentEntry(ui, fakeEntry);
                    break;
                case "3":
                    handleCreateEntry(ui);
                case "0":
                    programActive = false;
            }

        }
    }

    public static void handlePrintAllEntries(UserInterface ui) {
        ui.printEntries();
    }

    public static void handlePrintMostRecentEntry(UserInterface ui, Entry entry){
        ui.printMostRecentEntry(entry);
    }

    public static void handleCreateEntry(UserInterface ui){
        return;
    }

}
