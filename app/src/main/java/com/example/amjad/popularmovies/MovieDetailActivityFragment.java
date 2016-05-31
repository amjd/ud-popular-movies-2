package com.example.amjad.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amjad.popularmovies.model.Movie;
import com.example.amjad.popularmovies.adapter.ReviewAdapter;
import com.example.amjad.popularmovies.adapter.VideoAdapter;
import com.example.amjad.popularmovies.model.Review;
import com.example.amjad.popularmovies.model.Video;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

public class MovieDetailActivityFragment extends Fragment {
    private Movie mMovie;
    private Context mContext;
    private ReviewAdapter mReviewAdapter;
    private VideoAdapter mVideoAdapter;

    private static final String API_KEY = "458b917560061314b5ec5669c7555d84";

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mMovie != null) {
            fetchReviews();
            fetchVideos();
            Log.i(LOG_TAG,"Inside onStart, movie is not null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle arguments = getArguments();
        mContext = getActivity();

        mMovie = getActivity().getIntent().getExtras().getParcelable("movieDetail");

        // Create an adapter for reviews, and bind it with corresponding layout element
        mReviewAdapter = new ReviewAdapter(mContext, new ArrayList<Review>());
        ExpandableHeightListView reviewListView = (ExpandableHeightListView) rootView.findViewById(R.id.listMovieReviews);
        reviewListView.setAdapter(mReviewAdapter);
        reviewListView.setExpanded(true);
        reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Review review = mReviewAdapter.getItem(i);
                review.toggleReviewMode();
                mReviewAdapter.notifyDataSetChanged();
            }
        });


        // Create an adapter for videos, and bind it with corresponding layout element
        mVideoAdapter = new VideoAdapter(mContext, new ArrayList<Video>());
        ExpandableHeightListView videoListView = (ExpandableHeightListView) rootView.findViewById(R.id.listMovieVideos);
        videoListView.setExpanded(true);
        videoListView.setAdapter(mVideoAdapter);


        // Launch browser or YouTube app to view the trailer
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video video = mVideoAdapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(video.getVideoUrl()));
                startActivity(intent);
            }
        });

        if (mMovie != null)
        {
            String releaseDate = mMovie.getReleaseDate();
            try {
                DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                DateFormat dfOut = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                Date cleanDate = dfIn.parse(releaseDate);
                ((TextView) rootView.findViewById(R.id.releaseDate)).setText(String.format("Released on: %s", dfOut.format(cleanDate)));
            } catch (ParseException pe) {
                Log.e(LOG_TAG, "Error", pe);
            }

            ((TextView) rootView.findViewById(R.id.movieTitle)).setText(mMovie.getTitle());
            ((TextView) rootView.findViewById(R.id.overviewText)).setText(mMovie.getOverview());
            ((TextView) rootView.findViewById(R.id.rating)).setText(String.format("Rating: %.1f/10", mMovie.getVoteAverage()));

            ImageView movieThumbnail = (ImageView) rootView.findViewById(R.id.moviePosterDetail);
            Picasso.with(getContext()).load(mMovie.getPosterUrl()).into(movieThumbnail);
        }
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

            if(id == R.id.action_share)
            {
                if(!mVideoAdapter.isEmpty())
                {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mVideoAdapter.getItem(0).getVideoUrl());
                    startActivity(Intent.createChooser(shareIntent, "Share trailer using"));
                }
                else {
                    Toast.makeText(mContext, "No trailers available to share", Toast.LENGTH_SHORT);
                }
            }

        return super.onOptionsItemSelected(item);
    }

    private void fetchReviews() {
        AsyncHttpClient client = new AsyncHttpClient();
        String REVIEW_URL = "http://api.themoviedb.org/3/movie/%d/reviews?api_key=%s";
        String requestUrl = String.format(REVIEW_URL, mMovie.getId(), API_KEY);
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                try {
                    JSONObject jsonResult = new JSONObject(responseBody);
                    JSONArray jsonReviewsArray = jsonResult.getJSONArray("results");

                    Review[] reviewList = new Review[10];
                    for (int i = 0; i < jsonReviewsArray.length(); ++i) {
                        JSONObject rev = jsonReviewsArray.getJSONObject(i);
                        reviewList[i] = new Review(rev.getString("id"),
                                rev.getString("author"),
                                rev.getString("content"),
                                rev.getString("url")
                        );
                    }

                    mReviewAdapter.clear();
                    for (Review review : reviewList) {
                        if (review != null) {
                            Log.v(LOG_TAG, "Review " + review);
                            mReviewAdapter.add(review);
                        }
                    }
                    mReviewAdapter.notifyDataSetChanged();

                    if (mReviewAdapter.isEmpty()) {
                        try {
                            TextView reviewPlaceholderText = (TextView) getView().findViewById(R.id.reviewPlaceholderText);
                            reviewPlaceholderText.setText(R.string.no_reviews_found);
                        } catch(NullPointerException ne) {
                            Log.v(LOG_TAG, "Error ", ne);
                        }
                    }
                    else {
                        try {
                            TextView reviewPlaceholderText = (TextView) getView().findViewById(R.id.reviewPlaceholderText);
                            reviewPlaceholderText.setVisibility(View.GONE);
                        } catch(NullPointerException ne) {
                            Log.v(LOG_TAG, "Error ", ne);
                        }
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Toast.makeText(mContext, "Error loading reviews", Toast.LENGTH_SHORT).show();
                try {
                    TextView reviewPlaceholderText = (TextView) getView().findViewById(R.id.reviewPlaceholderText);
                    reviewPlaceholderText.setText("Couldn't retrieve reviews.");
                } catch(NullPointerException ne) {
                    Log.v(LOG_TAG, "Error ", ne);
                }
            }


        });

    }

    private void fetchVideos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String VIDEO_URL = "http://api.themoviedb.org/3/movie/%d/videos?api_key=%s";
        String requestUrl = String.format(VIDEO_URL, mMovie.getId(), API_KEY);
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                try {
                    JSONObject jsonResult = new JSONObject(responseBody);
                    JSONArray jsonVideosArray = jsonResult.getJSONArray("results");

                    Video[] videoList = new Video[10];
                    for (int i = 0; i < jsonVideosArray.length(); ++i) {
                        JSONObject vid = jsonVideosArray.getJSONObject(i);
                        videoList[i] = new Video(vid.getString("id"),
                                vid.getString("key"),
                                vid.getString("name"),
                                vid.getString("site"),
                                vid.getInt("size"),
                                vid.getString("type")
                        );
                    }

                    mVideoAdapter.clear();
                    for (Video video : videoList) {
                        if (video != null) {
                            Log.v(LOG_TAG, "Video " + video);
                            mVideoAdapter.add(video);
                        }
                    }
                    mVideoAdapter.notifyDataSetChanged();

                    if (mVideoAdapter.isEmpty()) {
                        try {
                            TextView videoPlaceholderText = (TextView) getView().findViewById(R.id.videoPlaceholderText);
                            videoPlaceholderText.setText(R.string.no_reviews_found);
                        } catch(NullPointerException ne) {
                            Log.v(LOG_TAG, "Error ", ne);
                        }
                    }
                    else {
                        try {
                            TextView videoPlaceholderText = (TextView) getView().findViewById(R.id.videoPlaceholderText);
                            videoPlaceholderText.setVisibility(View.GONE);
                        } catch(NullPointerException ne) {
                            Log.v(LOG_TAG, "Error ", ne);
                        }
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Toast.makeText(mContext, "Error loading trailers", Toast.LENGTH_SHORT).show();
                try {
                    TextView videoPlaceholderText = (TextView) getView().findViewById(R.id.videoPlaceholderText);
                    videoPlaceholderText.setText("Couldn't retrieve trailers.");
                } catch(NullPointerException ne) {
                    Log.v(LOG_TAG, "Error ", ne);
                }
            }


        });

    }
}
