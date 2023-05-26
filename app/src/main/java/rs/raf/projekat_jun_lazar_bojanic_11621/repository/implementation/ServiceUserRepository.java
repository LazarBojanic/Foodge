package rs.raf.projekat_jun_lazar_bojanic_11621.repository.implementation;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.operators.completable.CompletableEmpty;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.LoginException;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.RegistrationException;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.ValidationException;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.repository.specification.IServiceUserRepository;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Hasher;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Util;

public class ServiceUserRepository implements IServiceUserRepository {
    private static volatile ServiceUserRepository instance;
    private final FoodgeDatabase foodgeDatabase;

    private ServiceUserRepository(Context context){
        this.foodgeDatabase = FoodgeDatabase.getInstance(context);
    }

    public static ServiceUserRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ServiceUserRepository.class) {
                if (instance == null) {
                    instance = new ServiceUserRepository(context);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<List<ServiceUser>> getAll() {
        return null;
    }

    @Override
    public Observable<ServiceUser> getById(Integer id) {
        return null;
    }

    @Override
    public Completable register(ServiceUser serviceUser) {
        return Completable.defer(() -> {
                    try{
                        if(Util.validateUserData(serviceUser, false)){
                            ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmail(serviceUser.getEmail());
                            if (serviceUserInDatabase == null) {
                                serviceUser.setPass(Hasher.hashPassword(serviceUser.getPass()));
                                if (foodgeDatabase.serviceUserDao().insert(serviceUser) > 0) {
                                    return Completable.complete();
                                }
                                else {
                                    throw new RegistrationException("Registration failed. Failed to add user.");
                                }
                            }
                            else {
                                throw new RegistrationException("Registration failed. Email already exists.");
                            }
                        }
                        else{
                            throw new RegistrationException("Registration failed. Validation error.");
                        }
                    }
                    catch (Exception e){
                        throw new RegistrationException(e.getMessage());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable login(Context context, ServiceUser serviceUser)  {
        return Completable.defer(() -> {
            try{
                if(Util.validateUserData(serviceUser, false)) {
                    ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmailAndUsername(serviceUser.getEmail(), serviceUser.getUsername());
                    if (serviceUserInDatabase != null){
                        String columnPass = serviceUserInDatabase.getPass();
                        if(Hasher.checkPassword(serviceUser.getPass(), columnPass)) {
                            Integer columnId = serviceUserInDatabase.getId();
                            serviceUser.setId(columnId);
                            serviceUser.setPass(columnPass);
                            if (Util.putUserSharedPreference(context, serviceUser)) {
                                return Completable.complete();
                            }
                            else {
                                throw new LoginException("Login failed. Putting shared preferences failed.");
                            }
                        }
                        else{
                            throw new LoginException("Login failed. Incorrect password.");
                        }
                    }
                    else{
                        throw new LoginException("Login failed. Email or username incorrect.");
                    }
                }
                else {
                    throw new LoginException("Login failed. Validation error.");
                }
            }
            catch (Exception e){
                throw new LoginException(e.getMessage());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }
    @Override
    public Completable loginWithSharedPreferences(Context context){
        return Completable.defer(() -> {
                    try {
                        ServiceUser serviceUser = Util.getUserSharedPreference(context);
                        if (serviceUser != null) {
                            if(Util.validateUserData(serviceUser, true)){
                                ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmailAndUsername(serviceUser.getEmail(), serviceUser.getUsername());
                                if (serviceUserInDatabase != null) {
                                    String columnPass = serviceUserInDatabase.getPass();
                                    if (serviceUser.getPass().equals(columnPass)) {
                                        Integer id = serviceUserInDatabase.getId();
                                        serviceUser.setId(id);
                                        serviceUser.setPass(columnPass);
                                        if (Util.putUserSharedPreference(context, serviceUser)) {
                                            return Completable.complete();
                                        }
                                        else{
                                            throw new LoginException("Login failed. Putting shared preferences failed.");
                                        }
                                    }
                                    else {
                                        throw new LoginException("Login failed. Incorrect password.");
                                    }
                                }
                                else {
                                    throw new LoginException("Login failed. Incorrect email or username.");
                                }
                            }
                            else{
                                throw new LoginException("Login failed. Incorrect data format.");
                            }
                        }
                        else {
                            throw new LoginException("Login failed. User shared preference doesn't exist.");
                        }

                    }
                    catch (Exception e) {
                        throw new LoginException(e.getMessage());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    @Override
    public Completable verifyOldPasswordMatches(Integer id, String oldPass, String newPass, String confirmNewPass) {
        return Completable.defer(() -> {
                    try {
                        if (Util.checkPasswordValidityWhenChanging(id, oldPass, newPass, confirmNewPass)) {
                            ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getById(id);
                            if (serviceUserInDatabase != null) {
                                String columnPass = serviceUserInDatabase.getPass();
                                if (Hasher.checkPassword(oldPass, columnPass)) {
                                    return Completable.complete();
                                } else {
                                    throw new ValidationException("Incorrect password.");
                                }
                            } else {
                                throw new ValidationException("Could not find user.");
                            }
                        } else {
                            throw new ValidationException("Passwords don't match.");
                        }
                    } catch (Exception e) {
                        throw new ValidationException(e.getMessage());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable logout() {
        return null;
    }

    public boolean changeUserPassword(Context context, String oldPass, String newPass, String confirmNewPass){
        /*try{
            ServiceUser serviceUser = Util.getUserSharedPreference(context);
            if(serviceUser != null){
                Integer id = serviceUser.getId();
                if(verifyOldPasswordMatches(id, oldPass, newPass, confirmNewPass)){
                    String hashedPass = Hasher.hashPassword(newPass);
                    if(foodgeDatabase.serviceUserDao().updateById(id, hashedPass) > 0){
                        serviceUser.setPass(hashedPass);
                        if(Util.putUserSharedPreference(context, serviceUser)){
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Password change successful.");
                            Toast.makeText(context, "Password change successful.", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        else{
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Password change failed. Couldn't put user shared preference.");
                            Toast.makeText(context, "Password change failed. Couldn't put user shared preference.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    else{
                        Log.i(context.getResources().getString(R.string.foodgeTag), "Error updating password");
                        Toast.makeText(context, "Error updating password", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                else{
                    Log.i(context.getResources().getString(R.string.foodgeTag), "Password change failed. Old password doesn't match.");
                    Toast.makeText(context, "Password change failed. Old password doesn't match.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Log.i(context.getResources().getString(R.string.foodgeTag), "Password change failed. Couldn't get user shared preference.");
                Toast.makeText(context, "Password change failed. Couldn't get user shared preference.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Log.i(context.getResources().getString(R.string.foodgeTag), "Password change failed. " + e.getMessage());
            Toast.makeText(context, "Password change failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return false;
    }
}
