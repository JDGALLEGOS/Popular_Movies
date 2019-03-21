package com.galpotechsolutions.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader {
    /**Tag for log messages*/
    private static final String LOG_TAG = MovieLoader.class.getName();

    /**Query URL*/
    private String mUrl;

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param mUrl url to load data
     */
    public MovieLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread
     */
    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        //Perform a network request, parse he response an extract a list of movies.
        List<Movie> movies = QueryUtils.fetchNewData(mUrl);
        return movies;
    }
}
