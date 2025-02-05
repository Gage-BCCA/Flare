package data;

import data.models.Entry;

import java.util.ArrayList;

public class DatabaseContext {

    // Dummy Data factory
    public ArrayList<Entry> generateDummyData() {
        Entry fakeEntry = new Entry(
                "C# is better than Java",
                5
        );

        Entry fakeEntry1 = new Entry(
                "C++ is better than Java",
                5
        );

        Entry fakeEntry2 = new Entry(
                "Python is better than Java",
                5
        );

        // Fake List of Entries for testing
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(fakeEntry);
        entries.add(fakeEntry1);
        entries.add(fakeEntry2);

        return entries;
    }
}
