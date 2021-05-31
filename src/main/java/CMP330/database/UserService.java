package CMP330.database;

import CMP330.Utils.DateFns;
import CMP330.model.User;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final Database db = new Database();
    private final DateFns date =  new DateFns();
    private User user;

    @Inject
    public UserService() {

    }

    /**
     * This function creates a user
     * @param username
     * @param password
     * @param email
     * @return User
     * @throws SQLException
     */
    // This was used in testing with the signup page (doesnt get used inthe project)
    public User createUser(String username, String password, String email) throws Exception {
        // Get the current time as a string
        String currentTime = date.customDateFormat(DateFns.DateFormatOptions.Default);
        // Options to create new user
        User newUser = new User(currentTime, currentTime, username, email, password, "USER");
        // Save user to db.

        try{
            db.getUserDao().create(newUser);
        }catch (Exception e){
            throw new Exception("This user already exists");
        }

        return newUser;
    }

    // Create

    public User create(User user){
        try{
            db.getUserDao().create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    /**
     * Login the user
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public User loginUser(String username, String password) throws Exception {
        // Find user by username
        User foundUser = db.getUserDao().queryBuilder().where().eq("name",username).queryForFirst();
        // Throw error if password is incorrect
        if(!comparePassword(foundUser,password)) throw new Exception("Invalid Username or Password");
        return foundUser;
    }


    private Boolean comparePassword(User user, String password){
        // Guard clause to check if the user password is equal to the entered password
        if(user==null) return false;

        return user.getPassword().equals(password);
    }

    public List<User> getAllUsers()  {
        try{
            return db.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(User user) {
        try{
            db.getUserDao().delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User update(User updatedUser) {
        try{
            db.getUserDao().update(user);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return updatedUser;
    }


    public User findOneByUsername(String username) {
        try{
            return db.getUserDao().queryBuilder().where().eq("name",username).queryForFirst();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
