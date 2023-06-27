package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class HomeFragmentViewModel extends ViewModel {
    private ICategoryRepository categoryRepository;
    private MutableLiveData<List<Category>> categoryListLiveData;
    private MutableLiveData<Boolean> loadingStatusLiveData;
    private MainActivityViewModel mainActivityViewModel;

    public HomeFragmentViewModel() {
        categoryRepository = FoodgeApp.getInstance().getRemoteAppComponent().getCategoryRepository();
        categoryListLiveData = new MutableLiveData<>();
        loadingStatusLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public MutableLiveData<Boolean> getLoadingStatusLiveData() {
        return loadingStatusLiveData;
    }

    public Observable<List<Category>> fetchAllCategories() {
        List<Category> cachedCategories = mainActivityViewModel.getCachedCategoryList();
        if (cachedCategories != null) {
            setLoadingStatus(false);
            categoryListLiveData.postValue(cachedCategories);
            return Observable.just(cachedCategories);
        }

        // Categories not fetched, make an API call
        return categoryRepository.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> setLoadingStatus(true))
                .doFinally(() -> setLoadingStatus(false))
                .flatMap(response -> {
                    List<Category> categories = response.getCategories();
                    return Observable.just(categories);
                })
                .doOnNext(categories -> {
                    mainActivityViewModel.setCachedCategoryList(categories);
                    categoryListLiveData.postValue(categories);
                })
                .onErrorResumeNext(throwable -> {
                    // Handle the error and return a fallback list of categories or an empty list
                    return Observable.just(new ArrayList<Category>()); // Replace with appropriate fallback logic
                });
    }



    private void setLoadingStatus(boolean isLoading) {
        loadingStatusLiveData.postValue(isLoading);
    }
    public void setMainActivityViewModel(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }
}
