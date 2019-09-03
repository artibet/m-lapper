package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class User {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("is_active")
    private boolean isActive;

    @SerializedName("is_staff")
    private boolean isStaff;

    @SerializedName("is_superuser")
    private boolean isSuperUser;

    @SerializedName("date_joined")
    private String dateJoined;

    @SerializedName("last_login")
    private String lastLogin;

    // GETTERS

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public String getLastLogin() {
        return lastLogin;
    }
}
