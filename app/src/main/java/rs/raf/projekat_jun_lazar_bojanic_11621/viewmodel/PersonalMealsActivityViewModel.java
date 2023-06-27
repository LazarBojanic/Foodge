package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;

public class PersonalMealsActivityViewModel extends ViewModel {
    PersonalMealDao personalMealDao;
    private PublishSubject<String> nameSearchSubject;
    private PublishSubject<String> categorySearchSubject;
    private PublishSubject<String> ingredientSearchSubject;
    private MutableLiveData<List<PersonalMeal>> personalMealListLiveData;

    public PersonalMealsActivityViewModel() {
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
        nameSearchSubject = PublishSubject.create();
        categorySearchSubject = PublishSubject.create();
        ingredientSearchSubject = PublishSubject.create();
        personalMealListLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<PersonalMeal>> getPersonalMealListLiveData() {
        return personalMealListLiveData;
    }

    public Observable<List<PersonalMeal>> fetchAllMealsLocal() {
        personalMealListLiveData.postValue(Collections.emptyList());
        return personalMealDao.getAllPersonalMeals()
                .subscribeOn(Schedulers.io())
                .map(meals -> {
                    personalMealListLiveData.postValue(meals);
                    return meals;
                });
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
        return personalMealDao.getAllPersonalMealsByName(searchText)
                .subscribeOn(Schedulers.io())
                .map(meals -> {
                    personalMealListLiveData.postValue(meals);
                    return meals;
                });
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

        return personalMealDao.getAllPersonalMealsByCategory(searchText)
                .subscribeOn(Schedulers.io())
                .map(meals -> {
                    personalMealListLiveData.postValue(meals);
                    return meals;
                });
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

        return personalMealDao.getAllPersonalMealsByIngredient(searchText)
                .subscribeOn(Schedulers.io())
                .map(meals -> {
                    personalMealListLiveData.postValue(meals);
                    return meals;
                });
    }
}