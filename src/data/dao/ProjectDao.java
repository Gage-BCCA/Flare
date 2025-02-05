package data.dao;

import data.DaoInterface;
import data.models.Project;

import java.util.ArrayList;

public class ProjectDao implements DaoInterface<Project> {

    public ArrayList<Project> getAll() {
        return new ArrayList<Project>();
    }

    public Project getById(int id) {
        return new Project();
    }

    public void createNew(Project newProject) {
        return;
    }

    public void deleteById(int id) {
        return;
    }

    public void deleteAll() {
        return;
    }

    public void updateRecord(int id, Project updatedProject) {
        return;
    }

}
