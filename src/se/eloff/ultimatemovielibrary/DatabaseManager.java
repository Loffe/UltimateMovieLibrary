package se.eloff.ultimatemovielibrary;

public class DatabaseManager {
    private static DatabaseManager instance = null;

    public DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
}
