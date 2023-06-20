package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model;

public class Area {
    private String strArea;

    public Area() {
    }

    public Area(String strArea) {
        this.strArea = strArea;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    @Override
    public String toString() {
        return "Area{" +
                "strArea='" + strArea + '\'' +
                '}';
    }
}
