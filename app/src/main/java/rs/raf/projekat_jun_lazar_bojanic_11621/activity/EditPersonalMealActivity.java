package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;

public class EditPersonalMealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_meal);
        Intent intent = getIntent();
        PersonalMeal personalMeal = intent.getSerializableExtra(String.valueOf(R.string.extraPersonalMeal), PersonalMeal.class);
        Log.i(String.valueOf(R.string.foodgeTag), personalMeal.getStrMeal());
    }
}