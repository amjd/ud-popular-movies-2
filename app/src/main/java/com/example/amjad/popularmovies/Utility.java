package com.example.amjad.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.amjad.popularmovies.data.MovieContract;
import com.example.amjad.popularmovies.model.Movie;

/**
 * Created by amjad on 29/5/16.
 */
public class Utility {
    public static final String LOG_TAG = Utility.class.getSimpleName();

    public static boolean isFavoriteMovie(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            null,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
            new String[] { String.format("%d", id) },
            null
        );

        Log.v(LOG_TAG, String.format("Number of matching rows in table: %d",cursor.getCount()));

        return cursor.getCount() > 0;
    }

    public static class AddFavoriteMovieTask extends AsyncTask<Void, Void, Uri> {
        MenuItem item;
        Movie movie;
        View parentView;
        Context context;

        AddFavoriteMovieTask(Movie movie, MenuItem item, View parentView, Context context) {
            this.movie = movie;
            this.item = item;
            this.parentView = parentView;
            this.context = context;
        }

        @Override
        protected Uri doInBackground(Void... params) {
            ContentValues values = new ContentValues();

            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
            values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPoster());
            values.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdrop());
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
            values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());

            return context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                    values);
        }

        @Override
        protected void onPostExecute(Uri returnUri) {
            item.setIcon(R.drawable.ic_menu_favorite_remove);
            Snackbar.make(parentView, "Added to favorites", Snackbar.LENGTH_SHORT).show();
        }
    }

    public static class RemoveFavoriteMovieTask extends AsyncTask<Void, Void, Integer> {
        MenuItem item;
        Movie movie;
        View parentView;
        Context context;

        RemoveFavoriteMovieTask(Movie movie, MenuItem item, View parentView, Context context) {
            this.movie = movie;
            this.item = item;
            this.parentView = parentView;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            return context.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.format("%d",movie.getId())}
            );
        }

        @Override
        protected void onPostExecute(Integer rowsDeleted) {
            item.setIcon(R.drawable.ic_menu_favorite_add);
            Snackbar.make(parentView, "Deleted from favorites", Snackbar.LENGTH_SHORT).show();
        }
    }
}
