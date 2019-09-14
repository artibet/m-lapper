package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import gr.artibet.lapper.R;

public class User {

    // MEMBERS

    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

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

    @SerializedName("updated_at_formated")
    private String updatedAtFormated;

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public String getUpdatedAtFormated() {return updatedAtFormated; }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setUpdatedAtFormated(String updatedAtFormated) {
        this.updatedAtFormated = updatedAtFormated;
    }

    // User FullName
    public String getFullName() {
        String fn = (firstName != null) ? firstName : "";
        String ln = (lastName != null) ? lastName : "";
        return (lastName + " " + firstName).trim();
    }
}
