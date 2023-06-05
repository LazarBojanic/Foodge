package rs.raf.projekat_jun_lazar_bojanic_11621.app;

import android.app.Application;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.DaggerFoodgeAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.FoodgeAppComponent;

public class FoodgeApp extends Application {
    private FoodgeAppComponent foodgeAppComponent;
    private static FoodgeApp instance;
    public static final Object lock = new Object();
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        foodgeAppComponent = DaggerFoodgeAppComponent.create();
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
