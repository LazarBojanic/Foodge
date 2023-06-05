package rs.raf.projekat_jun_lazar_bojanic_11621.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.app.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;

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

}
