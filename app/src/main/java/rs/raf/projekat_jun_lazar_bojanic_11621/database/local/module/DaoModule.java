package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.module;

import dagger.Module;
import dagger.Provides;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;

@Module
public class DaoModule {
    @Provides
    public ServiceUserDao provideServiceUserDao(){
        return FoodgeDatabase.getInstance().serviceUserDao();
    }
}
