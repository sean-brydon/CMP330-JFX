package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.ProjectService;
import CMP330.database.TaskService;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.Project;
import CMP330.model.Tasks;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksController extends LayoutController {

    @FXML
    TableView listOfTasks;
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
    TextField inpTask;
    @FXML
    ComboBox inpStatus;
    @FXML
    ComboBox inpResearcher;
    @FXML
    ComboBox inpProject;


    @Inject
    ProjectService projectService;

    @Inject
    UserService userService;

    @Inject
    TaskService taskService;

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected
    private List<Tasks> allTasks;
    private User currentUser;

    private Boolean editState = false;
    private Tasks selectedTask;

    @FXML
    @Override
    public void initialize() {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all Customers on load and populate list
        populateTable();

        // Populate fields
        if (permissionCheck(User.USER_ROLES.SYS_ADMIN)) {
            btnDel.setDisable(false);
            btnCreate.setDisable(false);
            btnEdit.setDisable(false);
        } else if (permissionCheck(User.USER_ROLES.OFFICE_ADMIN)) {
            btnDel.setDisable(false);
            btnCreate.setDisable(true);
            btnEdit.setDisable(true);
        } else {
            btnDel.setDisable(true);
            btnCreate.setDisable(true);
            btnEdit.setDisable(true);

        }
    }

    private Boolean permissionCheck(User.USER_ROLES requiredRole) {
        String userRole = currentUser.getRole();
        return userRole.equals(requiredRole.getRole());
    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allTasks = this.taskService.getAllTasks();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));

        TableColumn<Map, String> titleColumn = new TableColumn<>("Task");
        titleColumn.setCellValueFactory(new MapValueFactory<>("task"));

        TableColumn<Map, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));

        TableColumn<Map, String> researcherColumn = new TableColumn<>("Assigned To");
        researcherColumn.setCellValueFactory(new MapValueFactory<>("assignedToId"));

        TableColumn<Map, String> projectColumn = new TableColumn<>("Project ");
        projectColumn.setCellValueFactory(new MapValueFactory<>("projectId"));

        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));

        this.listOfTasks.getColumns().add(idColumn);
        this.listOfTasks.getColumns().add(titleColumn);
        this.listOfTasks.getColumns().add(projectColumn);
        this.listOfTasks.getColumns().add(researcherColumn);
        this.listOfTasks.getColumns().add(statusColumn);
        this.listOfTasks.getColumns().add(createdAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allTasks.forEach(task -> {
            Map<String, Object> projectObject = new HashMap<>();
            // Forces int to be string to be converted back to int
            projectObject.put("id", String.valueOf(task.getId()));
            projectObject.put("task", task.getTask());
            projectObject.put("status", task.getStatus());
            projectObject.put("projectId", task.getProjectId().getTitle());
            projectObject.put("assignedToId", task.getAssignedToId().getName());
            projectObject.put("createdAt", task.getCreatedAt());
            items.add(projectObject);
        });

        // Add all the users to the table
        this.listOfTasks.getItems().addAll(items);
    }

    @FXML
    private void deleteCustomer() {
        Tasks task = getTaskFromSelection();

        this.taskService.delete(task);


        windowManager.setRoot(WindowManager.SCENES.CUSTOMER_MANAGEMENT_SCREEN);

    }


    @FXML
    private void editSelectedCustomer() {
        this.selectedTask = getTaskFromSelection();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        this.inpProject.setDisable(true);
        lblUserManagement.setText("Edit Task");

        inpTask.setText(this.selectedTask.getTask());
        for (Project.PROJECT_STATUS status : Project.PROJECT_STATUS_ARRAY
        ) {
            this.inpStatus.getItems().add(status.getStatus());
        }
        for (User user : this.userService.getAllUsers()
        ) {
            this.inpResearcher.getItems().add(user.getName());
        }
        inpProject.getItems().add(this.selectedTask.getProjectId().getTitle());
        inpProject.getSelectionModel().selectFirst();
    }

    @FXML
    private void createNewCustomer() {
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create Task");

        for (Project.PROJECT_STATUS status : Project.PROJECT_STATUS_ARRAY
        ) {
            this.inpStatus.getItems().add(status.getStatus());
        }
        for (User user : this.userService.getAllUsers()
        ) {
            this.inpResearcher.getItems().add(user.getName());
        }
        for (Project project : this.projectService.getAllProjects()
        ) {
            this.inpProject.getItems().add(project.getTitle());
        }

    }

    private Tasks getTaskFromSelection() {
        TableView.TableViewSelectionModel selectionModel = this.listOfTasks.getSelectionModel();
        HashMap<String, String> selected = (HashMap<String, String>) selectionModel.getSelectedItem();

        Project project = this.projectService.getProjectsByTitle(selected.get("projectId"));
        User assignedTo = this.userService.findOneByUsername(selected.get("assignedToId"));

        Tasks selectionTask = new Tasks(parseIntOrNull(selected.get("id")),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                selected.get("task"),
                selected.get("status"),
                project,
                assignedTo
        );

        return selectionTask;
    }

    @FXML
    private void onSaveEvent() {
        if (this.editState) {
            this.taskService.updateTask(new Tasks(
                    this.selectedTask.getId(),
                    this.selectedTask.getCreatedAt(),
                    DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                    inpTask.getText(),
                    inpStatus.getValue().toString(),
                    this.projectService.getProjectsByTitle(inpProject.getValue().toString()),
                    this.userService.findOneByUsername(inpResearcher.getValue().toString())
            ));
        } else {
            String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);
            Tasks newTask = new Tasks(date,
                    date,
                    inpTask.getText(),
                    inpStatus.getValue().toString(),
                    this.projectService.getProjectsByTitle(inpProject.getValue().toString()),
                    this.userService.findOneByUsername(inpResearcher.getValue().toString()));
            this.taskService.createTask(newTask);
        }
        this.anchorForm.setVisible(false);
        this.anchorTable.setVisible(true);

        windowManager.setRoot(WindowManager.SCENES.TASK_MANAGEMENT_SCREEN);
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
