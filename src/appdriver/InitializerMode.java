package appdriver;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;


import data.dao.EntryDao;
import data.dao.ProjectDao;
import data.dao.ProjectFileDao;
import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;

import runtime.crypto.Hasher;
import runtime.util.FlareFolder;
import runtime.xml.XmlWriter;

public class InitializerMode implements AppModeInterface{

    private final FlareFolder flareFolder;
    private final Project project;

    private final ProjectDao projectDao;
    private final EntryDao entryDao;
    private final ProjectFileDao projectFileDao;


    public InitializerMode(Project providedProject, ProjectDao projectDao, EntryDao entryDao, ProjectFileDao projectFileDao)
    {
        // Get the directory that the program is being executed from
        File originDirectory = new File(System.getProperty("user.dir"));

        this.flareFolder = new FlareFolder(originDirectory);

        this.project = providedProject;
        project.localFileDir = originDirectory.getName();

        this.projectDao = projectDao;
        this.entryDao = entryDao;
        this.projectFileDao = projectFileDao;
    }

    public void Run() {

        if (flareFolder.exists) {
            System.out.println("Project already has a Flare Repo");
            return;
        }

        // Create a .flare folder
        flareFolder.init();



        // Create initial entry for project and assign the generated ID to it
        Entry initialEntry = new Entry(
                "Flare Initialized.",
                0,
                project.id
        );
        initialEntry.id = entryDao.createEntryAndReturnGeneratedKey(initialEntry);

        // Iterate and store all files
        ArrayList<File> fileList = new ArrayList<>();
        getAllFiles(flareFolder.parentDirectoryAsFile(), fileList);

        // Construct and store all Project File objects
        ArrayList<ProjectFile> projectFileList = new ArrayList<>();
        for (File file : fileList) {
            ProjectFile pf = new ProjectFile();
            pf.fileName = file.getName();
            pf.parentProjectId = project.id;
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
                System.out.println(e.getMessage());
                pf.hash = null;
            }

            projectFileList.add(pf);
            projectFileDao.createProjectFile(pf);
        }

        // Store directory information in .flare folder
        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(project, projectFileList, flareFolder.path().toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Flare Initialized.");

    }

    private void getAllFiles(File directory, ArrayList<File> filesContainer) {
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
