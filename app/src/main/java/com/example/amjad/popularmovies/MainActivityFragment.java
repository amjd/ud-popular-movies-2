package com.example.amjad.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.amjad.popularmovies.adapter.MovieAdapter;
import com.example.amjad.popularmovies.data.MovieContract;
import com.example.amjad.popularmovies.model.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/%s?api_key=%s";
    private static final String API_KEY = "458b917560061314b5ec5669c7555d84";
    private static final String MODE_POPULARITY = "MODE_POPULARITY";
    private static final String MODE_RATING = "MODE_RATING";
    private static final String MODE_FAVORITES = "MODE_FAVORITES";
    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_BACKDROP
    };
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private String mSelectedMode;
    private String mPreviousMode;
    private MovieAdapter mMovieAdapter;

    public MainActivityFragment() {
        mSelectedMode = MODE_POPULARITY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getContext();

        mMovieAdapter = new MovieAdapter(context, new ArrayList<Movie>());

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        gridView.setAdapter(mMovieAdapter);

        loadMovies(mSelectedMode);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                //Toast.makeText(getActivity(), "Movie title:" + movie.title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movieDetail", movie);
                startActivity(intent);
            }

        });
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity && !mSelectedMode.equals(MODE_POPULARITY)) {
            mSelectedMode = MODE_POPULARITY;
            try {
                GridView gridView = (GridView) getView().findViewById(R.id.grid_view_movies);
                gridView.setVisibility(View.GONE);
            } catch(NullPointerException ne) {
                Log.e(LOG_TAG, "Error " + ne);
            }
            try {
                TextView loadingScreenSubtitle = (TextView) getView().findViewById(R.id.loadingScreenSubtitle);
                loadingScreenSubtitle.setText("Loading movies...");
            } catch(NullPointerException ne) {
                Log.e(LOG_TAG, "Error " + ne);
            }
            loadMovies(mSelectedMode);
            return true;
        } else if (id == R.id.action_sort_rating && !mSelectedMode.equals(MODE_RATING)) {
            mSelectedMode = MODE_RATING;
            try {
                GridView gridView = (GridView) getView().findViewById(R.id.grid_view_movies);
                gridView.setVisibility(View.GONE);
            } catch(NullPointerException ne) {
                Log.e(LOG_TAG, "Error " + ne);
            }
            try {
                TextView loadingScreenSubtitle = (TextView) getView().findViewById(R.id.loadingScreenSubtitle);
                loadingScreenSubtitle.setText("Loading movies...");
            } catch(NullPointerException ne) {
                Log.e(LOG_TAG, "Error " + ne);
            }
            loadMovies(mSelectedMode);
            return true;
        } else if (id == R.id.action_display_favorites && !mSelectedMode.equals(MODE_FAVORITES)) {
            Log.v(LOG_TAG, "Tapped on favorites");
            mPreviousMode = mSelectedMode;
            mSelectedMode = MODE_FAVORITES;
            FetchFavoriteMoviesTask task = new FetchFavoriteMoviesTask(getView());
            task.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getEndpointFromSortMode(String sortMode) {
        if (sortMode.equals(MODE_POPULARITY))
            return "popular";
        else if (sortMode.equals(MODE_RATING))
            return "top_rated";
        return "popular";
    }

    private void loadMovies(String sortMode) {
        AsyncHttpClient client = new AsyncHttpClient();
        String requestUrl = String.format(API_BASE_URL, getEndpointFromSortMode(sortMode), API_KEY);
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                try {
                    JSONObject jsonResult = new JSONObject(responseBody);
                    JSONArray jsonMoviesArray = jsonResult.getJSONArray("results");

                    Movie[] movieList = new Movie[20];
                    for (int i = 0; i < jsonMoviesArray.length(); ++i) {
                        JSONObject mov = jsonMoviesArray.getJSONObject(i);
                        movieList[i] = new Movie(mov.getLong("id"),
                                mov.getString("title"),
                                mov.getString("overview"),
                                mov.getString("release_date"),
                                mov.getDouble("popularity"),
                                mov.getDouble("vote_average"),
                                mov.getInt("vote_count"),
                                mov.getString("backdrop_path"),
                                mov.getString("poster_path")
                        );
                    }

                    mMovieAdapter.clear();
                    for (Movie movie : movieList) {
                        if (movie != null) {
                            Log.v(LOG_TAG, "Adding movie to adapter: " + movie);
                            mMovieAdapter.add(movie);
                        }
                    }
                    mMovieAdapter.notifyDataSetChanged();
                    try {
                        GridView gridView = (GridView) getView().findViewById(R.id.grid_view_movies);
                        gridView.setVisibility(View.VISIBLE);
                    } catch(NullPointerException ne) {
                        Log.e(LOG_TAG, "Error " + ne);
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error loading movies", Toast.LENGTH_SHORT).show();
                try {
                    TextView loadingScreenSubtitle = (TextView) getView().findViewById(R.id.loadingScreenSubtitle);
                    loadingScreenSubtitle.setText("Error loading movies. Please try again.");
                } catch(NullPointerException ne) {
                    Log.e(LOG_TAG, "Error " + ne);
                }
            }

        });
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        View parentView;

        FetchFavoriteMoviesTask(View view) {
            this.parentView = view;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            List movies = new ArrayList<Movie>();

            Cursor cursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
            );

            if (cursor != null && cursor.moveToFirst())
            {
                do {
                    Long movieId = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                    String movieTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                    String movieOverview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                    String movieReleaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                    Double moviePopularity = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY));
                    Double movieRating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
                    Long movieVoteCount = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));
                    String movieBackdrop = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP));
                    String moviePoster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));

                    Movie movie = new Movie(movieId, movieTitle, movieOverview, movieReleaseDate, moviePopularity, movieRating, movieVoteCount, movieBackdrop, moviePoster);
                    movies.add(movie);
                    Log.v(LOG_TAG, "Added movie to adapter from database: " + movie.getTitle());

                } while(cursor.moveToNext());
                cursor.close();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if(!movieList.isEmpty()) {
                mMovieAdapter.clear();
                for(final Movie movie: movieList) {
                    mMovieAdapter.add(movie);
                }
                mMovieAdapter.notifyDataSetChanged();
                try {
                    GridView gridView = (GridView) getView().findViewById(R.id.grid_view_movies);
                    gridView.setVisibility(View.VISIBLE);
                } catch(NullPointerException ne) {
                    Log.e(LOG_TAG, "Error " + ne);
                }
            } else {
                Snackbar.make(parentView, "You don't have any favorites.",Snackbar.LENGTH_SHORT).show();
                mSelectedMode = mPreviousMode;
            }
        }
    }
}
