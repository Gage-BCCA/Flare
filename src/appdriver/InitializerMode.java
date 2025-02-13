package appdriver;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;

import runtime.crypto.Hasher;
import runtime.xml.XmlWriter;

public class InitializerMode implements AppModeInterface{
    private final String initFolderName = "\\.flare";

    private final File originDirectory;

    private final Path initFolder;

    private final String projectName;
    private final String projectDescription;
    private final String projectLanguage;
    private final String projectUrl;
    private final String projectDir;


    public InitializerMode(Project providedProject)
    {
        // Get the directory that the program is being executed from
        this.originDirectory = new File(System.getProperty("user.dir"));

        // Construct the initialization folder path
        this.initFolder = Paths.get(originDirectory + initFolderName);

        // Construct a project with the provided project from Main
        // This will be filled out later in Run()
        this.projectName = providedProject.title;
        this.projectDescription = providedProject.desc;
        this.projectLanguage = providedProject.language;
        this.projectUrl = providedProject.url;
        this.projectDir = originDirectory.getName();
    }

    public void Run() {

        // Check if Folder is already initialized
        if (checkIfFolderAlreadyInitialized()) {
            System.out.println("Project already has Flare repo.");
            return;
        }

        // Create a .flare folder
        try {
            Files.createDirectory(initFolder);
        } catch (IOException e) {
            System.out.println("Fatal Error: " + e.getMessage());
        }

        // Create Project object
        Project project = new Project();
        project.title = projectName;
        project.desc = projectDescription;
        project.language = projectLanguage;
        project.localFileDir = projectDir;
        project.url = projectUrl;

        // Insert into Database and get Project ID for Entry
        // and project files to be inserted as well

        // Iterate and store all files
        ArrayList<File> fileList = new ArrayList<>();
        getAllFiles(originDirectory, fileList);

        // Construct and store all Project File objects
        ArrayList<ProjectFile> projectFileList = new ArrayList<>();
        for (File file : fileList) {

            ProjectFile pf = new ProjectFile();

            pf.fileName = file.getName();

            // To reference the Project with a Foreign Key
            pf.parentProject = project.id;

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
                System.out.println(e.getMessage());
                pf.hash = null;
            }

            projectFileList.add(pf);
        }

        // Create initial entry for project
        Entry initialEntry = new Entry(
                "Flare Initialized.",
                0
        );

        // Foreign Key reference to Project
        initialEntry.id = project.id;

        // Store directory information in .flare folder
        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(project, projectFileList, initFolder.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Flare Initialized.");

    }


    private boolean checkIfFolderAlreadyInitialized() {
        return Files.exists(initFolder);
    }

    private void getAllFiles(File directory, ArrayList<File> filesContainer) {
        File[] files = directory.listFiles();
        for(File file : files) {
            if (file.isDirectory()) {
                getAllFiles(file, filesContainer);
                continue;
            }
            filesContainer.add(file);
        }
    }
}
