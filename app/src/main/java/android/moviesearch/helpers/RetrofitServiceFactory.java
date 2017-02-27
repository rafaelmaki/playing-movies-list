package android.moviesearch.helpers;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceFactory {

    public static <T> T createRetrofitService(final Class<T> baseClass,
                                              final String endPoint) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .build();
        T service = retrofit.create(baseClass);

        return service;
    }

    public static <T> T createRetrofitService(final Class<T> baseClass,
                                              final String endPoint,
                                              final GsonConverterFactory gsonConverter,
                                              final RxJavaCallAdapterFactory rxAdapter) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(gsonConverter)
                .addCallAdapterFactory(rxAdapter)
                .build();
        T service = retrofit.create(baseClass);

        return service;
    }
}
