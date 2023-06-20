package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IAreaRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IIngredientRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.CategoriesResponse;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class MealsSearchActivityViewModel extends ViewModel {
    private ICategoryRepository categoryRepository;
    private IAreaRepository areaRepository;
    private IIngredientRepository ingredientRepository;
    private IMealRepository mealRepository;
    private MutableLiveData<List<String>> simpleCategoryListLiveData;
    private MutableLiveData<List<String>> simpleAreaListLiveData;
    private MutableLiveData<List<String>> simpleIngredientListLiveData;
    private MutableLiveData<List<Meal>> mealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public MealsSearchActivityViewModel() {
        simpleCategoryListLiveData = new MutableLiveData<>();
        simpleAreaListLiveData = new MutableLiveData<>();
        simpleIngredientListLiveData = new MutableLiveData<>();
        mealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
        categoryRepository = FoodgeApp.getInstance().getRemoteAppComponent().getCategoryRepository();
        areaRepository = FoodgeApp.getInstance().getRemoteAppComponent().getAreaRepository();
        ingredientRepository = FoodgeApp.getInstance().getRemoteAppComponent().getIngredientRepository();
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
    }
    public LiveData<List<String>> getSimpleCategoryListLiveData() {
        return simpleCategoryListLiveData;
    }

    public MutableLiveData<List<String>> getSimpleAreaListLiveData() {
        return simpleAreaListLiveData;
    }

    public MutableLiveData<List<String>> getSimpleIngredientListLiveData() {
        return simpleIngredientListLiveData;
    }

    public LiveData<List<Meal>> getMealListLiveData() {
        return mealListLiveData;
    }

    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Meal>> fetchAllMealsForCategory(String strCategory) {
        mealListLiveData.setValue(Collections.emptyList());
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
    public Observable<List<Meal>> fetchAllMealsForArea(String strArea) {
        mealListLiveData.setValue(Collections.emptyList());
        return mealRepository.fetchAllMealsByArea(strArea)
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

    public Observable<List<Meal>> fetchAllMealsForIngredient(String strIngredient) {
        mealListLiveData.setValue(Collections.emptyList());
        return mealRepository.fetchAllMealsByIngredient(strIngredient)
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

    public Observable<List<String>> fetchAllCategories() {
        return categoryRepository.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Category> categories = response.getCategories();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getStrCategory());
                    }
                    return categoryNames;
                })
                .doOnNext(simpleCategoryListLiveData::postValue)
                .onErrorReturnItem(new ArrayList<>());
    }
    public Observable<List<String>> fetchAllAreas() {
        return areaRepository.fetchAllAreas()
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Area> areas = response.getAreas();
                    List<String> areaNames = new ArrayList<>();
                    for (Area area : areas) {
                        areaNames.add(area.getStrArea());
                    }
                    return areaNames;
                })
                .doOnNext(simpleAreaListLiveData::postValue)
                .onErrorReturnItem(new ArrayList<>());
    }
    public Observable<List<String>> fetchAllIngredients() {
        return ingredientRepository.fetchAllIngredients()
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Ingredient> ingredients = response.getIngredients();
                    List<String> ingredientNames = new ArrayList<>();
                    for (Ingredient ingredient : ingredients) {
                        ingredientNames.add(ingredient.getStrIngredient());
                    }
                    return ingredientNames;
                })
                .doOnNext(simpleIngredientListLiveData::postValue)
                .onErrorReturnItem(new ArrayList<>());
    }


    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }

}
