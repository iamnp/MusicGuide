package iamnp.musicguide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface YandexSingersApi {
    @GET("artists.json")
    Call<List<Singer>> getSingers();
}