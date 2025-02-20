package data.dao;

import data.DaoInterface;
import data.models.Project;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectDao implements DaoInterface<Project> {

    private final Connection connection;

    public ProjectDao(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Project> getAll() throws SQLException {

        String sql = """
                SELECT  *
                FROM    projects
                """;


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
