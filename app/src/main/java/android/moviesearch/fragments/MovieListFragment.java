package android.moviesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.moviessearch.R;
import android.moviesearch.adapters.MovieAdapter;
import android.moviesearch.helpers.RetrofitServiceFactory;
import android.moviesearch.interfaces.MovieDataBaseApiInterface;
import android.moviesearch.listeners.EndlessRecyclerViewScrollListener;
import android.moviesearch.models.Movie;
import android.moviesearch.models.NowPlayingMoviesPage;

public class MovieListFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;

    private static List<Movie> mMovieList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EndlessRecyclerViewScrollListener scrollListener;

    private int mPage;
    private int mTotalPages;
    private String mApiKey;
    private String mLang;
    private Gson mGson;

    private List<Movie> mMovies;

    private MovieDataBaseApiInterface mMovieDataBaseInterface;

    private RxJavaCallAdapterFactory mRxAdapter = RxJavaCallAdapterFactory.create();

    private static final Double sMinimumAverage  = 5.0;

    public List<Movie> getMovies() {
        return mMovies;
    }

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(List<Movie> list) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        mMovieList = list;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager)mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(mPage < mTotalPages) {
                    callNowPlayingMovies(++mPage);
                }
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);

        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mApiKey = getString(R.string.movie_db_api_key);
        mLang = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
        mPage = 1;

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        mMovieDataBaseInterface = RetrofitServiceFactory.createRetrofitService(
                MovieDataBaseApiInterface.class, MovieDataBaseApiInterface.BASE_URL,
                GsonConverterFactory.create(mGson), mRxAdapter);

        mMovies = new ArrayList<>();

        callNowPlayingMovies(mPage);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void callNowPlayingMovies(int page) {
        Observable<NowPlayingMoviesPage> playingMoviesPage =
                mMovieDataBaseInterface.nowPlayingMovies(mApiKey, mLang, page);

        playingMoviesPage.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NowPlayingMoviesPage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR: ", e.getMessage());
                        String message =
                                getString(R.string.error_get_movies_list) + ": " + e.getMessage();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(NowPlayingMoviesPage nowPlayingMoviesPage) {
                        mTotalPages = nowPlayingMoviesPage.getTotalPages();

                        List<Movie> list =
                                filterList(nowPlayingMoviesPage.getMovies(), sMinimumAverage);

                        ((MovieAdapter)mAdapter).addList(list);
                        mMovies.addAll(list);
                    }
                });
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
}
