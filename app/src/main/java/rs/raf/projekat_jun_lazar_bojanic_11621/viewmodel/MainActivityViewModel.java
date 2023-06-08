package rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;

public class MainActivityViewModel extends ViewModel {
    private List<Category> cachedCategoryList;
    private List<Meal> cachedMealList;
    public MainActivityViewModel(){

    }

    public List<Category> getCachedCategoryList() {
        return cachedCategoryList;
    }

    public void setCachedCategoryList(List<Category> categories) {
        cachedCategoryList = categories;
    }

    public List<Meal> getCachedMealList() {
        return cachedMealList;
    }

    public void setCachedMealList(List<Meal> cachedMealList) {
        this.cachedMealList = cachedMealList;
    }
}
