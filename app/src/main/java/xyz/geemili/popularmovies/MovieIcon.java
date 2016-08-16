package xyz.geemili.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Created by geemili on 2016-08-16.
 */
public class MovieIcon {
    private int movieId;
    private Drawable image;

    public MovieIcon(int movieId) {
        this.movieId = movieId;
    }

    public int  getMovieId() {
        return movieId;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

}
