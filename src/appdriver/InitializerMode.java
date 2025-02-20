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


import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;

import runtime.crypto.Hasher;
import runtime.util.FlareFolder;
import runtime.xml.XmlWriter;

public class InitializerMode implements AppModeInterface{

    private final FlareFolder flareFolder;
    private final Project project;
    private final Connection context;


    public InitializerMode(Project providedProject, Connection context)
    {
        // Get the directory that the program is being executed from
        File originDirectory = new File(System.getProperty("user.dir"));

        this.flareFolder = new FlareFolder(originDirectory);

        this.project = providedProject;
        project.localFileDir = originDirectory.getName();

        this.context = context;
    }

    public void Run() {

        if (flareFolder.exists) {
            System.out.println("Project already has a Flare Repo");
            return;
        }

        // Create a .flare folder
        flareFolder.init();

        try {
            PreparedStatement projectInsertQuery = context.prepareStatement(
                    "INSERT INTO projects (title, description, language, local_file_dir, url) VALUES (?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            projectInsertQuery.setString(1, project.title);
            projectInsertQuery.setString(2, project.description);
            projectInsertQuery.setString(3, project.language);
            projectInsertQuery.setString(4, project.localFileDir);
            projectInsertQuery.setString(5, project.url);

            int rowsAffected = projectInsertQuery.executeUpdate();

            try (ResultSet generatedKeys = projectInsertQuery.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.id = generatedKeys.getLong(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Iterate and store all files
        ArrayList<File> fileList = new ArrayList<>();
        getAllFiles(flareFolder.parentDirectoryAsFile(), fileList);

        // Construct and store all Project File objects
        ArrayList<ProjectFile> projectFileList = new ArrayList<>();
        for (File file : fileList) {

            ProjectFile pf = new ProjectFile();

            pf.fileName = file.getName();

            // To reference the Project with a Foreign Key
            pf.parentProjectId = project.id;

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

        try {
            PreparedStatement entryInsertQuery = context.prepareStatement(
                    "INSERT INTO entries (notes, duration, parent_project_id) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            entryInsertQuery.setString(1, initialEntry.notes);
            entryInsertQuery.setInt(2, initialEntry.duration);
            entryInsertQuery.setLong(3, project.id);

            int rowsAffected = entryInsertQuery.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows affected. SQL Failed");
            }

            try (ResultSet generatedKeys = entryInsertQuery.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    initialEntry.id = generatedKeys.getLong(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
