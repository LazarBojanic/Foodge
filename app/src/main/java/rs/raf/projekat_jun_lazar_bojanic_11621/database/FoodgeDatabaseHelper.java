package rs.raf.projekat_jun_lazar_bojanic_11621.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Hasher;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Util;

public class FoodgeDatabaseHelper {
    private static volatile FoodgeDatabaseHelper instance;
    private final FoodgeDatabase foodgeDatabase;

    public FoodgeDatabaseHelper(Context context){
        this.foodgeDatabase = FoodgeDatabase.getInstance(context);
    }

    public static FoodgeDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (FoodgeDatabaseHelper.class) {
                if (instance == null) {
                    return new FoodgeDatabaseHelper(context);
                }
            }
        }
        return instance;
    }
    public boolean loginUserWithSharedPreferences(Context context, ServiceUser serviceUser){
        try{
            if(Util.validateUserData(context, serviceUser, true)){
                ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmailAndUsername(serviceUser.getEmail(), serviceUser.getUsername());
                if (serviceUserInDatabase != null){
                    String columnPass = serviceUserInDatabase.getPass();
                    if(serviceUser.getPass().equals(columnPass)){
                        Integer id = serviceUserInDatabase.getId();
                        serviceUser.setId(id);
                        serviceUser.setPass(columnPass);
                        if(Util.putUserSharedPreference(context, serviceUser)){
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Login successful.");
                            Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        else{
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Putting shared preferences failed.");
                            Toast.makeText(context, "Login failed. Putting shared preferences failed.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    else{
                        Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Incorrect password.");
                        Toast.makeText(context, "Login failed. Incorrect password.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                else{
                    Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Incorrect email or username.");
                    Toast.makeText(context, "Login failed. Incorrect email or username.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Incorrect data format.");
                Toast.makeText(context, "Login failed. Incorrect data format.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. " + e.getMessage());
            Toast.makeText(context, "Login failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean loginUser(Context context, ServiceUser serviceUser) {
        try{
            if(Util.validateUserData(context, serviceUser, false)){
                ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmailAndUsername(serviceUser.getEmail(), serviceUser.getUsername());
                if (serviceUserInDatabase != null){
                    String columnPass = serviceUserInDatabase.getPass();
                    if(Hasher.checkPassword(serviceUser.getPass(), columnPass)) {
                        Integer columnId = serviceUserInDatabase.getId();
                        serviceUser.setId(columnId);
                        serviceUser.setPass(columnPass);
                        if (Util.putUserSharedPreference(context, serviceUser)) {
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Login successful.");
                            Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        else {
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Putting shared preferences failed.");
                            Toast.makeText(context, "Login failed. Putting shared preferences failed.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    else{
                        Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Incorrect password.");
                        Toast.makeText(context, "Login failed. Incorrect password.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                else{
                    Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Email or username incorrect.");
                    Toast.makeText(context, "Login failed. Email or username incorrect.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. Incorrect data format.");
                Toast.makeText(context, "Login failed. Incorrect data format.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Log.i(context.getResources().getString(R.string.foodgeTag), "Login failed. " + e.getMessage());
            Toast.makeText(context, "Login failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public Completable registerUser(Context context, ServiceUser serviceUser) {
        if (Util.validateUserData(context, serviceUser, false)) {
            return Completable.fromAction(() -> {
                        ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getByEmail(serviceUser.getEmail());
                        if (serviceUserInDatabase == null) {
                            serviceUser.setPass(Hasher.hashPassword(serviceUser.getPass()));
                            if (foodgeDatabase.serviceUserDao().insert(serviceUser) > 1) {
                                Log.i(context.getResources().getString(R.string.foodgeTag), "Successful registration.");
                                Toast.makeText(context, "Successful registration.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(context.getResources().getString(R.string.foodgeTag), "Registration failed. Failed to add user.");
                                Toast.makeText(context, "Registration failed. Failed to add user.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i(context.getResources().getString(R.string.foodgeTag), "Registration failed. Email already exists.");
                            Toast.makeText(context, "Registration failed. Email already exists.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            Log.i(context.getResources().getString(R.string.foodgeTag), "Registration failed. Incorrect data format.");
            Toast.makeText(context, "Registration failed. Incorrect data format.", Toast.LENGTH_SHORT).show();
            return Completable.error(new IllegalArgumentException("Incorrect data format."));
        }
    }
    public boolean verifyOldPasswordMatches(Context context, Integer id, String oldPass, String newPass, String confirmNewPass){
        if(Util.checkPasswordValidityWhenChanging(context, id, oldPass, newPass, confirmNewPass)){
            ServiceUser serviceUserInDatabase = foodgeDatabase.serviceUserDao().getById(id);
            if(serviceUserInDatabase != null){
                String columnPass = serviceUserInDatabase.getPass();
                return Hasher.checkPassword(oldPass, columnPass);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    public boolean changeUserPassword(Context context, String oldPass, String newPass, String confirmNewPass){
        try{
            ServiceUser serviceUser = Util.getUserSharedPreference(context);
            if(serviceUser != null){
                Integer id = serviceUser.getId();
                if(verifyOldPasswordMatches(context, id, oldPass, newPass, confirmNewPass)){
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
        }
    }
    public void updateUser(ServiceUser serviceUser){

    }
    public void deleteUserById(Integer id){

    }
}
