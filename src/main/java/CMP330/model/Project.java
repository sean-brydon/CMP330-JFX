package CMP330.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tblProjects")
public class Project {

    public static final String RESEARCHER_COL_NAME = "researcher_id";
    public static final String HEAD_RESEARCHER_COL_NAME = "headResearcher_id";
    public static PROJECT_STATUS[] PROJECT_STATUS_ARRAY = new PROJECT_STATUS[]{PROJECT_STATUS.NEW_PROJECT, PROJECT_STATUS.IN_PROGRESS, PROJECT_STATUS.COMPLETE_PENDING_REVIEW,PROJECT_STATUS.COMPLETED};
    public enum PROJECT_STATUS {
        NEW_PROJECT("New"),
        IN_PROGRESS("In Progress"),
        COMPLETE_PENDING_REVIEW("Complete Pending Review"),
        COMPLETED("Completed");

        private String statusText;

        PROJECT_STATUS(String status) {
            this.statusText = status;
        }

        public String getStatus() {
            return statusText;
        }
    }


    @DatabaseField(generatedId = true)
    private int projectId;

    @DatabaseField(canBeNull = false)
    private String createdAt;

    @DatabaseField(canBeNull = false)
    private String updatedAt;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField(canBeNull = false)
    private String status;

    @DatabaseField(canBeNull = false,foreign = true, foreignAutoRefresh=true)
    private Customer customer;

    @DatabaseField(canBeNull = false,foreign = true, foreignAutoRefresh=true, columnName=RESEARCHER_COL_NAME )
    private User researcher;

    @DatabaseField(canBeNull = false,foreign = true, foreignAutoRefresh=true,columnName=HEAD_RESEARCHER_COL_NAME)
    private User headResearcher;


    Project() {

    }
    public Project(String createdAt, String updatedAt, String title, String status, Customer customer, User researcher, User headResearcher) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.status = status;
        this.customer = customer;
        this.researcher = researcher;
        this.headResearcher = headResearcher;
    }

    public Project(int projectId, String createdAt, String updatedAt, String title, String status, Customer customer, User researcher, User headResearcher) {
        this.projectId = projectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.status = status;
        this.customer = customer;
        this.researcher = researcher;
        this.headResearcher = headResearcher;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }



    public User getResearcher() {
        return researcher;
    }

    public void setResearcher(User researcher) {
        this.researcher = researcher;
    }

    public User getHeadResearcher() {
        return headResearcher;
    }

    public void setHeadResearcher(User headResearcher) {
        this.headResearcher = headResearcher;
    }

    public int getProjectId() {
        return projectId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
