package com.example.amjad.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    private Movie mMovie;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mMovie = getActivity().getIntent().getExtras().getParcelable("movieDetail");
        String releaseDate = mMovie.releaseDate;
        try {
            DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat dfOut = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Date cleanDate = dfIn.parse(releaseDate);
            ((TextView) rootView.findViewById(R.id.releaseDate)).setText(String.format("Released on: %s", dfOut.format(cleanDate)));
        } catch (ParseException pe) {
            Log.e(LOG_TAG, "Error", pe);
        }

        ((TextView) rootView.findViewById(R.id.movieTitle)).setText(mMovie.title);
        ((TextView) rootView.findViewById(R.id.overviewText)).setText(mMovie.overview);
        ((TextView) rootView.findViewById(R.id.rating)).setText(String.format("%s %.1f", "Rating:", mMovie.voteAverage));

        ImageView movieThumbnail = (ImageView) rootView.findViewById(R.id.moviePosterDetail);
        Picasso.with(getContext()).load(mMovie.posterUrl).into(movieThumbnail);

        return rootView;
    }
}
