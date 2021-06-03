package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagmentController extends LayoutController{

    // [x] Get all users
    // [] Delete button
    // [] Edit button -> Takes user to a form
    // [] Create New User -> Takes user to a form

    @FXML
    TableView listOfUsers;
    @FXML
    Button btnDel;
    @FXML
    Button btnEdit;
    @FXML
    Button btnCreate;
    @FXML
    AnchorPane anchorForm;
    @FXML
    AnchorPane anchorTable;
    @FXML
    Label lblUserManagement;
    @FXML
    TextField inpUsername;
    @FXML
    TextField inpEmail;
    @FXML
    TextField inpPassword;
    @FXML
    ComboBox inpRole;

    @Inject
    UserService userService; // This could be removed if the LayoutController userService was set to protected however

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected however
    private List<User> allUsers;
    private User currentUser;

    private Boolean editState = false;
    private User selectedUser;

    @FXML
    @Override
    public void initialize() throws IOException {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all users on load and populate list
        // Storing users to state to allow filtering of the whole array - Useful when passing the UID to a new form.
        populateTable();
        if(permissionCheck(User.USER_ROLES.SYS_ADMIN)){
            btnDel.setDisable(false);
            btnCreate.setDisable(false);
            btnEdit.setDisable(false);
        }else{
            btnDel.setDisable(true);
            btnCreate.setDisable(true);
            btnEdit.setDisable(true);
        }
        // Add items roles to the edit/create new box
        for (User.USER_ROLES role : User.allRoles) {
            this.inpRole.getItems().add(role.getRole());
        }

    }

    private Boolean permissionCheck(User.USER_ROLES requiredRole) {
        String userRole = currentUser.getRole();
        return userRole.equals(requiredRole.getRole());
    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allUsers = this.userService.getAllUsers();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));

        TableColumn<Map, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));

        TableColumn<Map, String> emailColumn = new TableColumn<>("email");
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));

        TableColumn<Map, String> passwordColumn = new TableColumn<>("password");
        passwordColumn.setCellValueFactory(new MapValueFactory<>("password"));

        TableColumn<Map, String> roleColumn = new TableColumn<>("role");
        roleColumn.setCellValueFactory(new MapValueFactory<>("role"));

        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));
        this.listOfUsers.getColumns().add(idColumn);
        this.listOfUsers.getColumns().add(nameColumn);
        this.listOfUsers.getColumns().add(emailColumn);
        this.listOfUsers.getColumns().add(passwordColumn);
        this.listOfUsers.getColumns().add(roleColumn);
        this.listOfUsers.getColumns().add(createdAt);
        // Create an observable list to add users to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allUsers.forEach(user->{
            Map<String,Object> userObj = new HashMap<>();
            // Forces int to be string to be converted back to int
            userObj.put("id",String.valueOf(user.getId()));
            userObj.put("name",user.getName());
            userObj.put("email",user.getEmail());
            userObj.put("password","*********");
            userObj.put("role",user.getRole());
            items.add(userObj);
        });

        // Add all the users to the table
        this.listOfUsers.getItems().addAll(items);
    }

    @FXML
    private void deleteUser(){
        User userToDelete = getUserFromSelection();

        // Prevent the user from deleting its self so relationships dont get skewed.
        if(userToDelete.getId() != this.currentUser.getId()){
            this.userService.delete(userToDelete);
        }

        populateTable();
    }



    @FXML
    private void editSelectedUser(){
        this.selectedUser = getUserFromSelection();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Edit User");

        // Populate fields
        inpUsername.setText(this.selectedUser.getName());
        inpEmail.setText(this.selectedUser.getEmail());
        inpPassword.setText(this.selectedUser.getPassword());

    }
    @FXML
    private void createNewUser(){
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create User");
    }

    private User getUserFromSelection() {
        TableView.TableViewSelectionModel selectionModel = listOfUsers.getSelectionModel();
        HashMap<String,String> selected = (HashMap<String,String>) selectionModel.getSelectedItem();

        // This was the easiest way to get the id back to an int.
        User userToDelete = new User(parseIntOrNull(selected.get("id")),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                selected.get("name"),
                selected.get("email"),
                selected.get("password"),
                selected.get("role"));
        return userToDelete;
    }

    @FXML
    private void onSaveEvent(){
        if(this.editState){
            User updatedUser = this.selectedUser;
            updatedUser.setEmail(inpEmail.getText());
            updatedUser.setName(inpUsername.getText());
            updatedUser.setPassword(inpPassword.getText());
            updatedUser.setPassword(inpRole.getValue().toString());
            updatedUser.setUpdatedAt(DateFns.customDateFormat(DateFns.DateFormatOptions.Default));
            this.userService.update(updatedUser);
        }else{
            String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);
            User newUser = new User(date,date,inpUsername.getText(),inpEmail.getText(),inpPassword.getText(),inpRole.getValue().toString());
            this.userService.create(newUser);
        }
        this.anchorForm.setVisible(false);
        this.anchorTable.setVisible(true);

        windowManager.setRoot(WindowManager.SCENES.USER_MANAGEMENT_SCREEN);
    }

    // Typing this is pretty difficult as the hashmap should be string,object. To get around this we foce string string and convert the ID
    // to int upon the user object creation.
    public Integer parseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // This will never return null. In this instance the value passed in is always a string
            return null;
        }
    }
}
