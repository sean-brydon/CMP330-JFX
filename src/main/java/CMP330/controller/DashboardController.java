package CMP330.controller;


import CMP330.Utils.DateFns;
import CMP330.Utils.ItemControlls;
import CMP330.Utils.UserSingleton;
import CMP330.database.ProjectService;
import CMP330.database.TaskService;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.Project;
import CMP330.model.Tasks;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class DashboardController extends LayoutController {

    private final String[] quotes = new String[]{"You're amazing keep it up", "Remember! Its a bad day not a bad life", "An apple a day keeps the doctors away", "Live Laugh Love", "A house isnt a home without a cat"};


    @FXML
    private Label lblQuote;

    private User user;
    @FXML
    Label lblError;
    //region Project FXML
    // Projects FXML items
    @FXML
    TextField inpPTitle;
    @FXML
    TextField inpPCustomer;
    @FXML
    ComboBox<String> inpPStatus;
    @FXML
    TextField inpPStatusText;
    @FXML
    TextField inpPHResearcher;
    @FXML
    TextField inpPResearcher;
    @FXML
    Button btnPEdit;
    @FXML
    Button btnPSave;
    private int projectPos = 0;
    private List<Project> projects;
    private Project currentProject;

    @Inject
    ProjectService projectService;
    //endregion

    // Task code

    //region Task FXML
    @FXML
    TextField inpTTitle;
    @FXML
    ComboBox<String> inpTStatus;
    @FXML
    ComboBox<String> inpTAssignedToBox;
    @FXML
    TextField inpTStatusText;
    @FXML
    TextField inpTProject;
    @FXML
    TextField inpTResearcher;
    @FXML
    Button btnTEdit;
    @FXML
    Button btnTSave;
    @FXML
    Button btnTDelete;
    private int taskPos = 0;
    private List<Tasks> tasks;
    private List<User> allUsers;
    private Tasks currentTask;
    @Inject
    TaskService taskService;

    @Inject
    UserService userService;

    @Inject
    WindowManager windowManager;
    //endregion

    @Override
    @FXML
    public void initialize() {
        // Get the current user
        this.user = UserSingleton.getInstance().getUser();
        lblUser.setText(user.getName());
        // Set quote
        Random rand = new Random();
        lblQuote.setText(quotes[rand.nextInt(quotes.length)]);

        // Get all users for combo boxes assign it to private variable so it can be used in tasks and projects.
        this.allUsers = this.userService.getAllUsers();
        // Get projects on initial load
        populateProjects(this.projectPos);
        // Get tasks on initial load
        populateTasks(this.taskPos);
        // Disable input for roles that cannot edit.
        disableInput();
    }

    private void disableInput() {
        // Hide Edit components
        btnPSave.setVisible(false);
        btnTSave.setVisible(false);
        // Hide edit combo boxes
        inpPStatus.setVisible(false);

        inpTStatus.setVisible(false);
        inpTAssignedToBox.setVisible(false);

        // Disable when not editing
        inpPTitle.setDisable(true);
        inpPStatusText.setDisable(true);
        inpPCustomer.setDisable(true);
        inpPHResearcher.setDisable(true);
        inpPResearcher.setDisable(true);

        inpTTitle.setDisable(true);
        inpTStatusText.setDisable(true);
        inpTResearcher.setDisable(true);
        inpTProject.setDisable(true);
    }


    //region Projects
    private void populateProjects(int projectPos) {
        // Load all projects from project service
        this.projects = this.projectService.getAllProjects();



        if (this.projects.size() > 0) {
            this.currentProject = ItemControlls.currentItem(this.projects, 0);
            displayProjects(currentProject);
        }
    }

    // Seperated this function to call when the user clicks a button to change project.
    private void displayProjects(Project currentProject) {
        // Empty list to combox items to make sure there is no duplicate fields -> when updating this function is called again.
        inpPStatus.getItems().removeAll(inpPStatus.getItems());
        // Access the current project Position
        if (currentProject != null) {
            inpPTitle.setText(currentProject.getTitle());
            inpPCustomer.setText(currentProject.getCustomer().getName());
            inpPStatusText.setText(currentProject.getStatus());
            addProjectStatusToComboBox(inpPStatus);
            inpPHResearcher.setText(currentProject.getHeadResearcher().getName());
            inpPResearcher.setText(currentProject.getResearcher().getName());
        }
    }

    // Handle Project forwards/backwards
    @FXML
    private void nextProject() {
        this.projectPos += 1;
        if (this.projectPos > (this.projects.size() - 1)) {
            this.projectPos = 0;
        }
        this.currentProject = ItemControlls.currentItem(this.projects, this.projectPos);
        this.displayProjects(this.currentProject);
    }

    @FXML
    private void backProject() {
        this.projectPos -= 1;
        if (this.projectPos < 0) {
            this.projectPos = this.projects.size() - 1;
        }
        this.currentProject = ItemControlls.currentItem(this.projects, this.projectPos);
        this.displayProjects(this.currentProject);
    }

    @FXML
    private void EditProject() {
        String userRole = this.user.getRole();
        btnPEdit.setVisible(false);
        btnPSave.setVisible(true);
        // Show / Hide combo box
        inpPStatusText.setVisible(false);
        inpPStatus.setVisible(true);

        // Set Editable based on roles
        if (userRole.equals(User.USER_ROLES.SYS_ADMIN) || userRole.equals(User.USER_ROLES.HRESEARCHER)) {
            inpPTitle.setDisable(false);
            inpPStatus.setDisable(false);
            inpPCustomer.setDisable(false);
            inpPHResearcher.setDisable(false);
            inpPResearcher.setDisable(false);
            inpPStatus.getItems().add(Project.PROJECT_STATUS.COMPLETED.getStatus());

        } else if (userRole.equals(User.USER_ROLES.OFFICE_ADMIN)) {
            inpPTitle.setDisable(false);
            inpPStatus.setDisable(false);
            inpPCustomer.setDisable(false);
            inpPHResearcher.setDisable(false);
            inpPResearcher.setDisable(false);
        }
    }

    @FXML
    private void SaveProject() {
        // TODO: Sort reseracher and users etc etc...
        Project project = new Project(this.currentProject.getProjectId(), this.currentProject.getCreatedAt(), DateFns.customDateFormat(DateFns.DateFormatOptions.Default), inpPTitle.getText(), inpPStatus.getValue().toString(), this.currentProject.getCustomer(), this.currentProject.getResearcher(), this.currentProject.getHeadResearcher());
        Project updatedProject = this.projectService.updateProject(project);

        if(updatedProject==null){
            lblError.setText("Failed to update Project");
        }else{
            lblError.setText("Project Updated");
            btnPEdit.setVisible(true);
            btnPSave.setVisible(false);
            // Show / Hide combo box
            inpPStatusText.setVisible(true);
            inpPStatus.setVisible(false);
            populateProjects(this.projectPos);
            disableInput();
        }
    }
    //endregion

    //region Tasks
    // Tasks logic
    private void populateTasks(int projectPos){
        // Load all projects from project service
        try {
            this.tasks = this.taskService.getTasks(user);
        } catch (SQLException e) {
            // Projects is null this there is no data to display
            e.printStackTrace();
        }


        if (this.tasks != null && this.tasks.size() > 0) {
            this.currentTask = ItemControlls.currentItem(this.tasks, 0);
            displayTasks(this.currentTask);
        }else{
            // If user has no tasks dont display the edit buttons
            // This prevents the user from saving an empty task - potentially causing errors if not caught by the DAO
            btnTEdit.setVisible(false);
            btnTDelete.setVisible(false);
        }
    }

    // Separated this function to call when the user clicks a button to change project.
    private void displayTasks(Tasks tasks) {
        // Empty list to combo box items to make sure there is no duplicate fields -> when updating this function is called again.
        inpTStatus.getItems().removeAll(inpTStatus.getItems());
        // Access the current project Position
        if (currentTask!= null) {
            inpTTitle.setText(currentTask.getTask());
            inpTStatusText.setText(currentTask.getStatus());
            addProjectStatusToComboBox(inpTStatus);
            addUsersToComboBox(inpTAssignedToBox);
            inpTResearcher.setText(currentTask.getAssignedToId().getName());
            inpTProject.setText(currentTask.getProjectId().getTitle());
        }
    }



    // Handle Project forwards/backwards
    @FXML
    private void nextTask() {
        this.taskPos += 1;
        if (this.taskPos > (this.tasks.size() - 1)) {
            this.taskPos = 0;
        }
        this.currentTask = ItemControlls.currentItem(this.tasks, this.taskPos);
        this.displayTasks(this.currentTask);
    }

    @FXML
    private void backTask() {
        this.taskPos -= 1;
        if (this.taskPos < 0) {
            this.taskPos = this.tasks.size() - 1;
        }
        this.currentTask = ItemControlls.currentItem(this.tasks, this.taskPos);
        this.displayTasks(this.currentTask);
    }

    @FXML
    private void EditTask() {
        String userRole = this.user.getRole();
        btnTEdit.setVisible(false);
        btnTSave.setVisible(true);
        // Show / Hide combo box
        inpTStatusText.setVisible(false);
        inpTStatus.setVisible(true);
        inpTResearcher.setVisible(false);
        inpTAssignedToBox.setVisible(true);

        // Set Editable based on roles
        if (userRole.equals("SYSADMIN") || userRole.equals("HEAD_RESEARCHER")) {
            inpTTitle.setDisable(false);
            inpTStatus.setDisable(false);
            inpTProject.setDisable(false);
            inpTResearcher.setDisable(false);
            inpTStatus.getItems().add(Project.PROJECT_STATUS.COMPLETED.getStatus());

        } else if (userRole.equals("OFFICE_ADMIN")) {
            inpTTitle.setDisable(false);
            inpTStatus.setDisable(false);
            inpTProject.setDisable(false);
            inpTResearcher.setDisable(false);
        }
    }

    @FXML
    private void SaveTask() {

        // Search the users array for the current selected user.
        User newAssignedUser= this.allUsers.stream().filter(user -> inpTAssignedToBox.getValue().equals(user.getName())).findAny().orElse(null);
        // Update task with the new values
        Tasks tasks = new Tasks(this.currentTask.getId(), this.currentTask.getCreatedAt(), DateFns.customDateFormat(DateFns.DateFormatOptions.Default), inpTTitle.getText(), inpTStatus.getValue(),this.currentTask.getProjectId() ,newAssignedUser);
        Tasks updatedTask = this.taskService.updateTask(tasks);

        if(updatedTask==null){
            lblError.setText("Failed to update Task");
            if(inpTStatus.getValue().equals(null)) lblError.setText("Failed to Update Task - No status provided");
        }else{
            lblError.setText("Task Updated");
            btnTEdit.setVisible(true);
            btnTSave.setVisible(false);
            // Show / Hide combo box
            inpTStatusText.setVisible(true);
            inpTStatus.setVisible(false);
            inpTResearcher.setVisible(true);
            populateTasks(this.taskPos);
            disableInput();
        }
    }
    //endregion


    private void addProjectStatusToComboBox(ComboBox<String> inpTStatus) {
        for (Project.PROJECT_STATUS project_status : Project.PROJECT_STATUS_ARRAY) {
            // Remove the complete option if they are a researcher
            if(!(this.user.getRole().equals(User.USER_ROLES.SYS_ADMIN)) && project_status.getStatus().equals(Project.PROJECT_STATUS.COMPLETED)) break;

                inpTStatus.getItems().add(project_status.getStatus());

        }
    }

    private void addUsersToComboBox(ComboBox<String> comboBox) {
        for(User user: this.allUsers){
            comboBox.getItems().add(user.getName());
        }
    }
    @FXML
    private void navNotes(){
        Stage stage = (Stage) navLogs.getScene().getWindow();
        stage.close();
        if(UserSingleton.getInstance().getUser().getRole().equals(User.USER_ROLES.SYS_ADMIN)){
            windowManager.setRoot(WindowManager.SCENES.NOTES_SCREEN);
        }
    }
}
