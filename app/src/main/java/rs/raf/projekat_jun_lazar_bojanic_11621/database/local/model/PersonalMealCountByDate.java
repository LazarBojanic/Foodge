package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model;

public class PersonalMealCountByDate {
    private Integer personalMealCount;
    private String dateOfPrep;

    public PersonalMealCountByDate(Integer personalMealCount, String dateOfPrep) {
        this.personalMealCount = personalMealCount;
        this.dateOfPrep = dateOfPrep;
    }

    public Integer getPersonalMealCount() {
        return personalMealCount;
    }

    public void setPersonalMealCount(Integer personalMealCount) {
        this.personalMealCount = personalMealCount;
    }

    public String getDateOfPrep() {
        return dateOfPrep;
    }

    public void setDateOfPrep(String dateOfPrep) {
        this.dateOfPrep = dateOfPrep;
    }
}
