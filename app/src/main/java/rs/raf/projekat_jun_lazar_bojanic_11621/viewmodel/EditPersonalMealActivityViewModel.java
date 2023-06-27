package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;

public class EditPersonalMealActivityViewModel extends ViewModel {
    private MutableLiveData<PersonalMeal> personalMealMutableLiveData;
    private PersonalMealDao personalMealDao;
    public EditPersonalMealActivityViewModel(){
        personalMealMutableLiveData = new MutableLiveData<>();
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
    }

    public MutableLiveData<PersonalMeal> getPersonalMealMutableLiveData() {
        return personalMealMutableLiveData;
    }

    public void setPersonalMealMutableLiveData(MutableLiveData<PersonalMeal> personalMealMutableLiveData) {
        this.personalMealMutableLiveData = personalMealMutableLiveData;
    }
    public Completable updatePersonalMeal(PersonalMeal personalMeal){
        Log.i(String.valueOf(R.string.foodgeTag), "Id: " + personalMeal.getId());
        return personalMealDao.updatePersonalMealById(
                personalMeal.getId(),
                personalMeal.getStrMeal(),
                personalMeal.getStrCategory(),
                personalMeal.getMealType(),
                personalMeal.getStrInstructions(),
                personalMeal.getRecipe(),
                personalMeal.getStrYoutube(),
                personalMeal.getMealImagePath(),
                DateConverter.fromDate(personalMeal.getDateOfPrep())).subscribeOn(Schedulers.io());
    }
}
