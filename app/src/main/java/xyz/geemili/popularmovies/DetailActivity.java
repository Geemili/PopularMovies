package xyz.geemili.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by geemili on 2016-08-17.
 */
public class DetailActivity extends AppCompatActivity {

    private ImageView mMoviePosterImageView;
    private TextView mMovieTitleTextView;
    private TextView mOverviewTextView;
    private TextView mUserRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_detail);

        mMoviePosterImageView = (ImageView) findViewById(R.id.movie_poster_imageView);
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_title_textView);
        mOverviewTextView = (TextView) findViewById(R.id.overview_textView);
        mUserRatingTextView = (TextView) findViewById(R.id.user_rating_textView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MovieData")) {
            MovieData data = (MovieData) intent.getParcelableExtra("MovieData");
            mMovieTitleTextView.setText(data.getOriginalTitle());
            mOverviewTextView.setText(data.getPlotSynopsis());
            mUserRatingTextView.setText(String.format(Locale.US, "%.1f", data.getVoteAverage()));
            String imageUrl = "https://image.tmdb.org/t/p/w300"+data.getImagePath();
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mMoviePosterImageView);
        }
    }
}
