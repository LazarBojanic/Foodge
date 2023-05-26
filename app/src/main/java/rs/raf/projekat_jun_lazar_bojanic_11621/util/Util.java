package rs.raf.projekat_jun_lazar_bojanic_11621.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.app.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;

public class Util {
    public static ServiceUser getUserSharedPreference(Context context) throws JsonProcessingException {
        SharedPreferences sharedPreferences;
        synchronized (FoodgeApp.lock){
            sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.foodgeSharedPreferences), MODE_PRIVATE);
            if(sharedPreferences != null){
                if(sharedPreferences.contains(context.getResources().getString(R.string.userSharedPreference))){
                    String serviceUserJson = sharedPreferences.getString(context.getResources().getString(R.string.userSharedPreference),  "USER_SP_UNAVAILABLE");
                    Log.i(context.getResources().getString(R.string.foodgeTag), serviceUserJson);
                    return Serializer.deserialize(serviceUserJson, ServiceUser.class);
                }
                else{
                    Log.i(context.getResources().getString(R.string.foodgeTag), "USER_SP Shared Preference doesn't exist");
                    return null;
                }
            }
            else{
                Log.i(context.getResources().getString(R.string.foodgeTag), "Shared Preferences null");
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
                Log.i(context.getResources().getString(R.string.foodgeTag), "Shared Preferences null");
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
                Log.i(context.getResources().getString(R.string.foodgeTag), "Shared Preferences null");
                return false;
            }
        }
    }
    public static boolean passwordIsValid(Context context, String pass) {
        if(!pass.equals("")){
            if(pass.length() >= 5){
                String specialCharacters = "~#^|$%&*!";
                for (int i = 0; i < pass.length(); i++) {
                    if (specialCharacters.contains(String.valueOf(pass.charAt(i)))) {
                        Toast.makeText(context, "Error6", Toast.LENGTH_SHORT).show();
                        Log.i(context.getResources().getString(R.string.foodgeTag), "Error6");
                        return false;
                    }
                }
                return true;
            }
            else{
                Log.i(context.getResources().getString(R.string.foodgeTag), "Error7");
                Toast.makeText(context, "Error7", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Password must have at least 5 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Log.i(context.getResources().getString(R.string.foodgeTag), "Error8");
            Toast.makeText(context, "Error8", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static boolean validateUserData(Context context, ServiceUser serviceUser, boolean usingSharedPreferences){
        Log.i(context.getResources().getString(R.string.foodgeTag), "in data validation" + serviceUser.toString());
        if(serviceUser.getEmail().equals("")){
            Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            Log.i(context.getResources().getString(R.string.foodgeTag), "data validation email empty");
            return false;
        }
        if(serviceUser.getUsername().equals("")){
            Log.i(context.getResources().getString(R.string.foodgeTag), "data validation username empty" + serviceUser.toString());
            Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!usingSharedPreferences){
            return passwordIsValid(context, serviceUser.getPass());
        }
        else{
            return true;
        }
    }
    public static boolean checkPasswordValidityWhenChanging(Context context, Integer id, String oldPass, String newPass, String confirmNewPass){
        if(newPass.equals(confirmNewPass)){
            if(!oldPass.equals(newPass)){
                return passwordIsValid(context, newPass);
            }
            else{
                Toast.makeText(context, "New password must be different from old password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(context, "New password not incorrectly confirmed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
