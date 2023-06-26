package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.graphics.Bitmap;

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
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IAreaRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IIngredientRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class MealsFragmentViewModel extends ViewModel {
    private ICategoryRepository categoryRepository;
    private IAreaRepository areaRepository;
    private IIngredientRepository ingredientRepository;
    private IMealRepository mealRepositoryRemote;
    private PersonalMealDao mealRepositoryLocal;
    private PublishSubject<String> searchSubject;

    private MutableLiveData<List<String>> simpleCategoryListLiveData;
    private MutableLiveData<List<String>> simpleAreaListLiveData;
    private MutableLiveData<List<String>> simpleIngredientListLiveData;
    private MutableLiveData<List<Meal>> mealListLiveData;
    private MutableLiveData<List<PersonalMeal>> personalMealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public MealsFragmentViewModel() {
        searchSubject = PublishSubject.create();
        simpleCategoryListLiveData = new MutableLiveData<>();
        simpleAreaListLiveData = new MutableLiveData<>();
        simpleIngredientListLiveData = new MutableLiveData<>();
        mealListLiveData = new MutableLiveData<>();
        personalMealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
        categoryRepository = FoodgeApp.getInstance().getRemoteAppComponent().getCategoryRepository();
        areaRepository = FoodgeApp.getInstance().getRemoteAppComponent().getAreaRepository();
        ingredientRepository = FoodgeApp.getInstance().getRemoteAppComponent().getIngredientRepository();
        mealRepositoryRemote = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        mealRepositoryLocal = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
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

    public MutableLiveData<List<PersonalMeal>> getPersonalMealListLiveData() {
        return personalMealListLiveData;
    }

    public LiveData<List<Meal>> getMealListLiveData() {
        return mealListLiveData;
    }

    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Meal>> fetchAllMealsForCategoryRemote(String strCategory) {
        mealListLiveData.setValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByCategory(strCategory)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .flatMap(response -> {
                    List<Meal> meals = new ArrayList<>();
                    if(response != null && response.getMeals() != null){
                        meals = response.getMeals();
                    }
                    List<Single<Meal>> mealSingles = new ArrayList<>();
                    for (Meal meal : meals) {
                        Single<Meal> mealSingle = mealRepositoryRemote.fetchMealImageThumbnail(meal.getStrMealThumb().substring(meal.getStrMealThumb().lastIndexOf("/") + 1))
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
    public Observable<List<PersonalMeal>> fetchAllMealsForCategoryLocal(String strCategory) {
        mealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);
        return mealRepositoryLocal.getAllMealsByCategoryName(strCategory)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> {
                    for (PersonalMeal personalMeal : personalMeals) {
                        personalMeal.loadPersonalMealImage();
                    }
                    return personalMeals;
                })
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }

    public Observable<List<Meal>> fetchAllMealsForAreaRemote(String strArea) {
        mealListLiveData.setValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByArea(strArea)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .flatMap(response -> {
                    List<Meal> meals = new ArrayList<>();
                    if(response != null && response.getMeals() != null){
                        meals = response.getMeals();
                    }
                    List<Single<Meal>> mealSingles = new ArrayList<>();
                    for (Meal meal : meals) {
                        Single<Meal> mealSingle = mealRepositoryRemote.fetchMealImageThumbnail(meal.getStrMealThumb().substring(meal.getStrMealThumb().lastIndexOf("/") + 1))
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
    public Observable<List<PersonalMeal>> fetchAllMealsForAreaLocal(String strCategory) {
        mealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);
        return mealRepositoryLocal.getAllMealsByArea(strCategory)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> {
                    for (PersonalMeal personalMeal : personalMeals) {
                        personalMeal.loadPersonalMealImage();
                    }
                    return personalMeals;
                })
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }
    public Observable<List<Meal>> fetchAllMealsForIngredientRemote(String strIngredient) {
        mealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByIngredient(strIngredient)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .flatMap(response -> {
                    List<Meal> meals = new ArrayList<>();
                    if(response != null && response.getMeals() != null){
                        meals = response.getMeals();
                    }
                    List<Single<Meal>> mealSingles = new ArrayList<>();
                    for (Meal meal : meals) {
                        Single<Meal> mealSingle = mealRepositoryRemote.fetchMealImageThumbnail(meal.getStrMealThumb().substring(meal.getStrMealThumb().lastIndexOf("/") + 1))
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
    public Observable<List<PersonalMeal>> fetchAllMealsForIngredientLocal(String strCategory) {
        mealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);
        return mealRepositoryLocal.getAllMealsByIngredient(strCategory)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> {
                    for (PersonalMeal personalMeal : personalMeals) {
                        personalMeal.loadPersonalMealImage();
                    }
                    return personalMeals;
                })
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
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



    public void setupSearchObserverRemote() {
        searchSubject
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performSearchRemote) // Use switchMap to cancel previous requests
                .subscribe(meals -> mealListLiveData.postValue(meals));
    }
    public void setupSearchObserverLocal() {
        searchSubject
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performSearchLocal) // Use switchMap to cancel previous requests
                .subscribe(personalMeals -> personalMealListLiveData.postValue(personalMeals));
    }

    public void onSearchTextChanged(String searchText) {
        searchSubject.onNext(searchText);
    }

    private Observable<List<Meal>> performSearchRemote(String searchText) {
        mealListLiveData.postValue(Collections.emptyList());
        return mealRepositoryRemote.fetchAllMealsByName(searchText)
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    List<Meal> meals = new ArrayList<>();
                    if(response != null && response.getMeals() != null){
                        meals = response.getMeals();
                    }
                    List<Single<Meal>> mealSingles = new ArrayList<>();
                    for (Meal meal : meals) {
                        Single<Meal> mealSingle = mealRepositoryRemote.fetchMealImageThumbnail(meal.getStrMealThumb().substring(meal.getStrMealThumb().lastIndexOf("/") + 1))
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
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    mealListLiveData.postValue(Collections.emptyList());
                    return Observable.empty();
                })
                .doOnNext(meals -> {
                    if (meals != null) {
                        mealListLiveData.postValue(meals);
                    } else {
                        mealListLiveData.postValue(Collections.emptyList());
                    }
                });
    }
    private Observable<List<PersonalMeal>> performSearchLocal(String searchText) {
        mealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);

        return mealRepositoryLocal.getAllMealsByName(searchText)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> {
                    for (PersonalMeal personalMeal : personalMeals) {
                        personalMeal.loadPersonalMealImage();
                    }
                    return personalMeals;
                })
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }


    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
}