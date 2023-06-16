package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.HttpUrl;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;

public class MealsSearchActivityViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<List<Meal>> mealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public MealsSearchActivityViewModel() {
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        mealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Meal>> getMealListLiveData() {
        return mealListLiveData;
    }

    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Meal>> fetchAllMealsForCategory(String strCategory) {
        return mealRepository.fetchAllMealsByCategory(strCategory)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    // Update cached data and LiveData with the fetched categories
                    List<Meal> meals = response.getMeals();
                    mealListLiveData.postValue(meals);
                    return meals;
                });
    }

    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }

}
