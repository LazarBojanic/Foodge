package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMealCountByDate;

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
    @Query("SELECT * FROM personal_meal WHERE recipe LIKE '%' || :strIngredient || '%'")
    Observable<List<PersonalMeal>> getAllPersonalMealsByIngredient(String strIngredient);
    @Insert
    Single<Long> addPersonalMeal(PersonalMeal personalMeal);
    @Query("UPDATE personal_meal SET strMeal = :strMeal, strCategory = :strCategory, mealType = :mealType, " +
            "strInstructions = :strInstructions, recipe = :recipe, strYoutube = :strYoutube, " +
            "mealImagePath = :mealImagePath, dateOfPrep = :dateOfPrep WHERE id = :id")
    Completable updatePersonalMealById(Integer id, String strMeal, String strCategory, String mealType,
                                   String strInstructions, String recipe, String strYoutube,
                                   String mealImagePath, String dateOfPrep);
    @Query("DELETE FROM personal_meal WHERE id = :id")
    Completable deletePersonalMealById(Integer id);
    @Query("SELECT COUNT(*) AS personalMealCount, dateOfPrep " +
            "FROM personal_meal " +
            "WHERE dateOfPrep > strftime('%Y-%m-%d', 'now', '-7 days') " +
            "GROUP BY dateOfPrep " +
            "ORDER BY dateOfPrep DESC")
    Observable<List<PersonalMealCountByDate>> getPersonalMealCountsByDate();

}
