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
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieDataAdapter mMoviesAdapter;
    @BindView(R.id.gridView_movies) GridView mMovieGrid;

    private int gridViewIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create a new MoveDataAdapter in case we don't have one
        if (mMoviesAdapter == null) {
            mMoviesAdapter = new MovieDataAdapter(
                    this,
                    R.layout.grid_item_movie,
                    R.id.grid_item_movie_imageView,
                    null
            );
        }

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
        outState.putInt(getString(R.string.first_index_gridView), mMovieGrid.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gridViewIndex = savedInstanceState.getInt(getString(R.string.first_index_gridView));
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask(this, mMoviesAdapter);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String order = sharedPreferences.getString(
                getString(R.string.pref_order_key),
                getString(R.string.pref_order_default)
        );

        // Set gridviews position to the previous position when the list is updated
        task.setPostExecuteListener(new FetchMoviesTask.OnPostExecuteListener() {
            @Override
            public void onPostExecute() {
                mMovieGrid.setSelection(gridViewIndex);
            }
        });

        task.execute(order);
    }
}
