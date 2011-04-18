package se.eloff.ultimatemovielibrary;

import java.util.List;

public interface MovieSearchClient {
    public void searchFinished(List<Movie> movies, int searchKey);
}
