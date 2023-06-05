package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.inject.Inject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.app.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.ServiceUserDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.specification.IServiceUserService;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.RegistrationException;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.VerificationException;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Serializer;

public class ServiceUserService implements IServiceUserService {
    private final ServiceUserDao serviceUserDao;

    @Inject
    public ServiceUserService(ServiceUserDao serviceUserDao){
        this.serviceUserDao = serviceUserDao;
    }

    @Override
    public Completable register(ServiceUser serviceUser) {
        return serviceUserDao.getByEmail(serviceUser.getEmail())
                .flatMapCompletable(existingUser -> Completable.error(new RegistrationException("Email already taken")))
                .onErrorResumeNext(error -> {
                    if (error instanceof NoSuchElementException) {
                        if (!validateData(serviceUser)) {
                            return Completable.error(new RegistrationException("Invalid user data"));
                        }

                        String hashedPassword = hashPassword(serviceUser.getPass());
                        serviceUser.setPass(hashedPassword);

                        return serviceUserDao.insert(serviceUser)
                                .ignoreElement();
                    } else {
                        return Completable.error(error);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable login(ServiceUser serviceUser) {
        return serviceUserDao.getByEmail(serviceUser.getEmail())
                .flatMap(existingUser -> {
                    if (!checkPassword(serviceUser.getPass(), existingUser.getPass())) {
                        return Single.error(new VerificationException("Invalid password"));
                    }
                    return Single.just(existingUser);
                })
                .flatMapCompletable(user -> setUserSharedPreference(user).subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable logout() {
        return removeUserSharedPreference().subscribeOn(Schedulers.io());
    }

    @Override
    public Completable loginWithSharedPreferences() {
        return getUserSharedPreference()
                .flatMapCompletable(serviceUser -> serviceUserDao.getByEmail(serviceUser.getEmail())
                        .flatMapCompletable(user -> setUserSharedPreference(user).subscribeOn(Schedulers.io()))
                        .onErrorComplete())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ServiceUser> getUserSharedPreference() {
        return Single.fromCallable(() -> {
            Context context = FoodgeApp.getInstance().getApplicationContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getResources().getString(R.string.foodgeSharedPreferences), Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                if (sharedPreferences.contains(context.getResources().getString(R.string.userSharedPreference))) {
                    String serviceUserJson = sharedPreferences.getString(
                            context.getResources().getString(R.string.userSharedPreference), "USER_SP_UNAVAILABLE");
                    return Serializer.deserialize(serviceUserJson, ServiceUser.class);
                }
            }
            return null;
        });
    }

    @Override
    public Completable setUserSharedPreference(ServiceUser serviceUser) {
        return Completable.fromAction(() -> {
            Context context = FoodgeApp.getInstance().getApplicationContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getResources().getString(R.string.foodgeSharedPreferences), Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                String serviceUserJson = Serializer.serialize(serviceUser);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getResources().getString(R.string.userSharedPreference), serviceUserJson);
                editor.apply();
            } else {
                throw new IllegalStateException("Failed to access shared preferences.");
            }
        });
    }

    @Override
    public Completable removeUserSharedPreference() {
        return Completable.fromAction(() -> {
            Context context = FoodgeApp.getInstance().getApplicationContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getResources().getString(R.string.foodgeSharedPreferences), Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(context.getResources().getString(R.string.userSharedPreference));
                editor.apply();
            } else {
                throw new IllegalStateException("Failed to access shared preferences.");
            }
        });
    }
    public Boolean validateData(ServiceUser serviceUser){
        return serviceUser.getPass().length() >= 4;
    }

    @Override
    public String hashPassword(String pass) {
        return BCrypt.withDefaults().hashToString(12, pass.toCharArray());
    }

    @Override
    public Boolean checkPassword(String pass, String hashedPass) {
        return BCrypt.verifyer().verify(pass.toCharArray(), hashedPass).verified;
    }
}
