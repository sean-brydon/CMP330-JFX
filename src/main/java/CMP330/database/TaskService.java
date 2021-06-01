package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.model.Project;
import CMP330.model.Tasks;
import CMP330.model.User;
import com.j256.ormlite.stmt.PreparedQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.List;


public class TaskService {
  private Database db = new Database();
  private DateFns date = new DateFns();
  private User user;


  /**
   * Constructor for the project service

   */

  public TaskService() {
    this.user = UserSingleton.getInstance().getUser();
  }

  /**
   * Create project by passing in the new project class
   * @param task
   * @return task
   */
  public Tasks createTask(Tasks task){
    try {
      db.getTaskDao().create(task);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }

    return task;
  }


  /**
   * Update project by passing in the task that you want to update.
   * @param task
   * @return
   */
  public Tasks updateTask(Tasks task){
    try{
      db.getTaskDao().update(task);
    }catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
    return task;
  }

  /**
   * Deletes the task by passing in task
   * @param task
   * @return
   */
  public void delete(Tasks task){
    try{
      db.getTaskDao().delete(task);
    }catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Get tasks by user
   * @param user
   * @return
   */
  public List<Tasks> getTasks(User user) throws SQLException {
    List<Tasks> tasks = null;
    try{
      PreparedQuery<Tasks> statement = db.getTaskDao().queryBuilder().where().eq(Tasks.ASSIGNED_TO_COL_NAME, user.getId()).prepare();
      tasks = db.getTaskDao().query(statement);
    }catch (SQLException e){
      e.printStackTrace();
    }

    return tasks;
  }


  public List<Tasks> getAllTasks()  {
    List<Tasks> tasks = null;
    try{
      tasks = db.getTaskDao().queryForAll();
    }catch (SQLException e){
      e.printStackTrace();
    }

    return tasks;
  }

  public List<Tasks> getAllCompletedTasks() {
    List<Tasks> tasks = null;
    try{
      PreparedQuery<Tasks> statement = db.getTaskDao().queryBuilder().where().eq("status", "Completed").prepare();
      tasks = db.getTaskDao().query(statement);
    }catch (SQLException e){
      e.printStackTrace();
    }

    return tasks;
  }

}
