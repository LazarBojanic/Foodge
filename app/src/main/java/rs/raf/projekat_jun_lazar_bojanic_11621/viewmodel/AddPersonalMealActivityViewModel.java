package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Completable;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;

public class AddPersonalMealActivityViewModel extends ViewModel {
    private MutableLiveData<PersonalMeal> personalMealMutableLiveData;
    private PersonalMealDao personalMealDao;
    public AddPersonalMealActivityViewModel(){
        personalMealMutableLiveData = new MutableLiveData<>();
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
    }

    public MutableLiveData<PersonalMeal> getPersonalMealMutableLiveData() {
        return personalMealMutableLiveData;
    }

    public void setPersonalMealMutableLiveData(MutableLiveData<PersonalMeal> personalMealMutableLiveData) {
        this.personalMealMutableLiveData = personalMealMutableLiveData;
    }
    public Completable addPersonalMeal(PersonalMeal personalMeal){
        return Completable.fromSingle(personalMealDao.addPersonalMeal(personalMeal));
    }
}
