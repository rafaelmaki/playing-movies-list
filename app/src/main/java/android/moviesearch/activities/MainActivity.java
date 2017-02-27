package android.moviesearch.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.annimon.stream.Stream;

import java.util.List;

import android.moviessearch.R;

import android.moviesearch.fragments.MovieListFragment;
import android.moviesearch.fragments.MovieSearchResultFragment;
import android.moviesearch.models.Movie;

public class MainActivity extends AppCompatActivity {

    private List<Movie> mMovies;

    private MovieListFragment mMovieListFragment;
    private MovieSearchResultFragment mMovieSearchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieListFragment = new MovieListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movies_fragment, mMovieListFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();

                mMovies = mMovieListFragment.getMovies();

                List<Movie> filteredList = queryListByMovieTitle(mMovies, query);

                mMovieSearchResultFragment = MovieSearchResultFragment.newInstance(filteredList);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movies_fragment, mMovieSearchResultFragment)
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movies_fragment, mMovieListFragment)
                    .commit();
            return false;
        });

        return true;
    }

    /**
     * Filters the movie list according to the give vote average
     * @param initialList
     * @param average
     * @return list of movies with vote average greater than @average parameter
     */
    private List<Movie> filterList(List<Movie> initialList, final Double average ) {
        return Stream.of(initialList)
                .filter(m -> m.getVoteAverage() > average)
                .toList();
    }

    private List<Movie> queryListByMovieTitle(List<Movie> initialList, final String title ) {
        return Stream.of(initialList)
                .filter(m -> m.getTitle().toLowerCase().contains(title))
                .toList();
    }

}
