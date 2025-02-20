// App mode driver classes
import appdriver.AppModeInterface;
import appdriver.InitializerMode;
import appdriver.InteractiveMode;

import appdriver.VersionCheckerMode;

// Data models
import data.models.Project;

import runtime.util.CliArgumentsParser;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) {


        Scanner input = new Scanner(System.in);
        CliArgumentsParser cli = new CliArgumentsParser(args);
        AppModeInterface app;
        Connection context = null;
        try {
            Class.forName("org.postgresql.Driver");
            context = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/flaredb",
                            "postgres", "password");
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+ ": " + e.getMessage());
            System.exit(0);
        }

        switch (cli.getAppMode()) {
            case UNKNOWN:
                System.out.println("Bad arguments.");
                return;
            case INTERACTIVE:
                app = new InteractiveMode(input, context);
                app.Run();
                break;
            case INITIALIZE:
                // We need to construct a project object to
                // pass to initializer. We could pass in
                // raw arguments, but that doesn't provide
                // as much resilience as just passing in
                // an object will null values
                Project project = cli.getInitArguments();
                app = new InitializerMode(project, context);
                app.Run();
                break;
            case CHECK_VERSION:
                app = new VersionCheckerMode();
                app.Run();
                break;
        }
    }
}

