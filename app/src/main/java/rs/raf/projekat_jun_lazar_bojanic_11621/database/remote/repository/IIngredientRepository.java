package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository;

import android.media.Image;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;

public interface IIngredientRepository {
    @GET("api/json/v1/1/list.php?i=list")
    Observable<List<Ingredient>> fetchAllIngredients();
    @GET("images/ingredients/{ingredientImageName}-Small")
    Single<Image> fetchIngredientImageThumbnail(@Path("ingredientImageName") String ingredientImageName);
    @GET("images/ingredients/{ingredientImageName}-Small")
    Single<Image> fetchIngredientImage(@Path("ingredientImageName") String ingredientImageName);
}
