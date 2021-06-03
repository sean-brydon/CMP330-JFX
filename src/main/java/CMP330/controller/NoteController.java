package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.NoteService;
import CMP330.database.ProjectService;
import CMP330.gui.WindowManager;
import CMP330.model.Customer;
import CMP330.model.Note;
import CMP330.model.Project;
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

public class NoteController extends LayoutController {

    @FXML
    TableView listOfCustomers;
    @FXML
    Button btnDelCustomer;
    @FXML
    Button btnEditUser;
    @FXML
    Button btnCreateUser;
    @FXML
    AnchorPane anchorForm;
    @FXML
    AnchorPane anchorTable;
    @FXML
    Label lblUserManagement;
    @FXML
    TextField inpNote;
    @FXML
    ComboBox inpProject;

    @Inject
    NoteService noteService;

    @Inject
    ProjectService projectService;

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected however
    private List<Note> allNotes;
    private User currentUser;

    private Boolean editState = false;
    private Note selectedNote;

    @FXML
    @Override
    public void initialize() {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all Customers on load and populate list
        populateTable();
        btnDelCustomer.setDisable(!permissionCheck(User.USER_ROLES.SYS_ADMIN));
        btnEditUser.setDisable(!permissionCheck(User.USER_ROLES.SYS_ADMIN));

    }

    private Boolean permissionCheck(User.USER_ROLES requiredRole) {
        String userRole = currentUser.getRole();
        return userRole.equals(requiredRole.getRole());
    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allNotes = this.noteService.getAllNotes();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));

        TableColumn<Map, String> textCol = new TableColumn<>("text");
        textCol.setCellValueFactory(new MapValueFactory<>("text"));

        TableColumn<Map, String> projectColumn = new TableColumn<>("project");
        projectColumn.setCellValueFactory(new MapValueFactory<>("project"));

        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));
        this.listOfCustomers.getColumns().add(idColumn);
        this.listOfCustomers.getColumns().add(textCol);

        this.listOfCustomers.getColumns().add(projectColumn);
        this.listOfCustomers.getColumns().add(createdAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allNotes.forEach(note -> {
            Map<String, Object> noteObj = new HashMap<>();
            // Forces int to be string to be converted back to int
            noteObj.put("id", String.valueOf(note.getId()));
            noteObj.put("text", note.getText());
            noteObj.put("project", note.getProject().getTitle());
            noteObj.put("createdAt", note.getCreatedAt());
            items.add(noteObj);
        });

        // Add all the users to the table
        this.listOfCustomers.getItems().addAll(items);
    }

    @FXML
    private void deleteNote() {
        Note note = getNoteFromSelection();

        this.noteService.delete(note);


        windowManager.setRoot(WindowManager.SCENES.CUSTOMER_MANAGEMENT_SCREEN);

    }


    @FXML
    private void editSelected() {
        this.selectedNote = getNoteFromSelection();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Edit Customer");

        // Populate fields
        inpNote.setText(this.selectedNote.getText());
        inpProject.getItems().add(this.selectedNote.getProject().getTitle());
        inpProject.getSelectionModel().selectFirst();

    }

    @FXML
    private void createNewCustomer() {
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create Note");

        for (Project project : this.projectService.getAllProjects()
        ) {
            inpProject.getItems().add(project.getTitle());

        }

    }

    private Note getNoteFromSelection() {
        TableView.TableViewSelectionModel selectionModel = this.listOfCustomers.getSelectionModel();
        HashMap<String, String> selected = (HashMap<String, String>) selectionModel.getSelectedItem();

        Note note = new Note(parseIntOrNull(selected.get("id")),
                selected.get("text"),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                this.projectService.getProjectsByTitle(selected.get("project")),
                this.currentUser);
        // This was the easiest way to get the id back to an int.
        return note;
    }

    @FXML
    private void onSaveEvent() {
        if (this.editState) {
            Note updatedNote = this.selectedNote;
            updatedNote.setText(inpNote.getText());
            updatedNote.setProject(this.selectedNote.getProject());

            updatedNote.setUpdatedAt(DateFns.customDateFormat(DateFns.DateFormatOptions.Default));
            this.noteService.update(updatedNote);
        } else {
            String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);
            Note newNote = new Note(
                    inpNote.getText(),
                    date,
                    date,
                    this.projectService.getProjectsByTitle(inpProject.getValue().toString()),
                    this.currentUser);
            this.noteService.create(newNote);
        }
        this.anchorForm.setVisible(false);
        this.anchorTable.setVisible(true);

        windowManager.setRoot(WindowManager.SCENES.CUSTOMER_MANAGEMENT_SCREEN);
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
