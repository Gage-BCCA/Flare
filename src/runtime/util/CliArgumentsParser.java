package runtime.util;

import runtime.AppModeEnum;
import data.models.Project;

import java.util.ArrayList;

public class CliArgumentsParser {

    private final ArrayList<String> arguments;

    public CliArgumentsParser(String[] args) {
        arguments = new ArrayList<>();
        for (String arg : args) {
            arguments.add(arg);
        }
    }

    public AppModeEnum getAppMode() {

        // If there are no arguments, we're
        // running the app in interactive mode
        if (arguments.isEmpty()) {
            return AppModeEnum.INTERACTIVE;
        }
        switch (arguments.getFirst()) {
            case "addnote":
                return AppModeEnum.QUICK_INSERT;
            case "init":
                return AppModeEnum.INITIALIZE;
            case "version":
                return AppModeEnum.CHECK_VERSION;
            default:
                return AppModeEnum.UNKNOWN;
        }
    }

    public Project getInitArguments() {
        Project project = new Project();
        for (int i = 1; i < arguments.size(); i++) {
            switch (arguments.get(i)) {
                case "--project":
                    project.title = arguments.get(i + 1);
                    continue;
                case "--language":
                    project.language = arguments.get(i + 1);
                    continue;
                case "--description":
                    project.description = arguments.get(i + 1);
                    continue;
                case "--url":
                    project.url = arguments.get(i + 1);
                    continue;
            }

        }
        return project;
    }

}
