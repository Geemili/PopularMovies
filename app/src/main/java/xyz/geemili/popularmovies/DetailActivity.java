package xyz.geemili.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geemili on 2016-08-17.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_poster_imageView) ImageView mMoviePosterImageView;
    @BindView(R.id.movie_title_textView) TextView mMovieTitleTextView;
    @BindView(R.id.overview_textView) TextView mOverviewTextView;
    @BindView(R.id.user_rating_textView) TextView mUserRatingTextView;
    @BindView(R.id.release_date_textView) TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.movie_data_intentExtra))) {
            MovieData data = intent.getParcelableExtra(getString(R.string.movie_data_intentExtra));
            mMovieTitleTextView.setText(data.getOriginalTitle());
            mOverviewTextView.setText(data.getPlotSynopsis());
            mUserRatingTextView.setText(String.format(Locale.US, "%.1f", data.getVoteAverage()));
            mReleaseDateTextView.setText(String.format(Locale.US, "Released %s", data.getReleaseDate()));
            String imageUrl = getString(R.string.tmdb_image_url)+getString(R.string.tmdb_image_size)+data.getImagePath();
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mMoviePosterImageView);
        }
    }
}
