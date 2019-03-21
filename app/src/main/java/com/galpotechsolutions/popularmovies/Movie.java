package com.galpotechsolutions.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    /** Original title of the movie */
    private String originalTitle;
    /** poster path of the movie */
    private String posterPath;
    /** Overwiew of the movie */
    private String overview;
    /** Vote average of the movie */
    private String voteAverage;
    /** Release date of the movie */
    private String releaseDate;
    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     *
     * @param originalTitle
     * @param posterPath
     * @param overview
     * @param voteAverage
     * @param releaseDate
     */
    public Movie(String originalTitle, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    /**
     * Gets the original title of the movie
     * @return originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Returns URL string to where the poster can be loaded
     *
     * @return URL string to where the poster can be loaded
     */
    public String getPosterPathBig() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

        return TMDB_POSTER_BASE_URL + posterPath;
    }

    /**
     * Returns URL string to where the poster can be loaded
     *
     * @return URL string to where the poster can be loaded
     */
    public String getPosterPathSmall() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + posterPath;
    }

    /**
     * Gets the TMDb movie description (plot)
     *
     * @return overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Gets the TMDb vote average score
     *
     * @return voteAverage
     */
    private String getVoteAverage() {
        return voteAverage;
    }

    /**
     * Gets the release date of the movie
     *
     * @return releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets the TMDb way of scoring the movie: <score>/10
     *
     * @return TMDb way of scoring the movie: <score>/10
     */
    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }

    /**
     * Returns the format of the date.
     *
     * @return Format of the date
     */
    public String getDateFormat() {
        return DATE_FORMAT;
    }


    /**
     * Gets the image of the movie
     * @return imageResourceIds
     */
    public String getImageResourceIds(){
        return getPosterPathBig();
    }

    /**
     * Gets the image of the movie
     * @return imageResourceIds
     */
    public String getImageResourceIdsSmall(){
        return getPosterPathSmall();
    }

    /**
     * Returns whether or not there is an image for this news story.
     */
    public boolean hasImage() {
        return !getImageResourceIds().equals("");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
    }

    private Movie(Parcel in){
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };
}
