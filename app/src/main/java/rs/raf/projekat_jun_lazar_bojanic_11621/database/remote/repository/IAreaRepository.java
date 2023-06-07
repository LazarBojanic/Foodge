package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;

public interface IAreaRepository {
    @GET("api/json/v1/1/list.php?a=list")
    Observable<List<Area>> fetchAllAreas();
}
