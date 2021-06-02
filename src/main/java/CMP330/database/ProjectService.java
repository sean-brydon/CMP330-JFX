package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.model.Customer;
import CMP330.model.Project;
import CMP330.model.User;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;


public class ProjectService {
    private Database db = new Database();
    private DateFns date = new DateFns();
    private User user;


    /**
     * Constructor for the project service
     */

    public ProjectService() {
        this.user = UserSingleton.getInstance().getUser();
    }

    /**
     * Create project by passing in the new project class
     *
     * @param project
     * @return project
     */
    public Project createProject(Project project) {
        try {
            int numOfAssigned = this.getProjects(project.getHeadResearcher()).size();
            if(numOfAssigned == 3) return null;
            db.getProjectDao().create(project);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        AuditLogService.Logger("Created a project" + project.getTitle(), UserSingleton.getInstance().getUser());

        return project;
    }

    /**
     * Update project by passing in the project that you want to update.
     *
     * @param project
     * @return
     */
    public Project updateProject(Project project) {
        try {
            db.getProjectDao().update(project);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        AuditLogService.Logger("Updated a project" + project.getTitle(), UserSingleton.getInstance().getUser());

        return project;
    }

    /**
     * Get Projects
     *
     * @param user
     * @return
     */
    public List<Project> getProjects(User user) {
        try {
            PreparedQuery<Project> statement = db.getProjectDao().queryBuilder().where().eq(Project.RESEARCHER_COL_NAME, user.getId()).prepare();
            return db.getProjectDao().query(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            AuditLogService.Logger("Got projects by user" + user.getName(), UserSingleton.getInstance().getUser());

            return null;
        }
    }

    public Project getProjectsByTitle(String title)  {
        Project project = null;
        try {
            PreparedQuery<Project> statement = db.getProjectDao().queryBuilder().where().eq("title",title).prepare();
            project = db.getProjectDao().queryForFirst(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Got projects by title" + title, UserSingleton.getInstance().getUser());

        return project;
    }

    public List<Project> getAllCompletedProjects()  {
        List<Project> projects = null;
        try {
            PreparedQuery<Project> statement = db.getProjectDao().queryBuilder().where().eq("status","Completed").prepare();
            projects = db.getProjectDao().query(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Got all completed projects", UserSingleton.getInstance().getUser());

        return projects;
    }


    public List<Project> getAllProjects() {
        try {
            return db.getProjectDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Got all projects", UserSingleton.getInstance().getUser());

        return null;
    }


    public void delete(Project project) {
        try{
            db.getProjectDao().delete(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogService.Logger("Got deleted project " + project.getTitle(), UserSingleton.getInstance().getUser());

    }
}
