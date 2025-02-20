package data.dao;

import data.DaoInterface;
import data.models.Project;

import java.sql.*;
import java.util.ArrayList;

public class ProjectDao {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Project> getAllProjects() {
        try {
            String sql = """
                    SELECT  *
                      FROM  projects
                    """;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return constructListFromResultSet(rs);
        } catch (SQLException e) {
            return new ArrayList<Project>();
        }
    }

    public boolean createProject(Project newProject) {
        try {
            String sql = """
                    INSERT INTO projects (title, language, description, local_file_dir, url)
                    VALUES (?,?,?,?,?)
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newProject.title);
            pstmt.setString(2, newProject.language);
            pstmt.setString(3, newProject.description);
            pstmt.setString(4, newProject.localFileDir);
            pstmt.setString(5, newProject.url);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0) {
                return false;
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public long createProjectAndReturnGeneratedKey(Project newProject) {
        long generatedKey = 0;
        try {
            String sql = """
                    INSERT INTO projects (title, language, description, local_file_dir, url)
                    VALUES (?,?,?,?,?)
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newProject.title);
            pstmt.setString(2, newProject.language);
            pstmt.setString(3, newProject.description);
            pstmt.setString(4, newProject.localFileDir);
            pstmt.setString(5, newProject.url);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0) {
                return 0;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getLong(1);
                }
            } catch (SQLException e) {
                return 0;
            }

        } catch (SQLException | RuntimeException e) {
            return 0;
        }

        return generatedKey;
    }

    public boolean deleteProjectById(long id) {
        try {
            String sql = """
                    DELETE FROM projects
                    WHERE id = ?
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0) {
                return false;
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public ArrayList<Project> searchByLanguage(String language) {
        try {
            String sql = """
                SELECT  *
                  FROM  projects
                 WHERE  LOWER(language) = LOWER(?)
                """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, language);
            ResultSet rs = pstmt.executeQuery();
            return constructListFromResultSet(rs);

        } catch (SQLException e) {
            return new ArrayList<Project>();
        }
    }

    public ArrayList<Project> searchByTitle(String title) {
        try {
            String sql = """
                SELECT  *
                  FROM  projects
                 WHERE  LOWER(title) = LOWER(?)
                """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return constructListFromResultSet(rs);

        } catch (SQLException e) {
            return new ArrayList<Project>();
        }
    }



    private ArrayList<Project> constructListFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<Project> results = new ArrayList<>();
        while (rs.next()) {
            long projectId = rs.getLong("id");
            String projectTitle = rs.getString("title");
            String projectLanguage = rs.getString("language");
            String projectDescription = rs.getString("description");
            String projectUrl = rs.getString("url");
            String projectDir = rs.getString("local_file_dir");
            Date projectCreatedAt = rs.getDate("created_at");

            Project project = new Project(
                    projectId,
                    projectTitle,
                    projectLanguage,
                    projectDescription,
                    projectUrl,
                    projectDir,
                    projectCreatedAt
            );

            results.add(project);
        }
        return results;
    }
}
