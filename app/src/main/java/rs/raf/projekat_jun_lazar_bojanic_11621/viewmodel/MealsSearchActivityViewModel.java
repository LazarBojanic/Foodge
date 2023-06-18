package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class MealsSearchActivityViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<List<Meal>> mealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public MealsSearchActivityViewModel() {
        mealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
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
                .flatMap(response -> {
                    List<Meal> meals = response.getMeals();

                    List<Single<Meal>> mealSingles = new ArrayList<>();
                    for (Meal meal : meals) {
                        Single<Meal> mealSingle = mealRepository.fetchMealImageThumbnail(meal.getStrMealThumb().substring(meal.getStrMealThumb().lastIndexOf("/") + 1))
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe(disposable -> setLoadingStatus(true))
                                .doFinally(() -> setLoadingStatus(false))
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

                        mealSingles.add(mealSingle);
                    }

                    return Single.zip(mealSingles, mealList -> {
                        List<Meal> updatedMeals = new ArrayList<>();
                        for (Object meal : mealList) {
                            updatedMeals.add((Meal) meal);
                        }
                        return updatedMeals;
                    }).toObservable();
                })
                .doOnNext(meals -> mealListLiveData.postValue(meals));
    }


    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }

}
