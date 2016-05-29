package com.example.amjad.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amjad.popularmovies.R;
import com.example.amjad.popularmovies.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    Context context;
    private LayoutInflater inflater;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, R.layout.list_item_review, reviews);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Review object from the ArrayAdapter at the appropriate position
        Review review = getItem(position);

        // Adapters recycle views to AdapterViews.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }

        if (review != null) {
            TextView authorTextView = (TextView) convertView.findViewById(R.id.movie_review_author);
            authorTextView.setText(review.getAuthor());

            TextView contentTextView = (TextView) convertView.findViewById(R.id.movie_review_content);
            contentTextView.setText(review.getReview());
        }

        return convertView;
    }
}
