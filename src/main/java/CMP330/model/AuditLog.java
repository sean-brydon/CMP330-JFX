package CMP330.model;

import com.j256.ormlite.field.DatabaseField;


public class AuditLog {
    @DatabaseField(generatedId = true)
    private long auditId;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String action;

    @DatabaseField(canBeNull = false)
    private String oldData;

    @DatabaseField(canBeNull = false)
    private String newData;

    @DatabaseField(canBeNull = false,foreign = true, foreignAutoRefresh=true)
    private User actionByUserId;

    AuditLog() {

    }

    public AuditLog(String createdAt, String updatedAt, String action, String oldData, String newData, User actionByUserId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.action = action;
        this.oldData = oldData;
        this.newData = newData;
        this.actionByUserId = actionByUserId;
    }

    public long getAuditId() {
        return auditId;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public User getActionByUserId() {
        return actionByUserId;
    }

    public void setActionByUserId(User actionByUserId) {
        this.actionByUserId = actionByUserId;
    }
}
