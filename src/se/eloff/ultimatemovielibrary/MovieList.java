package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies_lists")
public class MovieList {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private LocalMovie movie;

    @DatabaseField(canBeNull = false, foreign = true)
    private List list;

    public MovieList() {
    }

    public MovieList(LocalMovie movie, List list) {
        this.movie = movie;
        this.list = list;
    }

    public LocalMovie getMovie() {
        return movie;
    }

    public List getList() {
        return list;
    }
}
