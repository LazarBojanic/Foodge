package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;

@Entity(tableName = "personal_meal")
@TypeConverters(DateConverter.class)
public class PersonalMeal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo
    private String strMeal;
    @ColumnInfo
    private String strCategory;
    @ColumnInfo
    private String mealType;
    @ColumnInfo
    private String strInstructions;
    @ColumnInfo
    private String recipe;
    @ColumnInfo
    private String strYoutube;
    @ColumnInfo
    private String mealImagePath;
    @ColumnInfo
    private Date dateOfPrep;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
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

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public PersonalMeal(){

    }

    public PersonalMeal(Integer id, String strMeal, String strCategory, String mealType, String strInstructions, String recipe, String strYoutube, String mealImagePath, Date dateOfPrep) {
        this.id = id;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.mealType = mealType;
        this.strInstructions = strInstructions;
        this.recipe = recipe;
        this.strYoutube = strYoutube;
        this.mealImagePath = mealImagePath;
        this.dateOfPrep = dateOfPrep;

    }
}
