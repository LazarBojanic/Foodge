package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;

import okhttp3.ResponseBody;

public class Category {
    @JsonIgnore
    private transient Bitmap categoryImageThumbnail;
    private String idCategory;
    private String strCategory;
    private String strCategoryThumb;
    private String strCategoryDescription;

    public Category() {
    }

    public Category(String idCategory, String strCategory, String strCategoryThumb, String strCategoryDescription) {
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryThumb = strCategoryThumb;
        this.strCategoryDescription = strCategoryDescription;
    }

    public Category(Bitmap categoryImageThumbnail, String idCategory, String strCategory, String strCategoryThumb, String strCategoryDescription) {
        this.categoryImageThumbnail = categoryImageThumbnail;
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryThumb = strCategoryThumb;
        this.strCategoryDescription = strCategoryDescription;
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

    public String getStrCategoryThumb() {
        return strCategoryThumb;
    }

    public void setStrCategoryThumb(String strCategoryThumb) {
        this.strCategoryThumb = strCategoryThumb;
    }

    public String getStrCategoryDescription() {
        return strCategoryDescription;
    }

    public void setStrCategoryDescription(String strCategoryDescription) {
        this.strCategoryDescription = strCategoryDescription;
    }

    public Bitmap getCategoryImageThumbnail() {
        return categoryImageThumbnail;
    }

    public void setCategoryImageThumbnail(Bitmap categoryImageThumbnail) {
        this.categoryImageThumbnail = categoryImageThumbnail;
    }


    public void loadCategoryImageThumbnail(ResponseBody responseBody) throws IOException {
        byte[] bytes = responseBody.bytes();
        this.categoryImageThumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
