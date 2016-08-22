package xyz.geemili.popularmovies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainActivity extends AppCompatActivity {

    private MovieDataAdapter mMoviesAdapter;
    private GridView mMovieGrid;

    // Saves where the user is looking when the activity is reconfigured (i.e. screen is rotated)
    private int gridViewIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new MoveDataAdapter in case we don't have one
        if (mMoviesAdapter == null) {
            mMoviesAdapter = new MovieDataAdapter(
                    this,
                    R.layout.grid_item_movie,
                    R.id.grid_item_movie_imageView,
                    null
            );
        }

        mMovieGrid = (GridView) findViewById(R.id.gridView_movies);
        assert mMovieGrid != null;
        mMovieGrid.setAdapter(mMoviesAdapter);
        mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(getString(R.string.movie_data_intentExtra), (Parcelable) mMoviesAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save where the user is at in the list
        outState.putInt("gridViewIndex", mMovieGrid.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstaceState) {
        super.onRestoreInstanceState(savedInstaceState);
        gridViewIndex = savedInstaceState.getInt("gridViewIndex");
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String order = sharedPreferences.getString(
                getString(R.string.pref_order_key),
                getString(R.string.pref_order_default)
        );

        task.execute(order);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<MovieDb>> {

        @Override
        protected List<MovieDb> doInBackground(String... params) {
            TmdbMovies movies = new TmdbApi(BuildConfig.TMDB_KEY).getMovies();
            MovieResultsPage page = null;
            switch (params[0]) {
                case "popularity":
                    page = movies.getPopularMovies(getString(R.string.tmdb_language), 0);
                    break;
                case "vote_average":
                    page = movies.getTopRatedMovies(getString(R.string.tmdb_language), 0);
                    break;
            }
            return page.getResults();
        }

        @Override
        protected void onPostExecute(List<MovieDb> movieDbs) {
            if (movieDbs != null) {
                List<MovieData> list = mMoviesAdapter.getList();
                if (list==null) list = new ArrayList<>();
                list.clear();
                for (int i=0; i<movieDbs.size(); i++) {
                    MovieDb db = movieDbs.get(i);

                    MovieData movieData = new MovieData(db.getId());
                    movieData.setImagePath(db.getPosterPath());
                    movieData.setOriginalTitle(db.getOriginalTitle());
                    movieData.setVoteAverage(db.getVoteAverage());
                    movieData.setReleaseDate(db.getReleaseDate());
                    movieData.setPopularity(db.getPopularity());
                    movieData.setPlotSynopsis(db.getOverview());

                    list.add(i, movieData);
                }

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                String order = sharedPreferences.getString(
                        getString(R.string.pref_order_key),
                        getString(R.string.pref_order_default)
                );

                Comparator reverseOrder = Collections.reverseOrder(MovieData.CompareByPopularity);
                switch (order) {
                    case "popularity":
                        reverseOrder = Collections.reverseOrder(MovieData.CompareByPopularity);
                        break;
                    case "vote_average":
                        reverseOrder = Collections.reverseOrder(MovieData.CompareByVoteAverage);
                        break;
                }

                Collections.sort(list, reverseOrder);
                mMoviesAdapter.setList(list);
            }
            // Scroll the the position that the user was at before the activity was reconfigured.
            mMovieGrid.setSelection(gridViewIndex);
        }
    }
}
