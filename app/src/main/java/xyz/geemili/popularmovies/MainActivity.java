package xyz.geemili.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<MovieDb> mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesAdapter = new ArrayAdapter<MovieDb>(
                this,
                R.layout.grid_item_movie,
                R.id.grid_item_movie_textView,
                new ArrayList<MovieDb>()
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
                mMoviesAdapter.clear();
                mMoviesAdapter.addAll(movieDbs);
            }
        }
    }
}
