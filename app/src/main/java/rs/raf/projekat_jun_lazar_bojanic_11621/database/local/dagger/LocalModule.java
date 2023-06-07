package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation.ServiceUserService;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.specification.IServiceUserService;

@Module
public class LocalModule {
    private final Application application;

    public LocalModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public FoodgeDatabase provideFoodgeDatabase(Application application) {
        return FoodgeDatabase.getInstance(application);
    }

    @Provides
    @Singleton
    public ServiceUserDao provideServiceUserDao(FoodgeDatabase foodgeDatabase) {
        return foodgeDatabase.serviceUserDao();
    }

    @Provides
    @Singleton
    public IServiceUserService provideServiceUserService(ServiceUserDao serviceUserDao) {
        return new ServiceUserService(serviceUserDao);
    }
}
