package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField()
    private String name;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String email;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = false)
    private String role;

    public static USER_ROLES[] allRoles = {USER_ROLES.RESEARCHER,USER_ROLES.HRESEARCHER,USER_ROLES.OFFICE_ADMIN,USER_ROLES.SYS_ADMIN};

    public enum USER_ROLES {
        RESEARCHER("Researcher"),
        HRESEARCHER("Head Researcher"),
        OFFICE_ADMIN("Office Admin"),
        SYS_ADMIN("System Admin");

        private String role;

        USER_ROLES(String userrole) {
            this.role = userrole;
        }

        public String getRole() {
            return role;
        }
    }



    User() {

    }

    public User(String createdAt, String updatedAt, String name, String email, String password, String role) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    // For updating only
    public User(int id, String createdAt, String updatedAt, String name, String email, String password, String role) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

