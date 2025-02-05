package runtime.util;

import runtime.AppModeEnum;
import data.models.Project;

public class CliArgumentsParser {

    private final String[] arguments;

    public CliArgumentsParser(String[] args) {
        this.arguments = args;
    }

    public AppModeEnum getAppMode() {

        // If there are no arguments, we're
        // running the app in interactive mode
        if (arguments.length == 0) {
            return AppModeEnum.INTERACTIVE;
        }

        return switch (arguments[0]) {
            case "addnote" -> AppModeEnum.QUICK_INSERT;
            case "init" -> AppModeEnum.INITIALIZE;
            case "version" -> AppModeEnum.CHECK_VERSION;
            default -> AppModeEnum.UNKNOWN;
        };
    }

    public Project getInitArguments(String[] args) {
        Project project = new Project();
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--project":
                    project.title = args[i + 1];
                    break;
                case "--language":
                    project.language = args[i + 1];
                    break;
                case "--description":
                    project.desc = args[i + 1];
                    break;
                case "--url":
                    project.url = args[i + 1];
                    break;
            }
        }
        return project;
    }

}
