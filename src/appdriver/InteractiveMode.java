package appdriver;

import data.dao.EntryDao;
import data.dao.ProjectDao;
import data.dao.ProjectFileDao;
import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;
import runtime.crypto.Hasher;
import runtime.util.FlareFolder;
import runtime.util.ProjectFileIterator;
import runtime.xml.XmlReader;
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


    public InteractiveMode(Scanner input,
                           ProjectDao projectDao,
                           EntryDao entryDao,
                           ProjectFileDao projectFileDao,
                           Connection context,
                           FlareOutput ui) {
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
            String rawUserInput = ui.emptyPrompt();
            String[] commands;
            try {
                commands = rawUserInput.split(" ");
            } catch (Exception e) {
                System.out.println("Bad command.");
                continue;
            }
            if (commands.length < 2) {
                System.out.println("Bad command.");
                continue;
            }
            switch (commands[0]) {
                case ".project":
                    getProjectCommand(commands);
                    break;
                case ".entries":
                    getEntriesCommand(commands);
                    break;
                case ".exit":
                    programActive = false;
                    break;
                default:
                    System.out.println("Bad command.");
            }
        }
    }

    /********************************
     PROJECT COMMANDS
     ********************************/
    private void getProjectCommand(String[] commands) {

        // if the first flag contains a number...
        if (commands[1].matches(".*\\d.*")) {

            // Break here if the command doesn't contain an additional
            // command, because we are operating on individual projects
            // and need an additional action
            if (commands.length < 3 || commands.length > 3) {
                printProjectProperUsage();
                return;
            }

            getIndividualProjectCommands(commands);

        } else {
            switch (commands[1]) {
                case "all":
                    getAllProjects();
                    break;
                case "recent":
                    getMostRecentProject();
                    break;
                case "oldest":
                    getOldestProject();
                    break;
                case "create":
                    createProject();
                    break;
                case "search":
                    if (commands.length != 4) {
                        System.out.println("Bad Search Parameters");
                    }
                    handleProjectSearch(commands);
                    break;
                case "urls":
                    getAllProjectUrls();
                    break;
                default:
                    System.out.println("Bad command");
            }
        }

    }

    private void getIndividualProjectCommands(String[] commands) {
        long id;
        try {
            id = Long.parseLong(commands[1]);
        } catch (Exception e) {
            System.out.println("Bad Command.");
            return;
        }
        switch (commands[2]) {
            case "get":
                getProjectById(id);
                break;
            case "update":
                updateProject(id);
                break;
            case "delete":
                deleteProjectById(id);
                break;
            case "entries":
                // get all entries
                break;
            case "url":
                getProjectUrl(id);
                break;

        }
    }


    /********************************
     ENTRY COMMANDS
     ********************************/
    private void getEntriesCommand(String[] commands) {
        // if the first flag contains a number...
        if (commands[1].matches(".*\\d.*")) {

            // Break here if the command doesn't contain an additional
            // command, because we are operating on individual projects
            // and need an additional action
            if (commands.length < 3) {
                printEntriesProperUsage();
                return;
            }

            getIndividualEntryCommands(commands);

        } else {
            switch (commands[1]) {
                case "recent":
                    getRecentEntries();
                    break;
                case "oldest":
                    getOlderEntries();
                    break;
                case "create":
                    createEntry(context);
                    break;
                case "search":
                    if (commands.length != 4) {
                        System.out.println("Bad Search Parameters");
                        break;
                    }
                    handleEntrySearch(commands);
                    break;
                default:
                    System.out.println("Bad Command.");
            }
        }
    }

    private void getIndividualEntryCommands(String[] commands) {
        long id;
        try {
            id = Long.parseLong(commands[1]);
        } catch (Exception e) {
            System.out.println("Bad Command.");
            return;
        }

        switch (commands[2]) {
            case "get":
                getEntryById(id);
                break;
            case "update":
                updateEntry(id);
                break;
            case "delete":
                deleteEntryById(id);
                break;
        }
    }


    /********************************
     PROPER USAGE FUNCTIONS
     ********************************/
    private void printProjectProperUsage() {
        System.out.println("Learn to use a command, nerd.");
    }

    private void printEntriesProperUsage() {
        System.out.println("Learn to use a command, nerd.");
    }


    /********************************
     PROJECTS -- GET FUNCTIONS
     ********************************/
    private void getAllProjects() {
        ui.printProjectList(projectDao.getAllProjects());
    }

    private void getAllProjectUrls() {
        ui.printProjectUrls(projectDao.getAllProjects());
        ui.pause();
    }

    private void getProjectById(long id) {
        ui.printProjectList(projectDao.getProjectById(id));
    }

    private void getProjectUrl(long id) {
        ui.printProjectUrls(projectDao.getProjectById(id));
    }

    private void getMostRecentProject() {
        ui.printProjectList(projectDao.getMostRecentProject());
    }

    private void getOldestProject() {
        ui.printProjectList(projectDao.getOldestProject());
    }


    /********************************
     PROJECTS - UPDATE FUNCTION
     ********************************/
    private void updateProject(long id) {
        Project newProject = new Project();
        newProject.title = ui.prompt("Enter Title (leave blank to keep original)");
        newProject.language = ui.prompt("Enter Language (leave blank to keep original)");
        newProject.description = ui.prompt("Enter Description (leave blank to keep original)");
        newProject.url = ui.prompt("Enter Url (leave blank to keep original)");
        if (projectDao.updateProject(id, newProject)) {
            ui.successWithMessage("Project Updated");
        } else {
            ui.failure();
        }

        ui.pause();
    }


    /********************************
     PROJECTS - SEARCH FUNCTIONS
     ********************************/
    private void handleProjectSearch(String[] commands) {

        String searchParameter = commands[2].toLowerCase();
        String searchTerm = commands[3];
        switch (searchParameter) {
            case "language":
                searchProjectByLanguage(searchTerm);
                break;
            case "title":
                searchProjectByTitle(searchTerm);
        }
    }

    private void searchProjectByLanguage(String searchTerm) {
        ArrayList<Project> projects = projectDao.searchByLanguage(searchTerm);
        ui.printProjectList(projects);
        ui.pause();
    }

    private void searchProjectByTitle(String searchTerm) {
        ArrayList<Project> projects = projectDao.searchByTitle(searchTerm);
        ui.printProjectList(projects);
        ui.pause();
    }


    /********************************
     PROJECTS - DELETE FUNCTION
     ********************************/
    private void deleteProjectById(long id) {
        if (projectDao.deleteProjectById(id)) {
            ui.success();
        } else {
            ui.failure();
        }
    }


    /********************************
     PROJECTS - CREATE FUNCTION
     ********************************/
    public void createProject() {
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

        ProjectFileIterator iter = new ProjectFileIterator();
        iter.getAllFiles(flareFolder.parentDirectoryAsFile(), fileList);
        ArrayList<ProjectFile> pfList = iter.constructProjectFilesFromFiles(fileList, newProject.id, initialEntry.id);
        iter.filterProjectFiles(pfList);

        for (ProjectFile pf : pfList){
            projectFileDao.createProjectFile(pf);
        }

        // Store directory information in .flare folder
        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(newProject, pfList, flareFolder.path().toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Flare Initialized.");
        ui.success();
        ui.pause();
    }


    /********************************
     ENTRIES - GET FUNCTIONS
     ********************************/
    private void getRecentEntries() {
        ui.printEntryList(entryDao.getRecentEntries());
    }

    private void getOlderEntries() {
        ui.printEntryList(entryDao.getOlderEntries());
    }

    private void getEntryById(long id) {
        ui.printEntryList(entryDao.getEntryById(id));
    }


    /********************************
     ENTRIES - CREATE FUNCTION
     ********************************/
    private void createEntry(Connection context) {
        String projectTitle = ui.prompt("Enter Project Title:");
        boolean projectFound = false;
        Project project = new Project();
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
                            project.title = rs.getString("title");
                            project.id = rs.getLong("id");
                            project.language = rs.getString("language");
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

        // Insert new entry (which returns the pk)
        newEntry.id = entryDao.createEntryAndReturnGeneratedKey(newEntry);

        if (newEntry.id == 0) {
            ui.failure();
        }

        // Step 1 - Load all files and hashes into memory from XML

        ArrayList<ProjectFile> lastUpdatedProjectFileList;
        XmlReader reader = new XmlReader(projectFilePath);
        try {
            lastUpdatedProjectFileList = reader.getProjectFilesFromXml();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("No project files added to this entry, but the entry was still added.");
            ui.failure();
            return;
        }

        // Step 2 - load all current files into memory from directory
        ArrayList<File> fileList = new ArrayList<>();
        ProjectFileIterator iter = new ProjectFileIterator();
        iter.getAllFiles(new File(projectFilePath), fileList);

        ArrayList<ProjectFile> currentProjectFileList = iter.constructProjectFilesFromFiles(fileList, projectId, newEntry.id);
        iter.filterProjectFiles(currentProjectFileList);

        ArrayList<ProjectFile> newXmlFileList = new ArrayList<>();
        ArrayList<ProjectFile> projectFilesToAttachToEntry = new ArrayList<>();
        // Step 4 -- Compare both lists
        // We will output the new Project file if there is a match or the old project file is there is none
        // We will also keep another copy of new project files so that they can be appended to the entry
        for (ProjectFile currentPf : currentProjectFileList) {
            boolean name_match = false;
            boolean hash_match = false;
            boolean file_type_match = false;

            for (ProjectFile oldPf : lastUpdatedProjectFileList) {

                if (currentPf.fileName.equals(oldPf.fileName)) {

                    name_match = true;

                    if (currentPf.fileType.equals(oldPf.fileType)) {

                        file_type_match = true;

                        if (currentPf.hash.equals(oldPf.hash)) {
                            // If we get to this point, a match has been found of a file
                            // that has not been updated. Break out of the loop.
                            hash_match = true;

                        }

                        break;
                    }
                }
            }
            // Both the name and the hash match, the file has not been updated
            if (name_match && hash_match && file_type_match) {
                newXmlFileList.add(currentPf);
            }

            // If we find a matching name and file type, but not hash, the file has been updated
            else if (name_match && file_type_match && !hash_match) {
                newXmlFileList.add(currentPf);
                projectFilesToAttachToEntry.add(currentPf);
            }

            // If the name is never matched, we can assume its a new file and needs
            // to be catalogued
            else if (!name_match) {
                newXmlFileList.add(currentPf);
                projectFilesToAttachToEntry.add(currentPf);
            }

            // Catch any project file that has fallen through, but
            // this should not happen.
            else {
                newXmlFileList.add(currentPf);
            }

        }

        for (ProjectFile file : projectFilesToAttachToEntry) {
            projectFileDao.createProjectFile(file);
        }

        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(project, newXmlFileList, projectFilePath + "\\.flare");
        } catch (Exception e) {
            System.out.println(".flare local XML datastore not updated. Please try again with 'flare update-local'");
            System.out.println("Your changed project files were still appended to the entry.");
        }

        ui.successWithMessage("Entry Added");
    }


    /********************************
     ENTRIES - DELETE FUNCTION
     ********************************/
    private void deleteEntryById(long id) {
        long entryId = Long.parseLong(ui.prompt("Enter Entry ID to delete (this is permanent):"));
        if (entryDao.deleteEntryById(entryId)) {
            ui.success();
        } else {
            ui.failure();
        }
    }


    /********************************
     ENTRIES - UPDATE FUNCTION
     ********************************/
    private void updateEntry(long id) {
        Entry newEntry = new Entry();
        newEntry.notes = ui.prompt("Enter New Note (leave blank to keep original):");
        String durationAsString = ui.prompt("Enter Duration (leave blank to keep original):");
        if (!durationAsString.isEmpty()) {
            newEntry.duration = Integer.parseInt(durationAsString);
        } else {
            newEntry.duration = 0;
        }
        if (entryDao.updateEntry(id, newEntry)) {
            ui.success();
        } else {
            ui.failure();
        }

    }


    /********************************
     ENTRIES - SEARCH FUNCTIONS
     ********************************/
    private void handleEntrySearch(String[] commands) {
        String searchParameter = commands[2].toLowerCase();
        String searchTerm = commands[3];
        switch (searchParameter) {
            case "note":
                searchEntriesByNote(searchTerm);
                break;
            case "project.title":
                searchEntriesByProjectTitle(searchTerm);
                break;
            case "project.language":
                searchEntriesByProjectLanguage(searchTerm);
                break;
            case "projectfile.filename":
                searchEntriesByProjectFileName(searchTerm);
                break;
        }
    }

    private void searchEntriesByNote(String searchTerm) {
        ui.printEntryList(entryDao.searchByNote(searchTerm));
    }

    private void searchEntriesByProjectTitle(String searchTerm) {
        ui.printEntryList(entryDao.searchByProjectTitle(searchTerm));
    }

    private void searchEntriesByProjectLanguage(String searchTerm) {
        ui.printEntryList(entryDao.searchByProjectLanguage(searchTerm));
    }

    private void searchEntriesByProjectFileName(String searchTerm) {
    }


    private void getAllFiles(File directory, ArrayList<File> filesContainer) {
        if (directory.getName().equals(".git")) {
            return;
        }
        if (directory.getName().equals(".flare")) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllFiles(file, filesContainer);
                    continue;
                }
                filesContainer.add(file);
            }
        }
    }
}
