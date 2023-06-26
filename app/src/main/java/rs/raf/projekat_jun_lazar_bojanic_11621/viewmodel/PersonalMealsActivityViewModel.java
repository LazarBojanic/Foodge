package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

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

public class PersonalMealsActivityViewModel extends ViewModel {
    PersonalMealDao personalMealDao;
    private PublishSubject<String> nameSearchSubject;
    private PublishSubject<String> categorySearchSubject;
    private PublishSubject<String> ingredientSearchSubject;
    private MutableLiveData<List<PersonalMeal>> personalMealListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;

    public PersonalMealsActivityViewModel() {
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
        nameSearchSubject = PublishSubject.create();
        categorySearchSubject = PublishSubject.create();
        ingredientSearchSubject = PublishSubject.create();
        personalMealListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<PersonalMeal>> getPersonalMealListLiveData() {
        return personalMealListLiveData;
    }
    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<PersonalMeal>> fetchAllMealsLocal() {
        personalMealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);
        return personalMealDao.getAllMeals()
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> personalMeals)
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }

    public Observable<List<PersonalMeal>> fetchAllMealsForCategoryLocal(String strCategory) {
        personalMealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);
        return personalMealDao.getAllMealsByCategoryName(strCategory)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> personalMeals)
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }



    public void setupNameSearchObserver() {
        nameSearchSubject
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performNameSearchLocal) // Use switchMap to cancel previous requests
                .subscribe(personalMeals -> personalMealListLiveData.postValue(personalMeals));
    }

    public void onNameSearchTextChanged(String searchText) {
        nameSearchSubject.onNext(searchText);
    }

    private Observable<List<PersonalMeal>> performNameSearchLocal(String searchText) {
        personalMealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);

        return personalMealDao.getAllMealsByName(searchText)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> personalMeals)
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }
    public void setupCategorySearchObserver() {
        categorySearchSubject
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performCategorySearchLocal) // Use switchMap to cancel previous requests
                .subscribe(personalMeals -> personalMealListLiveData.postValue(personalMeals));
    }

    public void onCategorySearchTextChanged(String searchText) {
        categorySearchSubject.onNext(searchText);
    }

    private Observable<List<PersonalMeal>> performCategorySearchLocal(String searchText) {
        personalMealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);

        return personalMealDao.getAllMealsByCategoryName(searchText)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> personalMeals)
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }
    public void setupIngredientSearchObserver() {
        ingredientSearchSubject
                .debounce(300, TimeUnit.MILLISECONDS) // Delay to avoid sending too frequent requests
                .distinctUntilChanged() // Only trigger if the text has changed
                .subscribeOn(Schedulers.io())
                .switchMap(this::performIngredientSearchLocal) // Use switchMap to cancel previous requests
                .subscribe(personalMeals -> personalMealListLiveData.postValue(personalMeals));
    }

    public void onIngredientSearchTextChanged(String searchText) {
        ingredientSearchSubject.onNext(searchText);
    }

    private Observable<List<PersonalMeal>> performIngredientSearchLocal(String searchText) {
        personalMealListLiveData.postValue(Collections.emptyList());
        loadingStatusLiveData.postValue(true);

        return personalMealDao.getAllMealsByIngredient(searchText)
                .subscribeOn(Schedulers.io())
                .map(personalMeals -> personalMeals)
                .doFinally(() -> loadingStatusLiveData.postValue(false))
                .doOnNext(personalMealListLiveData::postValue);
    }




    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
}