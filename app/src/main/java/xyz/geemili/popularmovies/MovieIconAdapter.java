package xyz.geemili.popularmovies;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by geemili on 2016-08-16.
 */
public class MovieIconAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieIcon> movies;
    @LayoutRes int layoutResource;
    @IdRes int imageViewIdResource;
    private LayoutInflater mInflater;

    public MovieIconAdapter(Context c, @LayoutRes int resource, @IdRes int imageViewResource, List<MovieIcon> movies) {
        this.mContext = c;
        this.mInflater = LayoutInflater.from(mContext);
        this.movies = movies;
        this.layoutResource = resource;
        this.imageViewIdResource = imageViewResource;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; // Figure out what this is...
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ImageView imageView;
        if (convertView == null) {
            view = mInflater.inflate(layoutResource, parent, false);
        } else {
            view = convertView;
        }
        imageView = (ImageView) view.findViewById(imageViewIdResource);

        MovieIcon icon = movies.get(position);
        if (icon != null && icon.getImage() != null) {
            imageView.setImageDrawable(icon.getImage());
        } else {
            imageView.setImageResource(R.drawable.popular_movies_icon);
        }
        return imageView;
    }

    public List<MovieIcon> getList() {
        return movies;
    }
}
