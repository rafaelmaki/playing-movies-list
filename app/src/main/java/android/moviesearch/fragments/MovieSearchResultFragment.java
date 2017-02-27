package android.moviesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import android.moviessearch.R;
import android.moviesearch.adapters.MovieAdapter;
import android.moviesearch.models.Movie;

public class MovieSearchResultFragment extends Fragment {

    private List<Movie> mMovies;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieAdapter mAdapter;

    public MovieSearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param list Movies List.
     * @return A new instance of fragment MovieSearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieSearchResultFragment newInstance(List<Movie> list) {
        MovieSearchResultFragment fragment = new MovieSearchResultFragment();

        fragment.mMovies = list;

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
        View view = inflater.inflate(R.layout.fragment_movie_search_result, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_search_result_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setList(mMovies);

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
}
