package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.MealsResponse;

public interface IMealRepository {
    @GET("api/json/v1/1/lookup.php")
    Single<MealsResponse> fetchMealById(@Query("i") String i);
    @GET("api/json/v1/1/search.php")
    Observable<MealsResponse> fetchAllMealsByName(@Query("s") String s);
    @GET("api/json/v1/1/filter.php")
    Observable<MealsResponse> fetchAllMealsByCategory(@Query("c") String c);
    @GET("api/json/v1/1/filter.php")
    Observable<MealsResponse> fetchAllMealsByArea(@Query("a") String a);
    @GET("api/json/v1/1/filter.php")
    Observable<MealsResponse> fetchAllMealsByIngredient(@Query("i") String i);
    @GET("images/media/meals/{mealImageName}/preview")
    Single<ResponseBody> fetchMealImage(@Path("mealImageName") String mealImageName);
}
