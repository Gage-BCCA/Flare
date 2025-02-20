package data.models;

import java.util.Date;

public class Project {
    public long id;
    public String title;
    public String language;
    public String description;
    public String url;
    public String localFileDir;
    public String createdAt;

    public Project () {};
    public Project (
            long id,
            String title,
            String language,
            String description,
            String url,
            String localFileDir,
            Date createdAt
    ) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.description = description;
        this.url = url;
        this.localFileDir = localFileDir;
        this.createdAt = createdAt.toString();
    }
}
