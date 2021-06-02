package CMP330.model;

import com.j256.ormlite.field.DatabaseField;


public class AuditLog {
    @DatabaseField(generatedId = true)
    private int auditId;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String action;

    @DatabaseField(canBeNull = false,foreign = true, foreignAutoRefresh=true)
    private User actionByUserId;

    AuditLog() {

    }

    public AuditLog(String createdAt, String updatedAt, String action, User actionByUserId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.action = action;
        this.actionByUserId = actionByUserId;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
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

    public User getActionByUserId() {
        return actionByUserId;
    }

    public void setActionByUserId(User actionByUserId) {
        this.actionByUserId = actionByUserId;
    }
}
