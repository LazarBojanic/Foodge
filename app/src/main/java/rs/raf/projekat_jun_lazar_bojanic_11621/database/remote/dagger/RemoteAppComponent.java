package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IAreaRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IIngredientRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;
@Singleton
@Component(modules = RemoteModule.class)
public interface RemoteAppComponent {
    Application getApplication();
    IAreaRepository getAreaRepository();
    ICategoryRepository getCategoryRepository();
    IIngredientRepository getIngredientRepository();
    IMealRepository getMealRepository();
    Retrofit getRetrofit();
}
