package com.galpotechsolutions.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HorrorFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>  {
    /** Adapter for the list of moview*/
    private MovieAdapter movieAdapter;

    /**URL for movie data from the movie db*/
    private static final String MOVIE_REQUEST_URL = "https://api.themoviedb.org/3/discover/movie";

    /**
     * Constant value for the new loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEW_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView emptyStateTextView;

    /** loadingIndicator that is displayed when connection is slow*/
    private View loadingIndicator;

    public HorrorFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_activity, container,false);

        // Create a new adapter that takes an empty list of movies as input
        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        final MovieAdapter itemAdapter = movieAdapter;

        final GridView listView = rootView.findViewById(R.id.list);

        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);

        loadingIndicator = rootView.findViewById(R.id.loading_spinner);

        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current movie that was clicked on
                Movie currentMovie = itemAdapter.getItem(position);

                Bundle extra = new Bundle();
                extra.putString("originalTitle", currentMovie.getOriginalTitle());
                extra.putString("posterPath", currentMovie.getImageResourceIdsSmall());
                extra.putString("overView", currentMovie.getOverview());
                extra.putString("voteAverage", currentMovie.getDetailedVoteAverage());
                extra.putString("releaseDate", currentMovie.getReleaseDate());

                //Create a new Intent to view the detail of the movie
                Intent appInfo = new Intent(getActivity(), DetailActivity.class);
                appInfo.putExtras(extra);
                startActivity(appInfo);

            }
        });

        // Get a reference to the ConnectivityManager to chec state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()){
            // Get the reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallbacks parameters (which is valid)
            loaderManager.initLoader(NEW_LOADER_ID, null, this);
        }else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connect error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // Parse breaks apart the URI string that's passed into its parameters
        Uri baseUri = Uri.parse(getString(R.string.MOVIE_REQUEST_URL));

        // BuildUpon prepared the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the 'format=geojson'
        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter("api_key", getString(R.string.api_key));
        uriBuilder.appendQueryParameter("language", getString(R.string.language));
        //uriBuilder.appendQueryParameter("sort_by", orderBy);
        uriBuilder.appendQueryParameter("with_genres", getString(R.string.horror_genres));

        Log.v("Uri Builder", uriBuilder.toString());
        String urlText = uriBuilder.toString();

        return new MovieLoader(getActivity(), urlText);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {

        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No Movies found"
        emptyStateTextView.setText(R.string.no_movies);

        // Clear the adapter of previous new data
        movieAdapter.clear();

        // If there is a valid list of {@list Movie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update
        if (movies != null && !movies.isEmpty()){
            movieAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        // Loader reset, so we can lear out our existing data
        movieAdapter.clear();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
