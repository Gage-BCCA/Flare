package data;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DaoInterface<T> {
    public ArrayList<T> getAll() throws SQLException;
    public T getById(int id);
    public void createNew(T objToBeCreated);
    public void deleteById(int id);
    public void deleteAll();
    public void updateRecord(int id, T updatedObject);
}
