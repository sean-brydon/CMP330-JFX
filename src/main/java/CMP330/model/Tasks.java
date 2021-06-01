package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tblTasks")
public class Tasks {

    public static final String ASSIGNED_TO_COL_NAME="assignedToId";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String task;

    @DatabaseField(canBeNull = false)
    private String status;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Project projectId;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true,columnName = ASSIGNED_TO_COL_NAME)
    private User assignedToId;

    public Tasks(String createdAt, String updatedAt, String task, String status, Project projectId, User assignedToId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.task = task;
        this.status = status;
        this.projectId = projectId;
        this.assignedToId = assignedToId;
    }

    // Used to update tasks as it requires an ID
    public Tasks(int id, String createdAt, String updatedAt, String task, String status, Project projectId, User assignedToId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.task = task;
        this.status = status;
        this.projectId = projectId;
        this.assignedToId = assignedToId;
    }

    Tasks() {

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public User getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(User assignedToId) {
        this.assignedToId = assignedToId;
    }
}
