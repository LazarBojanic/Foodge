package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMealCountByDate;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;

public class StatsFragmentViewModel extends ViewModel {
    private PersonalMealDao personalMealDao;
    private MutableLiveData<List<PersonalMealCountByDate>> personalMealCountByDateListLiveData;
    public StatsFragmentViewModel(){
        personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
        personalMealCountByDateListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<PersonalMealCountByDate>> getPersonalMealCountByDateListLiveData() {
        return personalMealCountByDateListLiveData;
    }

    public void setPersonalMealCountByDateListLiveData(MutableLiveData<List<PersonalMealCountByDate>> personalMealCountByDateListLiveData) {
        this.personalMealCountByDateListLiveData = personalMealCountByDateListLiveData;
    }
    public void updateChart() {
        personalMealDao.getPersonalMealCountsByDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personalMealCounts -> personalMealCountByDateListLiveData.postValue(personalMealCounts));
    }
}