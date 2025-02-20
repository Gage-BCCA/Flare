// App mode driver classes
import appdriver.AppModeInterface;
import appdriver.InitializerMode;
import appdriver.InteractiveMode;

import appdriver.VersionCheckerMode;

// Data models
import data.dao.EntryDao;
import data.dao.ProjectDao;
import data.dao.ProjectFileDao;
import data.models.Project;

import runtime.util.CliArgumentsParser;
import userinterface.FlareOutput;

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

        // Construct Dao objects to pass to app drivers
        ProjectDao projectDao = new ProjectDao();
        EntryDao entryDao = new EntryDao();
        ProjectFileDao projectFileDao = new ProjectFileDao();

        if (context != null) {
            projectDao.setConnection(context);
            entryDao.setConnection(context);
            projectFileDao.setConnection(context);
        }

        FlareOutput ui = new FlareOutput();

        switch (cli.getAppMode()) {
            case UNKNOWN:
                System.out.println("Bad arguments.");
                return;
            case INTERACTIVE:
                app = new InteractiveMode(input, projectDao, entryDao, projectFileDao, context, ui);
                app.Run();
                break;
            case INITIALIZE:
                // We need to construct a project object to
                // pass to initializer. We could pass in
                // raw arguments, but that doesn't provide
                // as much resilience as just passing in
                // an object will null values
                Project project = cli.getInitArguments();
                app = new InitializerMode(project, projectDao, entryDao, projectFileDao);
                app.Run();
                break;
            case CHECK_VERSION:
                app = new VersionCheckerMode();
                app.Run();
                break;
        }
    }
}

