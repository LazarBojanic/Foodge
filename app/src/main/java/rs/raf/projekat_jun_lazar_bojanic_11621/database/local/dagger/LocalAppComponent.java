package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation.ServiceUserService;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.specification.IServiceUserService;
@Singleton
@Component(modules = {LocalModule.class})
public interface LocalAppComponent {
    Application getApplication();
    IServiceUserService getServiceUserService();
    PersonalMealDao getPersonalMealDao();
    ServiceUserDao getServiceUserDao();
}
