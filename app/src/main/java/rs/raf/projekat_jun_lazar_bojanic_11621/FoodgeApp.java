package rs.raf.projekat_jun_lazar_bojanic_11621;

import android.app.Application;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.DaggerFoodgeAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.FoodgeAppComponent;

public class FoodgeApp extends Application {
    private FoodgeAppComponent foodgeAppComponent;
    private static FoodgeApp instance;
    @Override
    public void onCreate(){
        super.onCreate();
        foodgeAppComponent = DaggerFoodgeAppComponent.create();
        instance = this;
    }

    public static FoodgeApp getInstance(){
        if(instance == null){
            instance = new FoodgeApp();
        }
        return instance;
    }
    public FoodgeAppComponent getFoodgeAppComponent(){
        return this.foodgeAppComponent;
    }
}
