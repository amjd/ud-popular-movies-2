package com.example.amjad.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.amjad.popularmovies.R;
import com.example.amjad.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by amjad on 27/3/16.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.item_movie, movies);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }

        Picasso.with(context)
                .load(movie.getPosterUrl())
                .into((ImageView) convertView);

        return convertView;
    }
}
