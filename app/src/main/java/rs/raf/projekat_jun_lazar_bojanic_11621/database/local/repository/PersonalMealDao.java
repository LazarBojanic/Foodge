package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;

@Dao
public interface PersonalMealDao {
    @Query("SELECT * FROM personal_meal WHERE id = :id")
    Single<PersonalMeal> getMealById(String id);
    @Query("SELECT * FROM personal_meal WHERE id = :idMeal")
    Single<PersonalMeal> getMealByIdMeal(String idMeal);
    @Query("SELECT * FROM personal_meal")
    Observable<List<PersonalMeal>> getAllMeals();
    @Query("SELECT * FROM personal_meal WHERE strMeal = :strMeal")
    Observable<List<PersonalMeal>> getAllMealsByName(String strMeal);
    @Query("SELECT * FROM personal_meal WHERE strMeal LIKE :firstLetter || '%'")
    Observable<List<PersonalMeal>> getAllMealsByFirstLetter(String firstLetter);
    @Query("SELECT * FROM personal_meal WHERE strCategory = :strCategory")
    Observable<List<PersonalMeal>> getAllMealsByCategoryName(String strCategory);
    @Query("SELECT * FROM personal_meal WHERE strArea = :strArea")
    Observable<List<PersonalMeal>> getAllMealsByArea(String strArea);
    @Query("SELECT * FROM personal_meal WHERE strIngredient = :strIngredient")
    Observable<List<PersonalMeal>> getAllMealsByIngredient(String strIngredient);
    @Query("SELECT * FROM personal_meal WHERE strTags LIKE '%' || :strTag || '%'")
    Observable<List<PersonalMeal>> getAllMealsByTag(String strTag);
    @Query("UPDATE personal_meal SET strMeal = :strMeal WHERE id = :id")
    Completable updatePersonalMealById(Integer id, String strMeal);
    @Query("DELETE FROM personal_meal WHERE id = :id")
    Completable deletePersonalMealById(Integer id);

}
