package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;

public class CategoriesResponse {
    private List<Category> categories;

    public CategoriesResponse() {
    }

    public CategoriesResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoriesResponse{" +
                "categories=" + categories +
                '}';
    }
}
