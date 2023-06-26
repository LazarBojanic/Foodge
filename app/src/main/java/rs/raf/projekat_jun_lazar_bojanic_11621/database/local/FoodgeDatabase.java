package rs.raf.projekat_jun_lazar_bojanic_11621.database.local;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;

@Database(entities = {ServiceUser.class, PersonalMeal.class}, version = 1)
public abstract class FoodgeDatabase extends RoomDatabase {
    private static volatile FoodgeDatabase instance;
    public static FoodgeDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (FoodgeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(application, FoodgeDatabase.class, "foodge_db").build();
                }
            }
        }
        return instance;
    }
    public abstract ServiceUserDao serviceUserDao();
    public abstract PersonalMealDao personalMealDao();
}
