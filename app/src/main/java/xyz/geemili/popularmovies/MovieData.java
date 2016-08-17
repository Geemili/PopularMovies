package xyz.geemili.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geemili on 2016-08-16.
 */
public class MovieData implements Parcelable {
    private int movieId;
    private String imagePath;
    private String originalTitle;
    private String plotSynopsis;
    private float voteAverage;
    private String releaseDate;
    private float popularity;

    public MovieData(int movieId) {
        this.movieId = movieId;
    }


    public int  getMovieId() {
        return movieId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(imagePath);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsis);
        dest.writeFloat(voteAverage);
        dest.writeString(releaseDate);
        dest.writeFloat(popularity);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    private MovieData(Parcel in) {
        movieId = in.readInt();
        imagePath = in.readString();
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        voteAverage = in.readFloat();
        releaseDate = in.readString();
        popularity = in.readFloat();
    }
}
