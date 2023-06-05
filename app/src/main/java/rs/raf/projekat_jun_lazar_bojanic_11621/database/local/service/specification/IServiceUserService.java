package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.specification;

import android.content.Context;

import java.util.List;


import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;

public interface IServiceUserService {
    Completable register(ServiceUser serviceUser);
    Completable login(ServiceUser serviceUser);
    Completable logout();
    Completable loginWithSharedPreferences();
    Single<ServiceUser> getUserSharedPreference();
    Completable setUserSharedPreference(ServiceUser serviceUser);
    Completable removeUserSharedPreference();
    String hashPassword(String pass);
    Boolean checkPassword(String pass, String hashedPass);
}