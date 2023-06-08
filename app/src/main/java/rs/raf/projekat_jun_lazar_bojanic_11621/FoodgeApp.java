package rs.raf.projekat_jun_lazar_bojanic_11621;

import android.app.Application;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.DaggerLocalAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.LocalAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.dagger.LocalModule;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.dagger.DaggerRemoteAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.dagger.RemoteAppComponent;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.dagger.RemoteModule;

public class FoodgeApp extends Application {
    private LocalAppComponent localAppComponent;
    private RemoteAppComponent remoteAppComponent;
    private static FoodgeApp instance;
    @Override
    public void onCreate(){
        super.onCreate();
        localAppComponent = DaggerLocalAppComponent.builder()
                .localModule(new LocalModule(this))
                .build();
        remoteAppComponent = DaggerRemoteAppComponent.builder()
                .remoteModule(new RemoteModule(this))
                .build();
        instance = this;
    }
    public static FoodgeApp getInstance(){
        return instance;
    }
    public LocalAppComponent getLocalAppComponent(){
        return this.localAppComponent;
    }
    public RemoteAppComponent getRemoteAppComponent(){
        return this.remoteAppComponent;
    }
}
