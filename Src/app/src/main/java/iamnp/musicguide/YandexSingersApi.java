package iamnp.musicguide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit service api interface to get singers data
 */
public interface YandexSingersApi {
    @GET("artists.json")
    Call<List<Singer>> getSingers();
}