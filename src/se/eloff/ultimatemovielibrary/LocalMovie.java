package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies")
public class LocalMovie implements Comparable<LocalMovie> {
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
    
    @DatabaseField
    private boolean seen;
    
    @DatabaseField
    private boolean favorite;
    
    @DatabaseField
    private boolean wish;
    
    //TODO: replace with a info object later
    @DatabaseField
    private int info_id = -1;
    
    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public LocalMovie() {
    }
    
    public LocalMovie(String name, int year, String filepath) {
        this(name, year, filepath, 1, 0);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalMovie(String name, int year, String filepath, int discnumber, int rating) {
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isWish() {
        return wish;
    }

    @Override
    public int compareTo(LocalMovie arg0) {
        return this.getName().toLowerCase().compareTo(
                arg0.getName().toLowerCase());
    }

    void setId(int id) {
        this.id = id;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setWish(boolean wish) {
        this.wish = wish;
    }
}