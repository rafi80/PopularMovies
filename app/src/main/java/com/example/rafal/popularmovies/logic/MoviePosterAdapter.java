package com.example.rafal.popularmovies.logic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.rafal.popularmovies.R;
import com.example.rafal.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Stefan on 2015-11-17.
 * * Adapter to fill the Views on main Movie Gallery Layout: fragment_main
 */
public class MoviePosterAdapter extends ArrayAdapter<Movie> {


    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    public MoviePosterAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        //Adapter to inflate the initial grid_view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.poster_item, parent, false);
        }

        // here's where Picasso gets to action :)
        // we need just the ImageViews to display on grid view, so no other attribute
        // of Movie is needed
        ImageView poster = (ImageView) convertView.findViewById(R.id.poster_image);

        Picasso.with(getContext())
                .load(movie.getPosterPathURLString())
                .error(getContext().getResources().getDrawable(R.drawable.no_image_available))
                .into(poster);

        return convertView;
    }
}
