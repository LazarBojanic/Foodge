package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.MealsResponse;

public class MealsViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<List<Meal>> mealListMutableLiveData;
    private List<Meal> cachedMealList;
    private MutableLiveData<Boolean> loadingStatusLiveData; // Added loading status LiveData

    public MealsViewModel() {
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        mealListMutableLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Meal>> getMealListLiveData() {
        return mealListMutableLiveData;
    }
    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }
    public Observable<List<Meal>> fetchAllMeals() {
        if (mealListMutableLiveData.getValue() != null) {
            // Categories already fetched, return cached data
            return Observable.just(cachedMealList);
        }

        // Categories not fetched, make an API call
        return mealRepository.fetchAllMealsByFirstLetter("b")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    // Update cached data and LiveData with the fetched categories
                    cachedMealList = response.getMeals();
                    mealListMutableLiveData.postValue(cachedMealList);
                    return cachedMealList;
                });
    }

    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
}