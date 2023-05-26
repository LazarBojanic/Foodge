package rs.raf.projekat_jun_lazar_bojanic_11621.app;

import android.app.Application;

import androidx.room.Room;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Util;

public class FoodgeApp extends Application {
    public static final Object lock = new Object();
    @Override
    public void onCreate(){
        super.onCreate();
        synchronized (lock){
            initDatabase();
            getSharedPreferences(getResources().getString(R.string.foodgeSharedPreferences), MODE_PRIVATE);
        }
    }
    private void initDatabase(){
        FoodgeDatabase.getInstance(this);
    }
}
