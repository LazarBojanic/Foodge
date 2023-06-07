package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response.MealsResponse;

public class HomeViewModel extends ViewModel {
    private IMealRepository mealRepository;
    private MutableLiveData<List<Meal>> mealListMutableLiveData;

    public HomeViewModel() {
        mealRepository = FoodgeApp.getInstance().getRemoteAppComponent().getMealRepository();
        mealListMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Meal>> getMealListLiveData() {
        return mealListMutableLiveData;
    }

    public void fetchAllMeals() {
        Log.d(String.valueOf(R.string.foodgeTag), "getting mealList");
        Observable<MealsResponse> observableMealList = mealRepository.fetchAllMealsByFirstLetter("a");
        observableMealList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealsResponse -> {
                            Log.d(String.valueOf(R.string.foodgeTag), "got mealList " + mealsResponse.getMeals());
                            mealListMutableLiveData.setValue(mealsResponse.getMeals());
                        },
                        error -> {
                            Log.d("FOODGE_APP", "Request failed: " + error.getMessage());
                        }
                );
    }
}