package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class Note {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField()
    private String text;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false,foreign = true,foreignAutoRefresh=true)
    private Project project;
    @DatabaseField(canBeNull = false,foreign = true,foreignAutoRefresh=true)
    private User user;


    Note() {

    }

    public Note(int id, String text, String createdAt, String updatedAt, Project project, User user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.project = project;
        this.user = user;
    }

    public Note(String text, String createdAt, String updatedAt, Project project, User user) {
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.project = project;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}

