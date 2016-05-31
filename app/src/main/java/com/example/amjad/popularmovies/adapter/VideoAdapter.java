package com.example.amjad.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amjad.popularmovies.R;
import com.example.amjad.popularmovies.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends ArrayAdapter<Video> {
    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();
    Context context;
    private LayoutInflater inflater;

    public VideoAdapter(Context context, ArrayList<Video> videos) {
        super(context, R.layout.item_video, videos);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Video object from the ArrayAdapter at the appropriate position
        Video video = getItem(position);

        // Adapters recycle views to AdapterViews.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);
        }

        if(video != null) {
            Picasso.with(context)
            .load(video.getImageUrl())
            .into((ImageView) convertView.findViewById(R.id.movie_video_image));
        }

        TextView videoTitleView = (TextView) convertView.findViewById(R.id.movie_video_title);
        videoTitleView.setText(video.getName());

        return convertView;
    }
}
