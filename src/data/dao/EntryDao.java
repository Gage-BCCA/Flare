package data.dao;

import data.DaoInterface;
import data.models.Entry;

import java.util.ArrayList;
import java.util.Date;

public class EntryDao implements DaoInterface<Entry> {

    public ArrayList<Entry> getAll() {
        return new ArrayList<Entry>();
    }

    public Entry getById(int id) {
        return new Entry("test", 5);
    }
    public void createNew(Entry newEntry) {
        return;
    }
    public void deleteById(int id) {
        return;
    }
    public void deleteAll() {
        return;
    }
    public void updateRecord(int id, Entry updatedObject) {
        return;
    }

}
