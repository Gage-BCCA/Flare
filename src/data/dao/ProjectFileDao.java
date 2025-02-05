package data.dao;

import data.DaoInterface;
import data.models.ProjectFile;

import java.util.ArrayList;

public class ProjectFileDao implements DaoInterface<ProjectFile> {

    public ArrayList<ProjectFile> getAll() {
        return new ArrayList<ProjectFile>();
    }

    public ProjectFile getById(int id) {
        return new ProjectFile();
    }

    public void createNew(ProjectFile newProjectFile) {
        return;
    }

    public void deleteById(int id) {
        return;
    }

    public void deleteAll() {
        return;
    }

    public void updateRecord(int id, ProjectFile updatedProjectFile) {
        return;
    }

}
