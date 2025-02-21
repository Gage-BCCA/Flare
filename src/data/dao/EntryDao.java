package data.dao;

import data.DaoInterface;
import data.models.Entry;
import data.models.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class EntryDao  {

    private Connection connection;
    public EntryDao() {}

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean createEntry(Entry newEntry) {
        try {
            String sql = """
                    INSERT INTO entries (notes, duration, parent_project_id)
                    VALUES  (?, ?, ?)
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newEntry.notes);
            pstmt.setInt(2, newEntry.duration);
            pstmt.setLong(3, newEntry.parentProjectId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0) {
                return false;
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public long createEntryAndReturnGeneratedKey(Entry newEntry) {
        long generatedKey = 0;
        try {
            String sql = """
                    INSERT INTO entries (notes, duration, parent_project_id)
                    VALUES  (?, ?, ?)
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newEntry.notes);
            pstmt.setInt(2, newEntry.duration);
            pstmt.setLong(3, newEntry.parentProjectId);
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
            System.out.println(e.getMessage());
            return 0;
        }
        return generatedKey;
    }

    public boolean deleteEntryById(long id) {
        try {
            String sql = """
                    DELETE FROM entries
                    WHERE id = ?
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected <= 0 ) {
                return false;
            }

        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public ArrayList<Entry> getRecentEntries() {
        String sql = """
                SELECT  entries.id, entries.notes, entries.duration, entries.created_at,
                        projects.title
                FROM    entries
                INNER   JOIN projects on projects.id = entries.parent_project_id
                ORDER   BY entries.created_at DESC
                LIMIT   5
                """;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return constructEntryListFromResultSet(rs);


        } catch (SQLException e) {
            System.out.println("Bad things happened.");
            return new ArrayList<Entry>();
        }
    }

    public ArrayList<Entry> getOlderEntries() {
        String sql = """
                SELECT  entries.id,
                        entries.notes,
                        entries.duration,
                        entries.created_at,
                        projects.title
                  FROM  entries
                 INNER  JOIN projects ON projects.id = entries.parent_project_id
                ORDER   BY entries.created_at ASC
                LIMIT   5
                """;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return constructEntryListFromResultSet(rs);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Bad things happened.");
            return new ArrayList<Entry>();
        }
    }

    public ArrayList<Entry> getEntryById(long id){
        String sql = """
                SELECT  entries.id, entries.notes, entries.duration, entries.created_at,
                        projects.title
                FROM    entries
                INNER   JOIN projects on projects.id = entries.parent_project_id
                WHERE entries.id = ?
                LIMIT   1
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            return constructEntryListFromResultSet(rs);


        } catch (SQLException e) {
            System.out.println("Bad things happened.");
            return new ArrayList<Entry>();
        }
    }

    public ArrayList<Entry> searchByNote(String note) {
        try {
            String sql = """
                SELECT  entries.id,
                        entries.notes,
                        entries.duration,
                        entries.create_at,
                        project.title
                  FROM  entries
                 INNER  JOIN projects ON entries.parent_project_id = projects.id
                 WHERE  LOWER(notes) LIKE LOWER('%' || ? || '%')
                """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, note);
            ResultSet rs = pstmt.executeQuery();
            return constructEntryListFromResultSet(rs);

        } catch (SQLException e) {
            return new ArrayList<Entry>();
        }
    }

    public ArrayList<Entry> searchByProjectTitle(String title) {
        try {
            String sql = """
                    SELECT  projects.title,
                            entries.id,
                            entries.notes,
                            entries.duration,
                            entries.created_at
                      FROM  projects
                     INNER  JOIN entries ON projects.id = entries.parent_project_id
                     WHERE  LOWER(projects.title) LIKE LOWER('%' || ? || '%')
                     ORDER  BY entries.created_at DESC;
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return constructEntryListFromResultSet(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<Entry>();
        }
    }

    public ArrayList<Entry> searchByProjectLanguage(String language) {
        try {
            String sql = """
                    SELECT  projects.title,
                            entries.id,
                            entries.notes,
                            entries.duration,
                            entries.created_at
                      FROM  projects
                     INNER  JOIN entries ON projects.id = entries.parent_project_id
                     WHERE  LOWER(projects.language) LIKE LOWER('%' || ? || '%')
                     ORDER  BY entries.created_at DESC;
                    """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, language);
            ResultSet rs = pstmt.executeQuery();
            return constructEntryListFromResultSet(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<Entry>();
        }
    }

    public boolean updateEntry(long id, Entry newEntry) {
        try {
            String selectSql = """
                SELECT  entries.id,
                        entries.notes,
                        entries.duration,
                        entries.created_at,
                        projects.title
                  FROM  entries
                 INNER  JOIN projects ON projects.id = entries.parent_project_id
                 WHERE  entries.id = ?
                 LIMIT 1;
                """;
            PreparedStatement selectPstmt = connection.prepareStatement(selectSql);
            selectPstmt.setLong(1, id);
            ResultSet rs1 = selectPstmt.executeQuery();
            ArrayList<Entry> selectResult = constructEntryListFromResultSet(rs1);
            Entry originalEntry = selectResult.getFirst();

            String updateSql = """
                    UPDATE entries
                    SET notes = ?, duration = ?
                    WHERE id = ?
                    """;
            PreparedStatement updatePstmt = connection.prepareStatement(updateSql);
            updatePstmt.setString(1, (
                    newEntry.notes.isEmpty() ? originalEntry.notes : newEntry.notes
            ));

            updatePstmt.setInt(2, (
                    newEntry.duration == 0 ? originalEntry.duration : newEntry.duration
            ));

            updatePstmt.setLong(3, id);
            int rowsAffected = updatePstmt.executeUpdate();
            if (rowsAffected <= 0) {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    private ArrayList<Entry> constructEntryListFromResultSet(ResultSet rs) throws SQLException{
        ArrayList<Entry> results = new ArrayList<>();
        while (rs.next()) {
            Entry entry = new Entry();
            entry.id = rs.getLong("id");
            entry.notes = rs.getString("notes");
            entry.duration =  rs.getInt("duration");
            entry.createdAt = rs.getTimestamp("created_at").toString();
            entry.parentProjectName = rs.getString("title");

            results.add(entry);
        }

        return results;
    }

}
