package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class LoginUser {

    // -------------------------------------------------------------------------------------
    // CLASS FIELDS
    // -------------------------------------------------------------------------------------

    @SerializedName("token")
    private String token;

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("is_superuser")
    private boolean isSuperuser;

    @SerializedName("is_staff")
    private boolean isStaff;

    // -------------------------------------------------------------------------------------
    // CONSTRUCTOR - INITIALIZE ALL FIELDS
    // -------------------------------------------------------------------------------------
    public LoginUser(String token, int id, String username, String firstName, String lastName, String email, boolean isSuperuser, boolean isStaff) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isSuperuser = isSuperuser;
        this.isStaff = isStaff;
    }

    // -------------------------------------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------------------------------------
    public String getToken() { return token; }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public boolean isSuperuser() { return isSuperuser; }
    public boolean isStaff() { return isStaff; }

}
