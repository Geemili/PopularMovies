package xyz.geemili.popularmovies;

import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainActivity extends AppCompatActivity {

    private MovieIconAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesAdapter = new MovieIconAdapter(
                this,
                R.layout.grid_item_movie,
                R.id.grid_item_movie_imageView,
                new ArrayList<MovieIcon>()
        );

        final GridView gridView = (GridView) findViewById(R.id.gridView_movies);
        gridView.setAdapter(mMoviesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute();
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, List<MovieDb>> {

        @Override
        protected List<MovieDb> doInBackground(Void... params) {
            TmdbMovies movies = new TmdbApi(BuildConfig.TMDB_KEY).getMovies();
            MovieResultsPage page = movies.getPopularMovies("en", 0);
            return page.getResults();
        }

        @Override
        protected void onPostExecute(List<MovieDb> movieDbs) {
            if (movieDbs != null) {
                List<MovieIcon> list = mMoviesAdapter.getList();
                for (int i=0; i<movieDbs.size(); i++) {
                    MovieIcon movieIcon = new MovieIcon(movieDbs.get(i).getId());
                    list.add(i, movieIcon);
                    new FetchMovieImageTask().execute(movieIcon);
                }
                mMoviesAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FetchMovieImageTask extends AsyncTask<MovieIcon, Void, Pair<MovieIcon, Drawable>> {

        private final String LOG_TAG = FetchMovieImageTask.class.getSimpleName();

        @Override
        protected Pair<MovieIcon, Drawable> doInBackground(MovieIcon... params) {
            int movieId = params[0].getMovieId();
            TmdbApi api = new TmdbApi(BuildConfig.TMDB_KEY);
            TmdbMovies movies = api.getMovies();
            MovieImages images = movies.getImages(movieId, "en");
            Artwork art = images.getPosters().get(0);

            InputStream in = null;
            Drawable bmap = null;

            try {
                URL url = new URL(api.getConfiguration().getSecureBaseUrl()+"w300"+art.getFilePath());
                in = url.openStream();

                bmap = BitmapDrawable.createFromStream(in, url.toString());
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error forming image download url", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error downloading image", e);
            } finally {
                if(in!=null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Couldn't close inputstream", e);
                    }
                }
            }
            return new Pair<>(params[0], bmap);
        }

        @Override
        protected void onPostExecute(Pair<MovieIcon, Drawable> pair) {
            pair.first.setImage(pair.second);
            mMoviesAdapter.notifyDataSetChanged();
        }
    }
}
