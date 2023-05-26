package rs.raf.projekat_jun_lazar_bojanic_11621.repository.specification;

import android.content.Context;

import java.util.List;


import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.repository.implementation.ServiceUserRepository;

public interface IServiceUserRepository {
    Observable<List<ServiceUser>> getAll();
    Observable<ServiceUser> getById(Integer id);
    Completable register(ServiceUser serviceUser);
    Completable login(Context context, ServiceUser serviceUser);
    Completable loginWithSharedPreferences(Context context);
    public Completable verifyOldPasswordMatches(Integer id, String oldPass, String newPass, String confirmNewPass);
    Completable logout();
}