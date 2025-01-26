import java.util.ArrayList;
import java.util.Date;

public class DatabaseContext {

    // Dummy Data factory
    public ArrayList<Entry> generateDummyData() {
        Entry fakeEntry = new Entry(
                "C#",
                "C# is better than Java",
                new Date(),
                new Date(2010, 1, 3)
        );

        Entry fakeEntry1 = new Entry(
                "C++",
                "C++ is better than Java",
                new Date(),
                new Date(2010, 1, 3)
        );

        Entry fakeEntry2 = new Entry(
                "Python",
                "Python is better than Java",
                new Date(),
                new Date(2010, 1, 3)
        );

        // Fake List of Entries for testing
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(fakeEntry);
        entries.add(fakeEntry1);
        entries.add(fakeEntry2);

        return entries;
    }
}
