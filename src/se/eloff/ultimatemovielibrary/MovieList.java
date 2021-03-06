package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies_lists")
public class MovieList {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private LocalMovie movie;

    @DatabaseField(canBeNull = false, foreign = true, uniqueIndex = true, uniqueIndexName = "playlist")
    private Playlist list;

    @DatabaseField(canBeNull = false, uniqueIndex = true, uniqueIndexName = "playlist")
    private int position;

    public MovieList() {
    }

    public MovieList(LocalMovie movie, Playlist list) {
        this.movie = movie;
        this.list = list;
    }

    public LocalMovie getMovie() {
        return movie;
    }

    public Playlist getList() {
        return list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}