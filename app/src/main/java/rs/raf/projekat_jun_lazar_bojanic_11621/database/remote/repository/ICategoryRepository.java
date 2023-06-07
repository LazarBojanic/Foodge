package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.SimpleCategory;

public interface ICategoryRepository {
    @GET("api/json/v1/1/categories.php")
    Observable<List<Category>> fetchAllCategories();
    @GET("api/json/v1/1/list.php?c=list")
    Observable<List<SimpleCategory>> fetchAllSimpleCategories();
}
