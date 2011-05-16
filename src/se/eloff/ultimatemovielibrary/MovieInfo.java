package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;

public class MovieInfo {
    @DatabaseField(generatedId = true)
    private int id;

    // Only names, separate with comma
    @DatabaseField
    private String cast = "";

    // Only names, separate with comma
    @DatabaseField
    private String directors = "";

    // path to image file
    @DatabaseField
    private String cover = "";

    @DatabaseField
    private String plot = "";

    // Only names, separate with comma
    @DatabaseField
    private String genres = "";

    @DatabaseField
    private int onlineRating = 0;
    
    @DatabaseField(unique = true)
    private int movieid = -1;

    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public MovieInfo(String cast, String directors, String cover, String plot,
            String genres, int onlineRating, int movieid) {
        super();
        this.setCast(cast);
        this.directors = directors;
        this.cover = cover;
        this.plot = plot;
        this.genres = genres;
        this.onlineRating = onlineRating;
        this.movieid = movieid;
    }

    public MovieInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getOnlineRating() {
        return onlineRating;
    }

    public void setOnlineRating(int onlineRating) {
        this.onlineRating = onlineRating;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCast() {
        return cast;
    }

}
