package rs.raf.projekat_jun_lazar_bojanic_11621.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import rs.raf.projekat_jun_lazar_bojanic_11621.dao.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;

@Database(entities = {ServiceUser.class}, version = 1)

public abstract class FoodgeDatabase extends RoomDatabase {

    private static volatile FoodgeDatabase instance;

    public static FoodgeDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (FoodgeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), FoodgeDatabase.class, "foodge_db").build();
                }
            }
        }
        return instance;
    }

    public abstract ServiceUserDao serviceUserDao();
}
