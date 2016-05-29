package com.example.amjad.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Review implements Parcelable {

    String id;
    String author;
    String content;
    String url;
    private String mReviewMode = "short";

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return this.author;
    }

    public String getReview() {
        if(mReviewMode.equals("short"))
            return this.getTruncatedReview();
        else
            return this.content + "... Read less";
    }

    public void toggleReviewMode() {
        if(mReviewMode.equals("short"))
            mReviewMode = "long";
        else
            mReviewMode = "short";
    }

    public String getTruncatedReview() {
        List<String> words = new ArrayList<String>();
        List<String> truncatedWords;
        String[] splitWords = content.split(" ");
        String truncatedReview = "";

        for (String word: splitWords) {
            words.add(word);
        }
        if(words.size() > 40)
        {
            truncatedWords = words.subList(0,40);
            for(String word: truncatedWords) {
                truncatedReview = truncatedReview + word + " ";
            }
            truncatedReview = truncatedReview + "... Read more";
        }
        else
        {
            for(String word: words) {
                truncatedReview = truncatedReview + word + " ";
            }
        }

        return truncatedReview;        
    }

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }
}
