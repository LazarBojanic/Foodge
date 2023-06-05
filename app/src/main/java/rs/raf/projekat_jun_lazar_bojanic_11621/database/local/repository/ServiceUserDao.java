package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;

@Dao
public interface ServiceUserDao {
    @Query("SELECT * FROM service_user")
    Observable<List<ServiceUser>> getAll();
    @Query("SELECT * FROM service_user WHERE id = :id")
    Single<ServiceUser> getById(Integer id);
    @Query("SELECT * FROM service_user WHERE email = :email")
    Single<ServiceUser> getByEmail(String email);
    @Query("SELECT * FROM service_user WHERE email = :email AND username = :username")
    Single<ServiceUser> getByEmailAndUsername(String email, String username);
    @Insert
    Single<Long> insert(ServiceUser serviceUser);
    @Query("UPDATE service_user SET pass = :pass WHERE id = :id")
    Completable updateById(Integer id, String pass);
    @Query("DELETE FROM service_user WHERE id = :id")
    Completable deleteById(Integer id);
}
