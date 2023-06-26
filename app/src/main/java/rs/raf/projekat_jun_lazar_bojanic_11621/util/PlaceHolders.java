package rs.raf.projekat_jun_lazar_bojanic_11621.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;

public class PlaceHolders {
    private static PlaceHolders instance;

    public static PlaceHolders getInstance(){
        if(instance == null){
            instance = new PlaceHolders();
        }
        return instance;
    }
    public Drawable getPlaceHolderImage() {
        return ContextCompat.getDrawable(FoodgeApp.getInstance().getApplicationContext(), R.drawable.placeholder_image_small);
    }
}
