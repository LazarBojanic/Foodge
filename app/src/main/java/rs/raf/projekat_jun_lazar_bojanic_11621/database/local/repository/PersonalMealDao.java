package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;

@Dao
public interface PersonalMealDao {
    @Query("SELECT * FROM personal_meal WHERE id = :id")
    Single<PersonalMeal> getPersonalMealById(String id);
    @Query("SELECT * FROM personal_meal WHERE id = :idMeal")
    Single<PersonalMeal> getPersonalMealByIdMeal(String idMeal);
    @Query("SELECT * FROM personal_meal")
    Observable<List<PersonalMeal>> getAllPersonalMeals();
    @Query("SELECT * FROM personal_meal WHERE strMeal LIKE '%' || :strMeal || '%'")
    Observable<List<PersonalMeal>> getAllPersonalMealsByName(String strMeal);
    @Query("SELECT * FROM personal_meal WHERE strCategory LIKE '%' || :strCategory || '%'")
    Observable<List<PersonalMeal>> getAllPersonalMealsByCategory(String strCategory);
    @Query("SELECT * FROM personal_meal WHERE strIngredient LIKE '%' || :strIngredient || '%'")
    Observable<List<PersonalMeal>> getAllPersonalMealsByIngredient(String strIngredient);
    @Insert
    Single<Long> addPersonalMeal(PersonalMeal personalMeal);
    @Query("UPDATE personal_meal SET strMeal = :strMeal WHERE id = :id")
    Completable updatePersonalMealById(Integer id, String strMeal);
    @Query("DELETE FROM personal_meal WHERE id = :id")
    Completable deletePersonalMealById(Integer id);

}
