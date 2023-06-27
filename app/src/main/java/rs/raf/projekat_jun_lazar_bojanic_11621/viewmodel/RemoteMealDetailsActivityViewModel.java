package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;

public class RemoteMealDetailsActivityViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<Meal> fullMealLiveData;

    public RemoteMealDetailsActivityViewModel(){
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        fullMealLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Meal> getFullMealLiveData() {
        return fullMealLiveData;
    }

    public Single<Meal> fetchMealDetails(String idMeal) {
        return mealRepository.fetchMealById(idMeal) // Fetch meal details from the repository
                .flatMap(response -> {
                    List<Meal> meals = response.getMeals();
                    if (meals.isEmpty()) {
                        throw new IllegalArgumentException("No meal found for id: " + idMeal);
                    }
                    Meal meal = meals.get(0);
                    fullMealLiveData.postValue(meal);
                    return Single.just(meal);
                });
    }
}
