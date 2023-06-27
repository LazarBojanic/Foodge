package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IAreaRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IIngredientRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class RemoteMealsFragmentViewModel extends ViewModel {
    private ICategoryRepository categoryRepository;
    private IAreaRepository areaRepository;
    private IIngredientRepository ingredientRepository;
    private IMealRepository mealRepositoryRemote;
    private PublishSubject<String> searchSubjectName;
    private PublishSubject<String> searchSubjectTag;
    private MutableLiveData<List<String>> simpleCategoryListLiveData;
    private MutableLiveData<List<String>> simpleAreaListLiveData;
    private MutableLiveData<List<String>> simpleIngredientListLiveData;
    private MutableLiveData<List<Meal>> remoteMealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public RemoteMealsFragmentViewModel() {
        searchSubjectName = PublishSubject.create();
        searchSubjectTag = PublishSubject.create();
        simpleCategoryListLiveData = new MutableLiveData<>();
        simpleAreaListLiveData = new MutableLiveData<>();
        simpleIngredientListLiveData = new MutableLiveData<>();
        remoteMealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
        categoryRepository = FoodgeApp.getInstance().getRemoteAppComponent().getCategoryRepository();
        areaRepository = FoodgeApp.getInstance().getRemoteAppComponent().getAreaRepository();
        ingredientRepository = FoodgeApp.getInstance().getRemoteAppComponent().getIngredientRepository();
        mealRepositoryRemote = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
    }
    public MutableLiveData<List<String>> getSimpleCategoryListLiveData() {
        return simpleCategoryListLiveData;
    }

    public MutableLiveData<List<String>> getSimpleAreaListLiveData() {
        return simpleAreaListLiveData;
    }

    public MutableLiveData<List<String>> getSimpleIngredientListLiveData() {
        return simpleIngredientListLiveData;
    }

    public MutableLiveData<List<Meal>> getRemoteMealListLiveData() {
        return remoteMealListLiveData;
    }

    public MutableLiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Meal>> fetchAllMealsForCategoryRemote(String strCategory) {
        remoteMealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByCategory(strCategory)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    List<Meal> meals = response != null ? response.getMeals() : Collections.emptyList();
                    return meals;
                })
                .doOnNext(remoteMealListLiveData::postValue);
    }

    public Observable<List<Meal>> fetchAllMealsForAreaRemote(String strArea) {
        remoteMealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByArea(strArea)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    List<Meal> meals = response != null && response.getMeals() != null ? response.getMeals() : Collections.emptyList();
                    return meals;
                })
                .doOnNext(remoteMealListLiveData::postValue);
    }

    public Observable<List<Meal>> fetchAllMealsForIngredientRemote(String strIngredient) {
        remoteMealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByIngredient(strIngredient)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    List<Meal> meals = response != null && response.getMeals() != null ? response.getMeals() : Collections.emptyList();
                    return meals;
                })
                .doOnNext(remoteMealListLiveData::postValue);
    }

    private Observable<List<Meal>> performNameSearchRemote(String searchText) {
        remoteMealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByName(searchText)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    List<Meal> meals = response != null && response.getMeals() != null ? response.getMeals() : Collections.emptyList();
                    return meals;
                })
                .doOnNext(meals -> {
                    if (meals != null) {
                        remoteMealListLiveData.postValue(meals);
                    } else {
                        remoteMealListLiveData.postValue(Collections.emptyList());
                    }
                });
    }
    public Observable<List<String>> fetchAllCategories() {
        return categoryRepository.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Category> categories = new ArrayList<>();
                    if(response != null && response.getCategories() != null){
                        categories = response.getCategories();
                    }
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
                    List<Area> areas = new ArrayList<>();
                    if(response != null && response.getAreas() != null){
                        areas = response.getAreas();
                    }
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
                    List<Ingredient> ingredients = new ArrayList<>();
                    if(response != null && response.getIngredients() != null){
                        ingredients = response.getIngredients();
                    }
                    List<String> ingredientNames = new ArrayList<>();
                    for (Ingredient ingredient : ingredients) {
                        ingredientNames.add(ingredient.getStrIngredient());
                    }
                    return ingredientNames;
                })
                .doOnNext(simpleIngredientListLiveData::postValue)
                .onErrorReturnItem(new ArrayList<>());
    }



    public void setupNameSearchObserver() {
        searchSubjectName
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performNameSearchRemote) // Use switchMap to cancel previous requests
                .subscribe(meals -> remoteMealListLiveData.postValue(meals));
    }

    public void onNameSearchTextChanged(String searchText) {
        searchSubjectName.onNext(searchText);
    }
    public void setupTagSearchObserver() {
        searchSubjectTag
                .debounce(10000, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performTagSearchRemote) // Use switchMap to cancel previous requests
                .subscribe(meals -> remoteMealListLiveData.postValue(meals));
    }

    public void onTagSearchTextChanged(String searchText) {
        searchSubjectTag.onNext(searchText);
    }
    private Observable<List<Meal>> performTagSearchRemote(String searchText) {
        List<Meal> mealList = remoteMealListLiveData.getValue();
        List<Meal> result = new ArrayList<>();
        loadingStatusLiveData.postValue(true);
        if (mealList != null) {
            return Observable.fromIterable(mealList)
                    .flatMapSingle(meal -> mealRepositoryRemote.fetchMealById(meal.getIdMeal())
                            .map(response -> {
                                List<Meal> meals = response.getMeals();
                                if (meals.isEmpty()) {
                                    throw new IllegalArgumentException("No meal found for id: " + meal.getIdMeal());
                                }
                                Meal fullMeal = meals.get(0);
                                if(fullMeal != null){
                                    if(fullMeal.getStrTags() != null){
                                        if (fullMeal.getStrTags().toLowerCase().contains(searchText.toLowerCase())) {
                                            result.add(meal);
                                        }
                                    }
                                }
                                return meal;
                            })
                    )
                    .toList()
                    .doOnSuccess(ignored -> remoteMealListLiveData.postValue(result))
                    .doFinally(() -> loadingStatusLiveData.postValue(false))
                    .toObservable();
        } else {
            return Observable.just(result);
        }
    }



    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
}