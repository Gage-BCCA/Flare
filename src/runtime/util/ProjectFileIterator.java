package runtime.util;

import data.models.ProjectFile;
import runtime.crypto.Hasher;

import java.io.File;
import java.util.ArrayList;

public class ProjectFileIterator {

    private final FlareFilter filter;

    public ProjectFileIterator() {
        filter = new FlareFilter();
    }

    public void getAllFiles(File directory, ArrayList<File> filesContainer) {
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

    public ArrayList<ProjectFile> constructProjectFilesFromFiles(ArrayList<File> fileList,
                                                                          long projectId,
                                                                          long entryId) {
        ArrayList<ProjectFile> pfList = new ArrayList<>();

        for (File file : fileList) {
            ProjectFile pf = new ProjectFile();
            pf.fileName = file.getName();
            pf.parentProjectId = projectId;
            pf.parentEntryId = entryId;

            // Manipulate the file name to get file type
            String extension = "";
            int i = file.getAbsolutePath().lastIndexOf('.');
            if (i > 0) {
                extension = file.getAbsolutePath().substring(i + 1);
                pf.fileType = extension;
            } else {
                pf.fileType = null;
            }

            // Attempt to find the hash value
            try {
                pf.hash = (new Hasher(file).getSha1Digest());
            } catch (Exception e) {
                pf.hash = null;
            }
            pfList.add(pf);
        }

        return pfList;
    }

    public void filterProjectFiles(ArrayList<ProjectFile> pfList) {
        pfList.removeIf(pf -> !filter.allowedFileTypes.contains(pf.fileType));
    }
}
