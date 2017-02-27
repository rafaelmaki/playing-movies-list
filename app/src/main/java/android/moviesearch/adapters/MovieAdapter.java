package android.moviesearch.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.moviessearch.R;
import android.moviesearch.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

    List<Movie> mItems;

    public MovieAdapter() {
        super();
        mItems = new ArrayList<Movie>();
    }

    public void addData(Movie movie) {
        mItems.add(movie);
        notifyDataSetChanged();
    }

    public void setList(List<Movie> list) {
        mItems.clear();
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<Movie> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Movie movie = mItems.get(i);

//        String url = POSTER_BASE_URL + movie.getPosterPath();
//
//        BitmapFactory.Options bmOptions;
//        bmOptions = new BitmapFactory.Options();
//        bmOptions.inSampleSize = 1;
//        Bitmap bmp = loadBitmap(url, bmOptions);
//
//        viewHolder.mIvMoviePoster.setImageBitmap(bmp);

        viewHolder.mTvMovieTitle.setText(movie.getTitle());
        viewHolder.mTvMovieOverview.setText(movie.getOverview());
        viewHolder.mTvVoteAverage.setText(movie.getVoteAverage().toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvMoviePoster;
        public TextView mTvMovieTitle;
        public TextView mTvMovieOverview;
        public TextView mTvVoteAverage;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            mTvMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            mTvMovieOverview = (TextView) itemView.findViewById(R.id.tv_movie_overview);
            mTvVoteAverage = (TextView) itemView.findViewById(R.id.tv_movie_vote);
        }
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = openHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException ex) {

        }
        return bitmap;
    }

    private static InputStream openHttpConnection(String strURL) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw ex;
        }
        return inputStream;
    }
}
