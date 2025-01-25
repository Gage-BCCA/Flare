import java.time.Duration;
import java.util.Date;

public class Entry {
    public String language;
    public String notes;

    public Date startTime;
    public Date endTime;

    public long durationInMinutes;

    public Entry(String language,
                 String notes,
                 Date startTime,
                 Date endTime ) {
        this.language = language;
        this.notes = notes;
        this.startTime = startTime;
        this.endTime = endTime;
        Duration duration = Duration.between(endTime.toInstant(), startTime.toInstant());
        this.durationInMinutes = duration.getSeconds();
    }
}
