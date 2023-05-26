package rs.raf.projekat_jun_lazar_bojanic_11621.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.reactivex.rxjava3.core.Completable;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.app.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.exception.ValidationException;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;

public class Util {
    public static ServiceUser getUserSharedPreference(Context context) throws JsonProcessingException {
        SharedPreferences sharedPreferences;
        synchronized (FoodgeApp.lock){
            sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.foodgeSharedPreferences), MODE_PRIVATE);
            if(sharedPreferences != null){
                if(sharedPreferences.contains(context.getResources().getString(R.string.userSharedPreference))){
                    String serviceUserJson = sharedPreferences.getString(context.getResources().getString(R.string.userSharedPreference),  "USER_SP_UNAVAILABLE");
                    return Serializer.deserialize(serviceUserJson, ServiceUser.class);
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
    }
    public static boolean putUserSharedPreference(Context context, ServiceUser serviceUser) throws JsonProcessingException {
        SharedPreferences sharedPreferences;
        synchronized (FoodgeApp.lock){
            sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.foodgeSharedPreferences), MODE_PRIVATE);
            if(sharedPreferences != null){
                String serviceUserJson = Serializer.serialize(serviceUser);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getResources().getString(R.string.userSharedPreference), serviceUserJson);
                editor.apply();
                return true;
            }
            else{
                return false;
            }
        }
    }
    public static boolean removeUserSharedPreference(Context context) {
        SharedPreferences sharedPreferences;
        synchronized (FoodgeApp.lock){
            sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.foodgeSharedPreferences), MODE_PRIVATE);
            if(sharedPreferences != null){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(context.getResources().getString(R.string.userSharedPreference));
                editor.apply();
                return true;
            }
            else{
                return false;
            }
        }
    }
    public static boolean passwordIsValid(String pass) {
        try{
            return pass.length() >= 4;
        }
        catch (Exception e){
            return false;
        }
    }
    public static boolean validateUserData(ServiceUser serviceUser, boolean usingSharedPreferences) {
        try {
            if (serviceUser.getEmail().isEmpty()) {
                return false;
            }
            if (serviceUser.getUsername().isEmpty()) {
                return false;
            }
            if (!usingSharedPreferences) {
                return passwordIsValid(serviceUser.getPass());
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean checkPasswordValidityWhenChanging(Integer id, String oldPass, String newPass, String confirmNewPass) {
        try {
            if(newPass.equals(confirmNewPass)){
                if(!oldPass.equals(newPass)){
                    return passwordIsValid(newPass);
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }
}
