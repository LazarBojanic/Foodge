package rs.raf.projekat_jun_lazar_bojanic_11621.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;

@Dao
public interface ServiceUserDao {
    @Query("SELECT * FROM service_user")
    List<ServiceUser> getAll();
    @Query("SELECT * FROM service_user WHERE id = :id")
    ServiceUser getById(Integer id);
    @Query("SELECT * FROM service_user WHERE email = :email")
    ServiceUser getByEmail(String email);
    @Query("SELECT * FROM service_user WHERE email = :email AND username = :username")
    ServiceUser getByEmailAndUsername(String email, String username);

    @Insert
    long insert(ServiceUser serviceUser);

    @Query("UPDATE service_user SET pass = :pass WHERE id = :id")
    int updateById(Integer id, String pass);

    @Query("DELETE FROM service_user WHERE id = :id")
    int deleteById(Integer id);
}
