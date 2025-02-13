// App mode driver classes
import appdriver.AppModeInterface;
import appdriver.InitializerMode;
import appdriver.InteractiveMode;

// Database Context
import appdriver.VersionCheckerMode;
import data.DatabaseContext;

// Data models
import data.models.Project;

import runtime.util.CliArgumentsParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner input = new Scanner(System.in);
        DatabaseContext db = new DatabaseContext();
        CliArgumentsParser cli = new CliArgumentsParser(args);
        AppModeInterface app;

        switch (cli.getAppMode()) {
            case UNKNOWN:
                System.out.println("Bad arguments.");
                return;
            case INTERACTIVE:
                app = new InteractiveMode(input, db);
                app.Run();
                break;
            case INITIALIZE:
                // We need to construct a project object to
                // pass to initializer. We could pass in
                // raw arguments, but that doesn't provide
                // as much resilience as just passing in
                // an object will null values
                Project project = cli.getInitArguments();
                app = new InitializerMode(project);
                app.Run();
                break;
            case CHECK_VERSION:
                app = new VersionCheckerMode();
                app.Run();
                break;
        }
    }
}

