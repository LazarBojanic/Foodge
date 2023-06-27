package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.PersonalMealDetailsActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;

public class PersonalMealDetailsActivityViewModel extends ViewModel {
    private MutableLiveData<PersonalMeal> fullPersonalMealLiveData;

    public PersonalMealDetailsActivityViewModel(){
        fullPersonalMealLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PersonalMeal> getFullPersonalMealLiveData() {
        return fullPersonalMealLiveData;
    }

    public void setFullPersonalMealLiveData(MutableLiveData<PersonalMeal> fullPersonalMealLiveData) {
        this.fullPersonalMealLiveData = fullPersonalMealLiveData;
    }
    public Observable<List<PersonalMeal>> fetchFullPersonalMealDetails(Integer id){
        return null;
    }
}
