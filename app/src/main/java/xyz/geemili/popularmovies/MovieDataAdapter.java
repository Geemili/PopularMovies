package xyz.geemili.popularmovies;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by geemili on 2016-08-16.
 */
public class MovieDataAdapter extends BaseAdapter {

    private static final String LOG_TAG = MovieDataAdapter.class.getSimpleName();

    private Context mContext;
    private List<MovieData> movies;
    @LayoutRes int layoutResource;
    @IdRes int imageViewIdResource;
    private LayoutInflater mInflater;

    public MovieDataAdapter(Context c, @LayoutRes int resource, @IdRes int imageViewResource, List<MovieData> movies) {
        this.mContext = c;
        this.mInflater = LayoutInflater.from(mContext);
        this.movies = movies;
        this.layoutResource = resource;
        this.imageViewIdResource = imageViewResource;
    }

    @Override
    public int getCount() {
        if (movies==null) return 0;
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        if (movies==null) return null;
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; // Figure out what this is...
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (movies==null) return convertView;
        View view;
        ImageView imageView;
        if (convertView == null) {
            view = mInflater.inflate(layoutResource, parent, false);
            view.setPadding(8, 8, 8, 8);
        } else {
            view = convertView;
        }
        imageView = (ImageView) view.findViewById(imageViewIdResource);

        MovieData icon = movies.get(position);

        String url = "https://image.tmdb.org/t/p/w300"+icon.getImagePath();

        Picasso.with(mContext)
                .load(url)
                .into(imageView);
        return imageView;
    }

    public List<MovieData> getList() {
        return movies;
    }

    public void setList(List<MovieData> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }
}
