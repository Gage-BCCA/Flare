package appdriver;

import java.io.File;
import java.util.ArrayList;


import data.dao.EntryDao;
import data.dao.ProjectDao;
import data.dao.ProjectFileDao;
import data.models.Entry;
import data.models.Project;
import data.models.ProjectFile;

import runtime.crypto.Hasher;
import runtime.util.FlareFolder;
import runtime.util.ProjectFileIterator;
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
        ProjectFileIterator iter = new ProjectFileIterator();
        iter.getAllFiles(new File(project.localFileDir), fileList);

        ArrayList<ProjectFile> currentProjectFileList = iter.constructProjectFilesFromFiles(fileList,
                project.id,
                initialEntry.id);

        iter.filterProjectFiles(currentProjectFileList);
        for (ProjectFile pf : currentProjectFileList) {
            projectFileDao.createProjectFile(pf);
        }

        // Store directory information in .flare folder
        XmlWriter writer = new XmlWriter();
        try {
            writer.CreateNew(project, currentProjectFileList, flareFolder.path().toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Flare Initialized.");

    }
}
