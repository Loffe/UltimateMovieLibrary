package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchfolders")
public class WatchFolder {
    @DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField
    private String folderPath;
    
    @DatabaseField
    private Long lastScanned;

    public WatchFolder() {
        super();
    }

    public WatchFolder(String folderPath, Long lastScanned) {
        super();
        this.folderPath = folderPath;
        this.lastScanned = lastScanned;
    }

    public int getId() {
        return id;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public Long getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Long lastScanned) {
        this.lastScanned = lastScanned;
    }
}
