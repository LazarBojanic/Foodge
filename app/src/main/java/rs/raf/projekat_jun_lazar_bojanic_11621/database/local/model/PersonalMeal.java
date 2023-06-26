package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

@Entity(tableName = "personal_meal")
@TypeConverters(DateConverter.class)
public class PersonalMeal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo
    private String idMeal;
    @ColumnInfo
    private String strMeal;
    @ColumnInfo
    private String mealType;
    @ColumnInfo
    private String instructions;
    @ColumnInfo
    private String recipe;
    @ColumnInfo
    private String strYoutube;
    @ColumnInfo
    private String mealImagePath;
    @ColumnInfo
    private Date dateOfPrep;

    @ColumnInfo
    private String idCategory;
    @ColumnInfo
    private String strCategory;
    @ColumnInfo
    private String strArea;
    @ColumnInfo
    private String strIngredient;
    @ColumnInfo
    private String strTags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public String getMealImagePath() {
        return mealImagePath;
    }

    public void setMealImagePath(String mealImagePath) {
        this.mealImagePath = mealImagePath;
    }

    public Date getDateOfPrep() {
        return dateOfPrep;
    }

    public void setDateOfPrep(Date dateOfPrep) {
        this.dateOfPrep = dateOfPrep;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public PersonalMeal(Integer id, String idMeal, String strMeal, String mealType, String instructions, String recipe, String strYoutube, String mealImagePath, Date dateOfPrep, String idCategory, String strCategory, String strArea, String strIngredient, String strTags) {
        this.id = id;
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.mealType = mealType;
        this.instructions = instructions;
        this.recipe = recipe;
        this.strYoutube = strYoutube;
        this.mealImagePath = mealImagePath;
        this.dateOfPrep = dateOfPrep;
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strIngredient = strIngredient;
        this.strTags = strTags;
    }

}
