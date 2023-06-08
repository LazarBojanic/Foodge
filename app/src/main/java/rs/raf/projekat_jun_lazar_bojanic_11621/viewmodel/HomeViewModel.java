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
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.CategoriesResponse;

public class HomeViewModel extends ViewModel {
    private ICategoryRepository categoryRepository;
    private MutableLiveData<List<Category>> categoryListLiveData;
    private List<Category> cachedCategoryList;
    private MutableLiveData<Boolean> loadingStatusLiveData; // Added loading status LiveData


    public HomeViewModel() {
        categoryRepository = FoodgeApp.getInstance().getRemoteAppComponent().getCategoryRepository();
        categoryListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public LiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Category>> fetchAllCategories() {
        if (categoryListLiveData.getValue() != null) {
            // Categories already fetched, return cached data
            return Observable.just(cachedCategoryList);
        }

        // Categories not fetched, make an API call
        return categoryRepository.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .map(response -> {
                    // Update cached data and LiveData with the fetched categories
                    cachedCategoryList = response.getCategories();
                    categoryListLiveData.postValue(cachedCategoryList);
                    return cachedCategoryList;
                });
    }

    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
}
