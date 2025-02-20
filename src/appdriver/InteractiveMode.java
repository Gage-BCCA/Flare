package appdriver;

import data.models.Entry;
import data.models.Project;
import userinterface.MainMenu;

import java.sql.*;
import java.util.Scanner;

public class InteractiveMode implements AppModeInterface {

    private final Scanner input;
    private final Connection context;
    private boolean programActive;

    public InteractiveMode(Scanner input, Connection db) {
        this.input = input;
        this.context = db;
        this.programActive = true;
    }

    public void Run() {
        MainMenu ui = new MainMenu(input);
        ui.printLogo();
        while (programActive) {
            ui.printMainMenu();
            String option = input.nextLine();
            switch (option) {
                case "1":
                    handlePrintRecentEntries(context);
                    break;
                case "2":
                    handlePrintAllProjectUrls(context);
                    break;
                case "3":
                    handleCreateEntry(context);
                    break;
                case "4":
                    handleCreateProject(context);
                    break;
                case "5":
                    handleSearchByLanguage(context);
                    break;
                case "6":
                    handleSearchEntriesByProjectTitle(context);
                    break;
                case "7":
                    handleDeleteEntryById(context);
                    break;
                case "8":
                    handleDeleteProjectById(context);
                    break;
                case "0":
                    programActive = false;
            }
        }
    }

    private void handlePrintRecentEntries(Connection context) {
        String sql = """
                SELECT  entries.id, entries.notes, entries.duration, entries.created_at,
                        projects.title
                FROM    entries
                INNER   JOIN projects on projects.id = entries.parent_project_id
                ORDER   BY entries.created_at DESC
                LIMIT   5
                """;
        try {
            Statement stmt = context.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Notes: " + rs.getString("notes"));
                System.out.println("Duration: " + rs.getInt("duration"));
                System.out.println("Created At: " + rs.getTimestamp("created_at"));
                System.out.println("Parent Project: " + rs.getString("title"));
                System.out.println("--------------" +
                        "-------------------------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Bad things happened.");
        }
    }

//    private void handlePrintMostRecentEntry(MainMenu ui){
//        // Will eventually have logic to query most recent logic
//        ui.printMostRecentEntry(entry);
//    }

