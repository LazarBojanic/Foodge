package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger;

import dagger.Component;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.module.DaoModule;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation.ServiceUserService;

@Component(modules = {DaoModule.class})
public interface FoodgeAppComponent {
    ServiceUserService getServiceUserService();
}
