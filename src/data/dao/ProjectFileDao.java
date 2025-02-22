package data.dao;

import data.models.ProjectFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectFileDao {

    private Connection connection;

    public ProjectFileDao() {};

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean createProjectFile(ProjectFile newProjectFile) {
        try {
            String sql = """
                    INSERT INTO project_files (filename, filetype, hash, parent_project_id, parent_entry_id)
                    VALUES (?,?,?,?,?)
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newProjectFile.fileName);
            pstmt.setString(2, newProjectFile.fileType);
            pstmt.setString(3, newProjectFile.hash);
            pstmt.setLong(4, newProjectFile.parentProjectId);
            pstmt.setLong(5, newProjectFile.parentEntryId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
