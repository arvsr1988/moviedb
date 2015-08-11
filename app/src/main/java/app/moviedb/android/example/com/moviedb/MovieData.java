package app.moviedb.android.example.com.moviedb;

import java.io.Serializable;

/**
 * Created by arvind on 20/07/15.
 */
public class MovieData implements Serializable {
    private String imageUrl;
    private String description;
    private String title;
    private String userRating;
    private String releaseDate;


    public MovieData(String title, String imageUrl, String description, String userRating, String releaseDate){
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return "http://image.tmdb.org/t/p/w92" + imageUrl;
    }
    public String getBigImageUrl() {
        return "http://image.tmdb.org/t/p/w342" + imageUrl;
    }
    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
