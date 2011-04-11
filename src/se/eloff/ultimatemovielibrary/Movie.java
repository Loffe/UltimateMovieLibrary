package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies")
public class Movie {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private int year;

    public Movie() {
    }

    public Movie(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
