package com.example.amjad.popularmovies;

import android.content.Context;
import android.content.Intent;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/%s?api_key=%s";
    private static final String API_KEY = "458b917560061314b5ec5669c7555d84";
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private String mSelectedMode;
    private MovieAdaptor mMovieAdaptor;

    public MainActivityFragment() {
        mSelectedMode = "popularity";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getContext();
        mMovieAdaptor = new MovieAdaptor(context, new ArrayList<Movie>());

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        gridView.setAdapter(mMovieAdaptor);

        loadMovies(mSelectedMode);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdaptor.getItem(position);
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

        if (id == R.id.action_sort_popularity && !mSelectedMode.equals("popularity")) {
            mSelectedMode = "popularity";
            loadMovies(mSelectedMode);
            return true;
        } else if (id == R.id.action_sort_rating && !mSelectedMode.equals("rating")) {
            mSelectedMode = "rating";
            loadMovies(mSelectedMode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getEndpointFromSortMode(String sortMode) {
        if (sortMode.equals("popularity"))
            return "popular";
        else if (sortMode.equals("rating"))
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

                    mMovieAdaptor.clear();
                    for (Movie movie : movieList) {
                        if (movie != null) {
                            Log.v(LOG_TAG, "POST EXECUTE IMAGE URLS" + movie);
                            mMovieAdaptor.add(movie);
                        }
                    }
                    mMovieAdaptor.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error loading movies", Toast.LENGTH_SHORT).show();
            }


        });
    }
}
