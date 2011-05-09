package se.eloff.ultimatemovielibrary;

import java.util.List;

public interface MovieSearchClient {
    public void searchFinished(List<LocalMovie> movies, int searchKey);
}
