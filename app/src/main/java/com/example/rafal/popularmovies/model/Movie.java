package com.example.rafal.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stefan on 2015-11-17.
 */
public class Movie implements Parcelable {

    private String id;
    private String title;
    private String posterPathURLString;
    private String releaseYear;
    private String plotSynopsis;
    private Double userRating;


    public Movie(String vId, String vTitle, String vPosterPathURLString, String vReleaseYear,
                 String vPlotSynopsis, Double vUserRating) {
        setId(vId);
        setTitle(vTitle);
        setPosterPathURLString(vPosterPathURLString);
        setReleaseYear(vReleaseYear);
        setPlotSynopsis(vPlotSynopsis);
        setUserRating(vUserRating);
    }

    private Movie(Parcel in) {
        setId(in.readString());
        setTitle(in.readString());
        setPosterPathURLString(in.readString());
        setReleaseYear(in.readString());
        setPlotSynopsis(in.readString());
        setUserRating(in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getPosterPathURLString());
        parcel.writeString(this.getReleaseYear());
        parcel.writeString(this.getPlotSynopsis());
        parcel.writeDouble(this.getUserRating());

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPathURLString() {
        return posterPathURLString;
    }

    public void setPosterPathURLString(String posterPathURLString) {
        this.posterPathURLString = posterPathURLString;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return id + "--" + title + "--" + posterPathURLString + "--" + releaseYear + "--" +
                plotSynopsis + "--" + userRating;
    }


}
