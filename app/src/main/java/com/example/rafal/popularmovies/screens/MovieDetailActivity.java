package com.example.rafal.popularmovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafal.popularmovies.R;
import com.example.rafal.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends ActionBarActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
    }

    /*
     * A placeholder fragment containing a simple view.
     */
    public static class MovieDetailFragment extends Fragment {

        public MovieDetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("A movie")) {
                Movie movie = intent.getExtras().getParcelable("A movie");

                // get the values from Movie object and pass them to Views

                // Title
                ((TextView) rootView.findViewById(R.id.title_text))
                        .setText(movie.getTitle());
                // Poster
                ImageView poster = (ImageView)rootView.findViewById(R.id.poster_image);
                Picasso.with(getContext()).load(movie.getPosterPathURLString()).into(poster);
                // Release date
                ((TextView)rootView.findViewById(R.id.release_date_text))
                        .setText(movie.getReleaseYear());
                // Rating
                ((TextView)rootView.findViewById(R.id.user_rating_text))
                        .setText(movie.getUserRating().intValue() + getString(R.string.maximalRating));
                // Synopis/Description
                ((TextView)rootView.findViewById(R.id.plot_synopsis_text))
                        .setText(movie.getPlotSynopsis());
            }

            return rootView;
        }
    }

}
