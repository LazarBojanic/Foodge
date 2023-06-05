package rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_user", indices = {@Index(value = {"email"}, unique = true), @Index(value = {"username"}, unique = true)})
public class ServiceUser {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String pass;
    @Ignore
    public ServiceUser() {
        this.id = -1;
        this.email = "";
        this.username = "";
        this.pass = "";
    }

    public ServiceUser(Integer id, String email, String username, String pass) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.pass = pass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
