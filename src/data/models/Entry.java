package data.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Entry {
    public long id;
    public String notes;
    public int duration;
    public long parentProjectId;
    public String parentProjectName;
    public String createdAt;
    public ArrayList<ProjectFile> modifiedFiles;

    // A few overloaded constructors to allow my code to
    // actually execute for now (I suck at this).
    public Entry(String notes,
                 int duration) {
        this.notes = notes;
        this.duration = duration;
        this.modifiedFiles = new ArrayList<ProjectFile>();
    }

    public Entry(String notes,
                 int duration,
                 long parentProjectId) {
        this.notes = notes;
        this.duration = duration;
        this.parentProjectId = parentProjectId;
        this.modifiedFiles = new ArrayList<ProjectFile>();
    }

    public Entry() {
        this.modifiedFiles = new ArrayList<ProjectFile>();
    } // Empty Constructor for testing and bootstrapping new methods easily
}
