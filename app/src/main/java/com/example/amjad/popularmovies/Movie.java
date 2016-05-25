package com.example.amjad.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amjad on 27/3/16.
 */
public class Movie implements Parcelable {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    long id;
    String title;
    String overview;
    String releaseDate;
    double popularity;
    double voteAverage;
    long voteCount;
    String backdropPath;
    String posterPath;
    String backdropUrl;
    String posterUrl;

    public Movie(long id, String title, String overview, String releaseDate, double popularity, double voteAverage, long voteCount, String backdropPath, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;

        this.posterUrl = IMAGE_BASE_URL + "w342" + posterPath;
        this.backdropUrl = IMAGE_BASE_URL + "w500" + backdropPath;
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.popularity = in.readDouble();
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
        this.backdropPath = in.readString();
        this.posterPath = in.readString();

        this.posterUrl = IMAGE_BASE_URL + "w342" + posterPath;
        this.backdropUrl = IMAGE_BASE_URL + "original" + backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.voteCount);
        dest.writeString(this.backdropPath);
        dest.writeString(this.posterPath);
    }
}
