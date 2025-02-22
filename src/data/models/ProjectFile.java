package data.models;

public class ProjectFile {
    public long id;
    public String fileName;
    public String fileType;
    public String hash;
    public String iteratedAt;
    public long parentProjectId;
    public long parentEntryId;

    @Override
    public String toString() {
        return fileName;
    }
}
