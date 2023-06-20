package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;

public class AreasResponse {
    @JsonProperty("meals")
    private List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "AreasResponse{" +
                "areas=" + areas +
                '}';
    }
}
