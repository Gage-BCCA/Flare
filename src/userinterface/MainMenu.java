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
            It's like git, but _way_ worse.
            """;

    public MainMenu(Scanner input) {
        this.input = input;
    }

    public void printLogo() {
        System.out.println(logo);
    }

}
