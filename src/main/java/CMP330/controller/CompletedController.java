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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedController extends LayoutController {

    @FXML
    TableView listOfTasks;

    @FXML
    TableView listOfProjects;

    @Inject
    ProjectService projectService;

    @Inject
    UserService userService;

    @Inject
    TaskService taskService;

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected
    private List<Tasks> allTasks;
    private List<Project> allProjects;
    private User currentUser;


    @FXML
    @Override
    public void initialize() {
        this.currentUser = UserSingleton.getInstance().getUser();

        populateTableTasks();
        populateTableProjects();

    }

    private void populateTableTasks() {
        this.allTasks = this.taskService.getAllCompletedTasks();
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

    private void populateTableProjects() {
        this.allProjects = this.projectService.getAllCompletedProjects();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("projectId"));

        TableColumn<Map, String> titleColumn = new TableColumn<>("title");
        titleColumn.setCellValueFactory(new MapValueFactory<>("title"));

        TableColumn<Map, String> customerColumn = new TableColumn<>("customer");
        customerColumn.setCellValueFactory(new MapValueFactory<>("customer"));

        TableColumn<Map, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));

        TableColumn<Map, String> researcherColumn = new TableColumn<>("researcher");
        researcherColumn.setCellValueFactory(new MapValueFactory<>("researcher"));

        TableColumn<Map, String> headResearcher = new TableColumn<>("headResearcher");
        headResearcher.setCellValueFactory(new MapValueFactory<>("headResearcher"));

        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));

        this.listOfProjects.getColumns().add(idColumn);
        this.listOfProjects.getColumns().add(titleColumn);
        this.listOfProjects.getColumns().add(headResearcher);
        this.listOfProjects.getColumns().add(researcherColumn);
        this.listOfProjects.getColumns().add(statusColumn);
        this.listOfProjects.getColumns().add(createdAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allProjects.forEach(project -> {
            Map<String, Object> projectObject = new HashMap<>();
            // Forces int to be string to be converted back to int
            projectObject.put("projectId", String.valueOf(project.getProjectId()));
            projectObject.put("title", project.getTitle());
            projectObject.put("status", project.getStatus());
            projectObject.put("customer", project.getCustomer().getName());
            projectObject.put("researcher", project.getResearcher().getName());
            projectObject.put("headResearcher", project.getHeadResearcher().getName());
            projectObject.put("createdAt", project.getCreatedAt());
            items.add(projectObject);
        });

        // Add all the users to the table
        this.listOfProjects.getItems().addAll(items);
    }


}
