package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository;

import android.media.Image;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.SimpleCategory;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.CategoriesResponse;

public interface ICategoryRepository {
    @GET("api/json/v1/1/categories.php")
    Observable<CategoriesResponse> fetchAllCategories();
    @GET("api/json/v1/1/list.php?c=list")
    Observable<List<SimpleCategory>> fetchAllSimpleCategories();
    @GET("images/category/{categoryImageName}")
    Single<ResponseBody> fetchCategoryImage(@Path("categoryImageName") String categoryImageName);
}
