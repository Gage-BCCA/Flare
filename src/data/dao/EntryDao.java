package data.dao;

import data.DaoInterface;
import data.models.Entry;

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
