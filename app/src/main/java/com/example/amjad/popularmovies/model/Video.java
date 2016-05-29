package com.example.amjad.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    String id;
    String key;
    String name;
    String site;
    int size;
    String type;
    String imageUrl;
    String videoUrl;

    public static final String VIDEO_IMAGE_URL = "http://img.youtube.com/vi/%s/0.jpg";
    public static final String VIDEO_URL = "http://www.youtube.com/watch?v=%s";

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public String getSite() {
        return this.site;
    }

    public int getSize() {
        return this.size;
    }

    public String getType() {
        return this.type;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Video(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;

        this.imageUrl = String.format(VIDEO_IMAGE_URL,this.key);
        this.videoUrl = String.format(VIDEO_URL,this.key);
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();

        this.imageUrl = String.format(VIDEO_IMAGE_URL,this.key);
        this.videoUrl = String.format(VIDEO_URL,this.key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }
}
