package se.eloff.ultimatemovielibrary;

import com.j256.ormlite.field.DatabaseField;

public class MovieInfo {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String cast = "";

    @DatabaseField
    private String directors = "";

    @DatabaseField
    private String cover = "";

    @DatabaseField
    private String plot = "";

    @DatabaseField
    private String genres = "";
    
    @DatabaseField
    private int onlineRating = 0;

    public MovieInfo() {
    }

}
