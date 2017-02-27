package android.moviesearch.interfaces;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import android.moviesearch.models.NowPlayingMoviesPage;

public interface MovieDataBaseApiInterface {

    public String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/now_playing")
    Observable<NowPlayingMoviesPage> nowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
