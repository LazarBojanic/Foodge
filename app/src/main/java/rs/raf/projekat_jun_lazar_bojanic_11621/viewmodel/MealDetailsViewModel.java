package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.MealsResponse;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class MealDetailsViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<Meal> fullMealLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public MealDetailsViewModel(){
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        fullMealLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Meal> getFullMealLiveData() {
        return fullMealLiveData;
    }

    public MutableLiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Single<Meal> fetchMealDetails(String idMeal) {
        loadingStatusLiveData.postValue(true); // Set loading status to true

        return mealRepository.fetchMealById(idMeal) // Fetch meal details from the repository
                .flatMap(response -> {
                    List<Meal> meals = response.getMeals();
                    if (meals.isEmpty()) {
                        throw new IllegalArgumentException("No meal found for id: " + idMeal);
                    }

                    Meal meal = meals.get(0);
                    String mealThumbUrl = meal.getStrMealThumb();
                    String fileName = mealThumbUrl.substring(mealThumbUrl.lastIndexOf("/") + 1);

                    return mealRepository.fetchMealImageThumbnail(fileName)
                            .map(imageResponseBody -> {
                                try {
                                    meal.loadMealImageThumbnail(imageResponseBody);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    meal.setMealImageThumbnail(PlaceHolders.getInstance().getPlaceHolderImage());
                                    meal.setMealImage(PlaceHolders.getInstance().getPlaceHolderImage());
                                }
                                return meal;
                            })
                            .onErrorResumeNext(throwable -> {
                                throwable.printStackTrace();
                                meal.setMealImageThumbnail(PlaceHolders.getInstance().getPlaceHolderImage());
                                meal.setMealImage(PlaceHolders.getInstance().getPlaceHolderImage());
                                return Single.just(meal);
                            });
                })
                .doOnSuccess(meal -> {
                    loadingStatusLiveData.postValue(false); // Set loading status to false
                    fullMealLiveData.postValue(meal);
                })
                .doOnError(error -> loadingStatusLiveData.postValue(false)); // Set loading status to false in case of error
    }

}
