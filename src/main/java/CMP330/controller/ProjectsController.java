package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.CustomerService;
import CMP330.database.ProjectService;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.Customer;
import CMP330.model.Project;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsController extends LayoutController {

    @FXML
    TableView listOfCustomers;
    @FXML
    Button btnDelCustomer;
    @FXML
    AnchorPane anchorForm;
    @FXML
    AnchorPane anchorTable;
    @FXML
    Label lblUserManagement;
    @FXML
    TextField inpTitle;
    @FXML
    ComboBox inpStatus;
    @FXML
    ComboBox inpCustomer;
    @FXML
    ComboBox inpResearcher;
    @FXML
    ComboBox inpHeadResearcher;

    @Inject
    ProjectService projectService;

    @Inject
    UserService userService;

    @Inject
    CustomerService customerService;

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected
    private List<Project> allProjects;
    private User currentUser;

    private Boolean editState = false;
    private Project selectedProject;

    @FXML
    @Override
    public void initialize() {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all Customers on load and populate list
        populateTable();

        // Populate fields


        btnDelCustomer.setDisable(!permissionCheck(User.USER_ROLES.SYS_ADMIN));

    }

    private Boolean permissionCheck(User.USER_ROLES requiredRole) {
        String userRole = currentUser.getRole();
        return userRole.equals(requiredRole.getRole());
    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allProjects = this.projectService.getAllProjects();
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

        this.listOfCustomers.getColumns().add(idColumn);
        this.listOfCustomers.getColumns().add(titleColumn);
        this.listOfCustomers.getColumns().add(headResearcher);
        this.listOfCustomers.getColumns().add(researcherColumn);
        this.listOfCustomers.getColumns().add(statusColumn);
        this.listOfCustomers.getColumns().add(createdAt);
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
        this.listOfCustomers.getItems().addAll(items);
    }

    @FXML
    private void deleteCustomer() {
        Project project = getProjectFromSelectedValue();

        this.projectService.delete(project);


        windowManager.setRoot(WindowManager.SCENES.CUSTOMER_MANAGEMENT_SCREEN);

    }


    @FXML
    private void editSelectedCustomer() {
        this.selectedProject = getProjectFromSelectedValue();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        this.inpCustomer.setDisable(true);
        lblUserManagement.setText("Edit Customer");

        inpTitle.setText(this.selectedProject.getTitle());
        for (Project.PROJECT_STATUS status : Project.PROJECT_STATUS_ARRAY
        ) {
            this.inpStatus.getItems().add(status.getStatus());
        }
        for (User user : this.userService.getAllUsers()
        ) {
            this.inpHeadResearcher.getItems().add(user.getName());
            this.inpResearcher.getItems().add(user.getName());
        }
        inpCustomer.getItems().add(this.selectedProject.getCustomer().getName());
        inpCustomer.getSelectionModel().selectFirst();
//        addItemsToComboBox();
    }

    @FXML
    private void createNewCustomer() {
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create Customer");

        for (Project.PROJECT_STATUS status : Project.PROJECT_STATUS_ARRAY
        ) {
            this.inpStatus.getItems().add(status.getStatus());
        }
        for (User user : this.userService.getAllUsers()
        ) {
            this.inpHeadResearcher.getItems().add(user.getName());
            this.inpResearcher.getItems().add(user.getName());
        }
        for (Customer customer   : this.customerService.getAllCustomers()
        ) {
            this.inpCustomer.getItems().add(customer.getName());
        }

    }

    private void addItemsToComboBox() {
        for (Project.PROJECT_STATUS status : Project.PROJECT_STATUS_ARRAY
        ) {
            this.inpStatus.getItems().add(status.getStatus());
        }
        for (User user : this.userService.getAllUsers()
        ) {
            this.inpHeadResearcher.getItems().add(user.getName());
            this.inpResearcher.getItems().add(user.getName());
        }
    }

    private Project getProjectFromSelectedValue() {
        TableView.TableViewSelectionModel selectionModel = this.listOfCustomers.getSelectionModel();
        HashMap<String, String> selected = (HashMap<String, String>) selectionModel.getSelectedItem();

        User researcher = this.userService.findOneByUsername(selected.get("researcher"));
        User headResearcher = this.userService.findOneByUsername(selected.get("headResearcher"));
        Customer customer = this.customerService.findOneByName(selected.get("customer"));

        Project project = new Project(parseIntOrNull(selected.get("projectId")),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                selected.get("title"),
                selected.get("status"),
                customer,
                researcher,
                headResearcher
        );
        return project;
    }

    @FXML
    private void onSaveEvent() {
        if (this.editState) {
            Project updatedProject = this.selectedProject;
            updatedProject.setTitle(inpTitle.getText());
            updatedProject.setStatus(inpStatus.getValue().toString());
            updatedProject.setResearcher(this.userService.findOneByUsername(inpResearcher.getValue().toString()));
            updatedProject.setHeadResearcher(this.userService.findOneByUsername(inpHeadResearcher.getValue().toString()));


            updatedProject.setUpdatedAt(DateFns.customDateFormat(DateFns.DateFormatOptions.Default));
            this.projectService.updateProject(updatedProject);
        } else {
            String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);
            Project newProject = new Project(date,
                    date,
                    inpTitle.getText(),
                    inpStatus.getValue().toString(),
                    this.customerService.findOneByName(inpCustomer.getValue().toString()),
                    this.userService.findOneByUsername(inpResearcher.getValue().toString()),
                    this.userService.findOneByUsername(inpHeadResearcher.getValue().toString()));
            this.projectService.createProject(newProject);
        }
        this.anchorForm.setVisible(false);
        this.anchorTable.setVisible(true);

        windowManager.setRoot(WindowManager.SCENES.PROJECT_MANAGEMENT_SCREEN);
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
