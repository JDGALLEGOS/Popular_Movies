package com.galpotechsolutions.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving movie data from the movie db.
 */
public class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Movie> extractFeaturesFromJson(String movieJSON){
        //If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(movieJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding movies.
        List<Movie> movie = new ArrayList<>();

        // Try to parse the newJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try{
            // build up a list of Earthquake objects with the corresponding data.

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonReponse = new JSONObject(movieJSON);

            // Extract the JSONArray associated with the ket called "results"
            // which represents a list of results (or news).
            JSONArray movieArray = baseJsonReponse.getJSONArray("results");

            for (int i=0; i<movieArray.length(); i++ ){
                // Get a sing movie at position i within the list of movies
                JSONObject currentMovie = movieArray.getJSONObject(i);

                //Extract the value for the key called title
                String title = currentMovie.getString("title");

                // Extract the value for the key called release_date
                String releaseDate = currentMovie.getString("release_date");

                // Extract the value for the key called vote_average
                String voteAverage = currentMovie.getString("vote_average");

                // Extract the value for the key called poster_path
                String posterPath = currentMovie.getString("poster_path");

                // Extract the value for the key called overview
                String overView = currentMovie.getString("overview");

                // Create a new {@link Movie} object with the title, vote_average, release_date,
                // poster_path, overview from the JSON response
                Movie movieResponse = new Movie(title, posterPath, overView, voteAverage, releaseDate);
                movie.add(movieResponse);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }

        return movie;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createURL(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return  url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // IF the URL is null, then return early.
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds*/);
            urlConnection.setConnectTimeout(150000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successfull (response code 200),
            //then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *  Query the movie db database and return a list of {@link Movie} objects.
     */
    public static List<Movie> fetchNewData(String requestURL){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        //Create URL Object
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem make the HTTP request", e);
        }

        // Extract relevant field from the JSON response and create a list of {@link Movie}s
        List<Movie> movies = extractFeaturesFromJson(jsonResponse);

        return movies;
    }

}
