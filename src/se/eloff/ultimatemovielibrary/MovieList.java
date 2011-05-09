package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies_lists")
public class MovieList {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private Movie movie;

    @DatabaseField(canBeNull = false, foreign = true)
    private Playlist list;

    public MovieList() {
    }

    public MovieList(Movie movie, Playlist list) {
        this.movie = movie;
        this.list = list;
    }

    public Movie getMovie() {
        return movie;
    }

    public Playlist getList() {
        return list;
    }
}
