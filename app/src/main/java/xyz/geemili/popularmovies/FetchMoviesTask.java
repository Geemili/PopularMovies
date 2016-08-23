package xyz.geemili.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by geemili on 2016-08-23.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieDb>> {

    private Context mContext;
    private MovieDataAdapter mMovieDataAdapter;
    private OnPostExecuteListener postExecuteListener;

    public FetchMoviesTask(Context mContext, MovieDataAdapter mMovieDataAdapter) {
        this.mContext = mContext;
        this.mMovieDataAdapter = mMovieDataAdapter;
    }

    public void setPostExecuteListener(OnPostExecuteListener listener) {
        this.postExecuteListener = listener;
    }

    @Override
    protected List<MovieDb> doInBackground(String... params) {
        TmdbMovies movies = new TmdbApi(BuildConfig.TMDB_KEY).getMovies();
        MovieResultsPage page = null;
        switch (params[0]) {
            case "popularity":
                page = movies.getPopularMovies(mContext.getString(R.string.tmdb_language), 0);
                break;
            case "vote_average":
                page = movies.getTopRatedMovies(mContext.getString(R.string.tmdb_language), 0);
                break;
        }
        return page.getResults();
    }

    @Override
    protected void onPostExecute(List<MovieDb> movieDbs) {
        if (movieDbs != null) {
            List<MovieData> list = mMovieDataAdapter.getList();
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
                    .getDefaultSharedPreferences(mContext);
            String order = sharedPreferences.getString(
                    mContext.getString(R.string.pref_order_key),
                    mContext.getString(R.string.pref_order_default)
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
            mMovieDataAdapter.setList(list);

            if (postExecuteListener != null) {
                postExecuteListener.onPostExecute();
            }
        }
    }

    public interface OnPostExecuteListener {
        public void onPostExecute();
    }
}