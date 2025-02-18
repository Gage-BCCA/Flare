package data.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Entry {
    public long id;
    public String notes;
    public int duration;
    public long parentProjectId;
    public String createdAt;
    public ArrayList<String> modifiedFiles;

    public Entry(String notes,
                 int duration) {
        this.notes = notes;
        this.duration = duration;
    }

    public Entry() {} // Empty Constructor for testing and bootstrapping new methods easily
}
