package com.example.rafal.popularmovies.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rafal.popularmovies.R;
import com.example.rafal.popularmovies.logic.FetchedMovies;
import com.example.rafal.popularmovies.logic.MoviePosterAdapter;
import com.example.rafal.popularmovies.model.Movie;

import org.json.JSONException;

import java.util.ArrayList;

/*
 * The activity that shows the gallery of popular movies in the grid view.
 * It's the fragment of main activity
 */
public class MovieGalleryFragment extends Fragment{

    private static final String LOG_TAG = MovieGalleryFragment.class.getSimpleName();

    public String currentSortingOrder ="popularity.desc";

    private MoviePosterAdapter moviePosterAdapter;

    // list of movies passed from async Task
    private ArrayList<Movie> fetchedMovieList;

    public MovieGalleryFragment() {
    }

    public void setList(ArrayList<Movie> list){
        this.fetchedMovieList=list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {

            this.moviePosterAdapter = new MoviePosterAdapter(getActivity(), new ArrayList<Movie>());
        } else {

            fetchedMovieList = savedInstanceState.getParcelableArrayList("movies");
            this.moviePosterAdapter = new MoviePosterAdapter(getActivity(), new ArrayList<Movie>());

            for (Movie movie : fetchedMovieList){
                moviePosterAdapter.add(movie);
                Log.v(LOG_TAG, "Retrieving poster from savedInstanceState: "
                        + movie.getPosterPathURLString());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
           inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // setting to determine sorting order for MovieGalleryFragment
        if (id == R.id.action_settings) {

           // the onOptionsMenuClosed doesn't work so the solution using Setting activity
           // cannot be used
           /*Intent intent = new Intent(getActivity(),SettingsActivity.class);
           startActivity(intent);*/

           // and therefore a simple, not elegant solution:
            if (currentSortingOrder.equals(getString(R.string.value_sortOrder_popular))) {
                // change the current sorting order to high-rank
                currentSortingOrder= getString(R.string.value_sortOrder_rank);
                // change the text of the setting text
                item.setTitle(R.string.label_sortOrder_popular);
            } else {
                // change the current sorting order to popular
                currentSortingOrder= getString(R.string.value_sortOrder_popular);
                // change the text of the setting text
                item.setTitle(R.string.label_sortOrder_rank);
            }

            updateMovies(currentSortingOrder);

           return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    //for some reason onOptionsMenuClosed doesn't work :(
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.v(LOG_TAG, "Update!!!");
        updateMovies();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", fetchedMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

/*
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), new ArrayList<Movie>());*/

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.main_movies_grid);
        gridView.setAdapter(moviePosterAdapter);

        // setting the listener on movie items in gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // passing the clicked movie object in intent to MovieDetailActivity
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie clickedMovie = moviePosterAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("A movie", clickedMovie);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void updateMovies(String sortingOrder) {

        GetMoviesTask getMoviesTask = new GetMoviesTask(this);

        getMoviesTask.execute(sortingOrder);
    }


    /*private void updateMovies() {

        GetMoviesTask getMoviesTask = new GetMoviesTask(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingOrder = prefs.getString(getString(R.string.pref_sortOrder_key),
                getString(R.string.pref_sortOrder_key));

        getMoviesTask.execute(sortingOrder);
    }
*/
    public class GetMoviesTask extends AsyncTask<String, Void,  ArrayList<Movie> > {

        // using activity as a attribute of this class to pass a list of movies back to
        // this activity (MovieGalleryFragment) to use in onSaveInstanceState method
        // based upon: http://stackoverflow.com/questions/17596188/asynctask-return-list-to-activity
        private MovieGalleryFragment activity;

        public GetMoviesTask (MovieGalleryFragment activity){
            this.activity=activity;
        }

        // loading the movie data in background
        @Override
        public ArrayList<Movie>  doInBackground (String... params) {

            ArrayList<Movie> fetchedListOfMovies = new  ArrayList<Movie>();

            FetchedMovies fetchedMovies = new FetchedMovies();
            try {
                fetchedListOfMovies = fetchedMovies.fetchTheListOfMovies(params[0]);
            }  catch (JSONException e) {
                e.printStackTrace();
            }

            return fetchedListOfMovies;
        }

        protected void onPostExecute( ArrayList<Movie> fetchedListOfMovies) {

            if (fetchedListOfMovies != null) {
                moviePosterAdapter.clear();
                for (Movie movieObject:fetchedListOfMovies ) {
                    moviePosterAdapter.add(movieObject);
                }
            } else {
                Toast.makeText(activity.getContext(), R.string.movie_fetching_error,
                        Toast.LENGTH_LONG).show();
            }
            activity.setList(fetchedListOfMovies);

        }
    }
}