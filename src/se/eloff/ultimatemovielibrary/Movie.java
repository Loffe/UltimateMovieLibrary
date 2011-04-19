package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies")
public class Movie implements Comparable<Movie> {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private int year;

    @DatabaseField
    private int discnumber;
    
    @DatabaseField(unique = true)
    private String filepath;
    
    @DatabaseField
    private int rating;

    public Movie() {
    }
    
    public Movie(String name, int year, String filepath) {
        this(name, year, filepath, 1, 1);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie(String name, int year, String filepath, int discnumber, int rating) {
        this.name = name;
        this.year = year;
        this.discnumber = discnumber;
        this.filepath = filepath;
        this.rating = rating;
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

    public void setDiscnumber(int discnumber) {
        this.discnumber = discnumber;
    }

    public int getDiscnumber() {
        return discnumber;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    @Override
    public int compareTo(Movie arg0) {
        return this.getName().toLowerCase().compareTo(
                arg0.getName().toLowerCase());
    }
}