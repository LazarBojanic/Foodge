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
    public Bitmap getPlaceHolderImage() {
        Drawable drawable = ContextCompat.getDrawable(FoodgeApp.getInstance().getApplicationContext(), R.drawable.placeholder_image_small);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
