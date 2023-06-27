package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.PersonalMealDetailsActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;

public class PersonalMealDetailsActivityViewModel extends ViewModel {
    private MutableLiveData<PersonalMeal> fullPersonalMealLiveData;
    private PersonalMealDao personalMealDao;
    public PersonalMealDetailsActivityViewModel(){
        fullPersonalMealLiveData = new MutableLiveData<>();
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
    }

    public MutableLiveData<PersonalMeal> getFullPersonalMealLiveData() {
        return fullPersonalMealLiveData;
    }

    public void setFullPersonalMealLiveData(MutableLiveData<PersonalMeal> fullPersonalMealLiveData) {
        this.fullPersonalMealLiveData = fullPersonalMealLiveData;
    }
    public Completable deletePersonalMealById(Integer id){
        return personalMealDao.deletePersonalMealById(id);
    }
}
