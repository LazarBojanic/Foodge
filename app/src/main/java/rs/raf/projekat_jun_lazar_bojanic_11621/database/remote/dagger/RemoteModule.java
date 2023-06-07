package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.dagger;

import android.app.Application;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IAreaRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.ICategoryRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IIngredientRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.repository.IMealRepository;

@Module
public class RemoteModule {
    private final Application application;

    public RemoteModule(Application application) {
        this.application = application;
    }
    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Interceptor provideUrlLoggingInterceptor() {
        return chain -> {
            Request request = chain.request();
            String url = request.url().toString();
            Log.d(String.valueOf(R.string.foodgeTag), "Request URL: " + url);
            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Interceptor interceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        client.addInterceptor(interceptor);
        return client.build();
    }

    @Provides
    @Singleton
    ObjectMapper provideJackson() {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(ObjectMapper objectMapper, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl("https://www.themealdb.com/")
                .client(okHttpClient)
                .build();
    }
    @Provides
    @Singleton
    public IAreaRepository provideAreaRepository(Retrofit retrofit) {
        return retrofit.create(IAreaRepository.class);
    }
    @Provides
    @Singleton
    public ICategoryRepository provideCategoryRepository(Retrofit retrofit) {
        return retrofit.create(ICategoryRepository.class);
    }
    @Provides
    @Singleton
    public IIngredientRepository provideIngredientRepository(Retrofit retrofit) {
        return retrofit.create(IIngredientRepository.class);
    }
    @Provides
    @Singleton
    public IMealRepository provideMealRepository(Retrofit retrofit) {
        return retrofit.create(IMealRepository.class);
    }
}
