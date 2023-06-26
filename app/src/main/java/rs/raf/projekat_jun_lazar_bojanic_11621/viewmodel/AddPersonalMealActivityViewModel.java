package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;

public class AddPersonalMealActivityViewModel extends ViewModel {
    private MutableLiveData<PersonalMeal> personalMealMutableLiveData;

    public AddPersonalMealActivityViewModel(){
        personalMealMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PersonalMeal> getPersonalMealMutableLiveData() {
        return personalMealMutableLiveData;
    }

    public void setPersonalMealMutableLiveData(MutableLiveData<PersonalMeal> personalMealMutableLiveData) {
        this.personalMealMutableLiveData = personalMealMutableLiveData;
    }
}
