package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote;

public class MealDatabase {
    private static volatile MealDatabase instance;

    private MealDatabase(){

    }

    public static MealDatabase getInstance() {
        if (instance == null) {
            synchronized (MealDatabase.class) {
                if (instance == null) {
                    instance = new MealDatabase();
                }
            }
        }
        return instance;
    }
}
