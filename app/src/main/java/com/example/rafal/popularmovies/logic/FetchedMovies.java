package com.example.rafal.popularmovies.logic;

import android.net.Uri;
import android.util.Log;

import com.example.rafal.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Stefan on 2015-11-17.
 */
public class FetchedMovies {

    private final String LOG_TAG = FetchedMovies.class.getSimpleName();

    // Hardcoded API Key of TheMovieDBAPI
    private String apiKey = "";

    public FetchedMovies() {
    }

    public ArrayList<Movie> fetchTheListOfMovies(String sortingType) throws JSONException {

        // The public method that provide the results based upon the chosen sort method
        // Uses the private methods to get the answer form the Movie DB and parse it
        // To a list of Movie Objects

        ArrayList<Movie> fetchedMovieList = new ArrayList<Movie>();

        try {
            fetchedMovieList = this.getMoviesDataFromDiscoverJsonString(
                                    fetchTheMovieDBAnswerString(
                                        buildDiscoverMoviesApiCAll(sortingType)
                    )
            );

        } catch (JSONException e) {
            System.out.println("There was a problem with fetching the movies.");
            e.printStackTrace();
        }

        return fetchedMovieList;
    }

    public String[] fetchTheTrailersList (String movieId) {
        // The public method that provide the results based upon the chosen sort method
        String[] listOfTrailers = new String[0];
        // TODO in part 2
        return listOfTrailers;
    }

    private ArrayList<Movie> getMoviesDataFromDiscoverJsonString(String movieDBAnswerJsonString)
            throws JSONException {

        // The method parses JSON answer String and returns a list of Movie objects
        // Based upon solution from Sunshine App

        ArrayList<Movie> resultStrs = new ArrayList<Movie>();

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_TITLE = "original_title";
        final String MDB_POSTER = "poster_path";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_PLOT_SYNOPSIS = "overview";
        final String MDB_USER_RATING = "vote_average";


        JSONObject inputJSONString = new JSONObject(movieDBAnswerJsonString);
        // passing result objects
        JSONArray resultsArray = inputJSONString.getJSONArray(MDB_RESULTS);

        // parsing through the array of results and extracting relevant attributes
        for (int i = 0; i < resultsArray.length(); i++) {

            String id;
            String title;
            String posterPathURLString;
            String releaseYear;
            String plotSynopsis;
            Double userRating;

            // Get the JSON object representing one movie
            JSONObject movie = resultsArray.getJSONObject(i);

            // title is an attribute of JSON movie object
            id = movie.getString(MDB_ID);

            // title is an attribute of JSON movie object
            title = movie.getString(MDB_TITLE);

            // posterPathSuffix is an attribute of JSON movie object
            String posterPathSuffix = movie.getString(MDB_POSTER);

            // creating full path to poster with hardcoded size: w342
            posterPathURLString = "http://image.tmdb.org/t/p/w342/" + posterPathSuffix;

            // release date is an attribute of JSON movie object
            // Not sure in what format should release Date be presented in.
            // Based upon Mockups - it should be just year. So getting just year.
            releaseYear = getFormattedReleaseDate(movie.getString(MDB_RELEASE_DATE));

            // plotSynopsis is an attribute of JSON movie object
            plotSynopsis = movie.getString(MDB_PLOT_SYNOPSIS);

            // title is an attribute of JSON movie object
            userRating = movie.getDouble(MDB_USER_RATING);

            resultStrs.add(i,new Movie(id, title, posterPathURLString,
                    releaseYear, plotSynopsis, userRating));
        }

        for (Movie m : resultStrs) {
            Log.v(LOG_TAG, "Movie entry: " + m.toString());
        }
        return resultStrs;
    }

    private String[] getMovieTrailerDataFromMoviesJson(String movieDBAnswerJsonString)
            throws JSONException {

        String[] resultStrs = new String[0];
        // TODO in part 2
        return resultStrs;
    }

    private String fetchTheMovieDBAnswerString(String apiCallUrl) {

        // Method built upon solution from Sunshine App
        // Generic method which returns the JSON String with The MovieDB API answer
        // based upon API Call Url which is being passed as method's argument

        // create connection
        HttpURLConnection urlConnection = null;
        // prepare reader to get data
        BufferedReader reader = null;

        // JSON response as String
        String movieDBAnswerJsonStr = null;

        try {

            URL url = new URL(apiCallUrl);

            Log.v(LOG_TAG, "Built URL = " + url.toString());

            // Create the request to MovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieDBAnswerJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieDBAnswerJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data,
            // there's no point in attempting to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("DetailFragment", "Error closing stream", e);
                }
            }
        }

        return movieDBAnswerJsonStr;
    }

    private String buildDiscoverMoviesApiCAll(String sortingType) {

        //Example call http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[]
        Uri.Builder urlBuilder = new Uri.Builder();

        urlBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sortingType)
                .appendQueryParameter("api_key", apiKey);

        String myUrl = urlBuilder.build().toString();

        return myUrl;
    }

    private String buildGetMovieTrailerApiCAll(String movieId) {

        // Example call http://api.themoviedb.org/3/movie/2/videos?api_key=[]
        Uri.Builder urlBuilder = new Uri.Builder();

        urlBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", apiKey);

        String myUrl = urlBuilder.build().toString();
        ;

        return myUrl;
    }

    private String getFormattedReleaseDate(String date) {

        String year = "";

        if (date.length()>=4) {
            year = date.substring(0,4);
        } else {
            year = "NA";
        }
        return year;
    }
}


