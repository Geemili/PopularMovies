package xyz.geemili.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesAdapter = new MovieDataAdapter(
                this,
                R.layout.grid_item_movie,
                R.id.grid_item_movie_imageView,
                new ArrayList<MovieData>()
        );

        final GridView gridView = (GridView) findViewById(R.id.gridView_movies);
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("MovieData", (Parcelable) mMoviesAdapter.getItem(position));
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
                List<MovieData> list = mMoviesAdapter.getList();
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
                mMoviesAdapter.notifyDataSetChanged();
            }
        }
    }
}