    private void handleCreateEntry(Connection context){
        Scanner input = new Scanner(System.in);

        System.out.println("> Enter Project Title: ");
        String projectTitle = input.nextLine();
        boolean projectFound = false;
        long projectId = 0;
        try {
            String sql = """
                SELECT *
                FROM projects
                WHERE LOWER(title) = LOWER(?)
                """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setString(1, projectTitle);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String foundProjectTitle = rs.getString("title");
                System.out.println("Found: " + foundProjectTitle);
                System.out.println("Is this correct? [y/n]");


                boolean correctUserInputReceived = false;
                while (!correctUserInputReceived) {
                    String userInput = input.nextLine();
                    switch (userInput.toLowerCase()) {
                        case "y":
                            projectFound = true;
                            correctUserInputReceived = true;
                            break;
                        case "n":
                            correctUserInputReceived = true;
                            break;
                        default:
                            break;
                    }
                }
                if (projectFound) {
                    projectId = rs.getLong("id");
                    break;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        // If the project wasn't found, we have to exit.
        if (!projectFound) {
            System.out.println("Parent project not found. Aborting...");
            return;
        }

        System.out.print("> Enter Notes: ");
        String notes = input.nextLine();

        System.out.print("> Enter Duration: ");
        int duration = input.nextInt();

        Entry newEntry = new Entry(
                notes,
                duration,
                projectId
        );



        // Insert new entry
        try {
            String sql = """
                    INSERT INTO entries (notes, duration, parent_project_id)
                    VALUES  (?, ?, ?)
                    """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setString(1, newEntry.notes);
            pstmt.setInt(2, newEntry.duration);
            pstmt.setLong(3, newEntry.parentProjectId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Entry added...");
            } else {
                throw new RuntimeException("Error inserting entry. Entry not recorded.");
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }

        // TODO: GET MODIFIED FILES
        // TODO: UPDATE HASHES IN FLARE FOLDER

        return;
    }

    private void handleSearchByLanguage(Connection context) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Language to Search: ");
        System.out.print("> ");
        String targetLanguage = input.nextLine().toLowerCase();

        try {
            String sql = """
                SELECT *
                FROM projects
                WHERE LOWER(language) = LOWER(?)
                """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setString(1, targetLanguage);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                long projectId = rs.getLong("id");
                String projectTitle = rs.getString("title");
                String projectLanguage = rs.getString("language");
                String projectDescription = rs.getString("description");
                System.out.printf(
                        "%s --- %s --- %s --- %s%n",
                        projectId,
                        projectTitle,
                        projectLanguage,
                        projectDescription);
                System.out.println("=====================================================");
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteEntryById(Connection context) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Entry ID to delete (this is permanent): ");
        System.out.print("> ");
        long targetEntryId = input.nextLong();

        try {
            String sql = """
                    DELETE FROM entries
                    WHERE id = ?
                    """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setLong(1, targetEntryId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Entry deleted...");
                System.out.println("==============================================");
            } else {
                throw new RuntimeException("Something went wrong deleting an entry. Please try again.");
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteProjectById(Connection context) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Project ID to delete (this is permanent): ");
        System.out.print("> ");
        long targetProjectId = input.nextLong();

        try {
            String sql = """
                    DELETE FROM projects
                    WHERE id = ?
                    """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setLong(1, targetProjectId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Project deleted...");
                System.out.println("==============================================");
            } else {
                throw new RuntimeException("Something went wrong deleting a project. Please try again.");
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleCreateProject(Connection context) {
        Scanner input = new Scanner(System.in);
        Project newProject = new Project();

        System.out.println("> Enter full file path to root directory (C:\\\\projects\\\\myproject): ");
        newProject.localFileDir = input.nextLine();

        // TODO: DO ALL THE INITIALIZER STUFF

        System.out.println("> Enter project title: ");
        newProject.title = input.nextLine();

        System.out.println("> Enter primary project language: ");
        newProject.language = input.nextLine();

        System.out.println("> Enter a short description: ");
        newProject.description = input.nextLine();

        System.out.println("> Enter project URL (github, personal site, etc) (leave blank for none): ");
        newProject.url = input.nextLine();

        try {
            String sql = """
                    INSERT INTO projects (title, language, description, local_file_dir, url)
                    VALUES (?,?,?,?,?)
                    """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setString(1, newProject.title);
            pstmt.setString(2, newProject.language);
            pstmt.setString(3, newProject.description);
            pstmt.setString(4, newProject.localFileDir);
            pstmt.setString(5, newProject.url);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Project created...");
                System.out.println("==============================================");
            } else {
                throw new RuntimeException("Fatal Error: Could not create project. Please try again.");
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    private void handlePrintAllProjectUrls(Connection context) {
        try {
            String sql = """
                    SELECT title, url
                    FROM projects
                    """;
            Statement stmt = context.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String projectTitle = rs.getString("title");
                String projectUrl = rs.getString("url");
                System.out.printf("%s ----- %s%n", projectTitle, projectUrl);
            }

            System.out.println("=====================================================");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleSearchEntriesByProjectTitle(Connection context) {
        Scanner input = new Scanner(System.in);

        System.out.println("> Enter Project Title: ");
        String targetProjectTitle = input.nextLine();

        try {
            String sql = """
                    SELECT projects.id, projects.title, entries.id, entries.notes, entries.duration, entries.created_at
                    FROM   projects
                    INNER JOIN entries ON projects.id = entries.parent_project_id
                    WHERE LOWER(projects.title) = LOWER(?)
                    """;
            PreparedStatement pstmt = context.prepareStatement(sql);
            pstmt.setString(1, targetProjectTitle);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                System.out.printf(
                        "Project ID: %s%n%s%n%s%n%s%n%s%n",
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getInt("duration"),
                        rs.getDate("created_at"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
