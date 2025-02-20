package runtime.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FlareFolder {

    private final String flareFolderName = "\\.flare";

    public boolean exists;
    public Path parentDirectory;
    public Path flareFolderFullPath;


    public FlareFolder(
            File originDirectory
    ) {
        this.flareFolderFullPath = Paths.get(originDirectory + flareFolderName);
        this.parentDirectory = flareFolderFullPath.getParent();
        this.exists = checkExistence();
    }


    private boolean checkExistence() {
        return Files.exists(flareFolderFullPath);
    }


    public void init() {
        try {
            Files.createDirectory(flareFolderFullPath);
        } catch (IOException e) {
            System.out.println("Fatal Error: " + e.getMessage());
        }
    }


    public Path path() {
        return flareFolderFullPath;
    }

    public File parentDirectoryAsFile() {
        return parentDirectory.toFile();
    }


}
