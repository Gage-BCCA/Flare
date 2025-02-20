package appdriver;

import data.dao.EntryDao;
import data.dao.ProjectDao;
import data.dao.ProjectFileDao;
import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;
import runtime.crypto.Hasher;
import runtime.util.FlareFolder;
import runtime.xml.XmlWriter;
import userinterface.FlareOutput;
import userinterface.MainMenu;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InteractiveMode implements AppModeInterface {

    private final Scanner input;
    private final FlareOutput ui;
    private final Connection context;
    private final ProjectDao projectDao;
    private final EntryDao entryDao;
    private final ProjectFileDao projectFileDao;
    private boolean programActive;


    public InteractiveMode(Scanner input, ProjectDao projectDao, EntryDao entryDao, ProjectFileDao projectFileDao, Connection context, FlareOutput ui) {
        this.input = input;
        this.ui = ui;
        this.projectDao = projectDao;
        this.entryDao = entryDao;
        this.projectFileDao = projectFileDao;
        this.context = context;
        this.programActive = true;
    }

    public void Run() {
        MainMenu mainMenu = new MainMenu(input);
        mainMenu.printLogo();
        while (programActive) {
            mainMenu.printMainMenu();
            String option = input.nextLine();
            switch (option) {
                case "1":
                    handlePrintRecentEntries();
                    break;
                case "2":
                    handlePrintAllProjectUrls();
                    break;
                case "3":
                    handleCreateEntry(context);
                    break;
                case "4":
                    handleCreateProject();
                    break;
                case "5":
                    handleSearchByLanguage();
                    break;
                case "6":
                    handleSearchEntriesByProjectTitle();
                    break;
                case "7":
                    handleDeleteEntryById();
                    break;
                case "8":
                    handleDeleteProjectById();
                    break;
                case "0":
                    programActive = false;
            }
        }
    }

    private void handlePrintRecentEntries() {
        ui.printEntryList(entryDao.getRecentEntries());
        ui.pause();
    }

    private void handleCreateEntry(Connection context){
        String projectTitle = ui.prompt("Enter Project Title:");
        boolean projectFound = false;
        long projectId = 0;
        String projectFilePath = "";
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
                            projectFilePath = rs.getString("local_file_dir");
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

        String notes = ui.prompt("Enter Notes:");
        int duration = Integer.parseInt(ui.prompt("Enter Duration:"));

        Entry newEntry = new Entry(
                notes,
                duration,
                projectId
        );

        // Insert new entry
        if (entryDao.createEntry(newEntry)) {
            ui.success();
        } else {
            ui.failure();
        }

        ui.pause();

        // TODO: GET MODIFIED FILES
        // TODO: UPDATE HASHES IN FLARE FOLDER

        File projectDir = new File(projectFilePath);
        //projectDir.listFiles();

    }

    private void handleSearchByLanguage() {
        String targetLanguage = ui.prompt("Enter Language to Search: ");
        ArrayList<Project> projects = projectDao.searchByLanguage(targetLanguage);
        ui.printProjectList(projects);
        ui.success();
        ui.pause();
    }

    private void handleDeleteEntryById() {
        long entryId = Long.parseLong(ui.prompt("Enter Entry ID to delete (this is permanent):"));
        if (entryDao.deleteEntryById(entryId)) {
            ui.success();
        } else {
            ui.failure();
        }

        ui.pause();

    }

    private void handleDeleteProjectById() {
        long targetProjectId = Long.parseLong(ui.prompt("Enter Project ID to delete (this is permanent):"));
        if (projectDao.deleteProjectById(targetProjectId)) {
            ui.success();
        } else {
            ui.failure();
        }

        ui.pause();
    }

    public void handleCreateProject() {
        Project newProject = new Project();
        newProject.localFileDir = ui.prompt("Enter full file path to root directory (C:\\\\projects\\\\myproject): ");



        newProject.title = ui.prompt("Enter project title: ");
        newProject.language = ui.prompt("Enter primary project language:");
        newProject.description = ui.prompt("Enter a short description:");
        newProject.url = ui.prompt("Enter project URL (github, personal site, etc) (leave blank for none): ");
        newProject.id = projectDao.createProjectAndReturnGeneratedKey(newProject);

        Entry initialEntry = new Entry(
                "Flare Initialized.",
                0,
                newProject.id
        );
        initialEntry.id = entryDao.createEntryAndReturnGeneratedKey(initialEntry);

        // TODO: DO ALL THE INITIALIZER STUFF
        File dir = new File(newProject.localFileDir);
        if (!dir.exists()) {
            ui.failure();
            return;
        }

        FlareFolder flareFolder = new FlareFolder(dir);
        if (flareFolder.exists) {
            ui.failureWithMessage("Project already created.");
            return;
        }

        flareFolder.init();

        // Iterate and store all files
        ArrayList<File> fileList = new ArrayList<>();
        getAllFiles(flareFolder.parentDirectoryAsFile(), fileList);

        // Construct and store all Project File objects
        ArrayList<ProjectFile> projectFileList = new ArrayList<>();
        for (File file : fileList) {
            ProjectFile pf = new ProjectFile();
            pf.fileName = file.getName();
            pf.parentProjectId = newProject.id;
            pf.parentEntryId = initialEntry.id;

            // Manipulate the file name to get file type
            String extension = "";
            int i = file.getAbsolutePath().lastIndexOf('.');
            if (i > 0) {
                extension = file.getAbsolutePath().substring(i+1);
                pf.fileType = extension;
            } else {
                pf.fileType = null;
            }

            // Attempt to find the hash value
            try {
                pf.hash = (new Hasher(file).getSha256AsString());
            } catch (Exception e) {
                pf.hash = null;
            }

            projectFileList.add(pf);
            projectFileDao.createProjectFile(pf);
        }
        // Store directory information in .flare folder
        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(newProject, projectFileList, flareFolder.path().toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Flare Initialized.");
        ui.success();
        ui.pause();
    }

    private void handlePrintAllProjectUrls() {
        ui.printProjectUrls(projectDao.getAllProjects());
        ui.pause();
    }

    private void handleSearchEntriesByProjectTitle() {
        String targetProjectTitle = ui.prompt("Enter Project Title:");
        ui.printProjectList(projectDao.searchByTitle(targetProjectTitle));
        ui.pause();
    }

    private void getAllFiles(File directory, ArrayList<File> filesContainer) {
        if (directory.getName().equals(".git")) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for(File file : files) {
                if (file.isDirectory()) {
                    getAllFiles(file, filesContainer);
                    continue;
                }
                filesContainer.add(file);
            }
        }
    }
}
