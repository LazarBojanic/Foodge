package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class MealsFragmentViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<List<Meal>> mealListMutableLiveData;
    private static List<Meal> cachedMealList;
    private MutableLiveData<Boolean> loadingStatusLiveData; // Added loading status LiveData
    private MainActivityViewModel mainActivityViewModel;
    public MealsFragmentViewModel() {
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
        List<Meal> cachedMealList = mainActivityViewModel.getCachedMealList();
        if (cachedMealList != null) {
            setLoadingStatus(false);
            mealListMutableLiveData.postValue(cachedMealList);
            return Observable.just(cachedMealList);
        }
        return mealRepository.fetchAllMealsByFirstLetter("b")
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
                                    return Single.just(new Meal());
                                });

                        mealSingles.add(mealSingle);
                    }
                    return Single.zip(mealSingles, mealList -> {
                        List<Meal> updatedMeals = new ArrayList<>();
                        for (Object meal : mealList) {
                            if (meal instanceof Meal) {
                                updatedMeals.add((Meal) meal);
                            }
                        }
                        return updatedMeals;
                    }).toObservable();
                })
                .doOnNext(meals -> {
                    mainActivityViewModel.setCachedMealList(meals);
                    mealListMutableLiveData.postValue(meals);
                })
                .onErrorResumeNext(throwable -> {
                    // Handle the error and return a fallback list of meals or an empty list
                    return Observable.just(new ArrayList<Meal>());  // Replace with appropriate fallback logic
                });
    }

    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
    public void setMainActivityViewModel(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }
}