package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.CustomerService;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.Customer;
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

public class CustomersController extends LayoutController {

    // [x] Get all users
    // [] Delete button
    // [] Edit button -> Takes user to a form
    // [] Create New User -> Takes user to a form

    @FXML
    TableView listOfCustomers;
    @FXML
    Button btnDelUser;
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

    @Inject
    CustomerService customerService; // This could be removed if the LayoutController userService was set to protected however

    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected however
    private List<Customer> allCustomers;
    private User currentUser;

    private Boolean editState = false;
    private Customer selectedCustomer;

    @FXML
    @Override
    public void initialize() throws IOException {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all users on load and populate list
        // Storing users to state to allow filtering of the whole array - Useful when passing the UID to a new form.
        populateTable();
        btnDelUser.setDisable(!permissionCheck(User.USER_ROLES.SYS_ADMIN));
        // Add items roles to the edit/create new box

    }

    private Boolean permissionCheck(User.USER_ROLES requiredRole) {
        String userRole = currentUser.getRole();
        return userRole.equals(requiredRole.getRole());
    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allCustomers = this.customerService.getAllCustomers();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("cutomerId"));

        TableColumn<Map, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));

        TableColumn<Map, String> addressColumn = new TableColumn<>("address");
        addressColumn.setCellValueFactory(new MapValueFactory<>("address"));

        TableColumn<Map, String> postcodeColumn = new TableColumn<>("postcode");
        postcodeColumn.setCellValueFactory(new MapValueFactory<>("postcode"));

        TableColumn<Map, String> emailColumn = new TableColumn<>("email");
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));

        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));
        this.listOfCustomers.getColumns().add(idColumn);
        this.listOfCustomers.getColumns().add(nameColumn);
        this.listOfCustomers.getColumns().add(emailColumn);
        this.listOfCustomers.getColumns().add(postcodeColumn);
        this.listOfCustomers.getColumns().add(addressColumn);
        this.listOfCustomers.getColumns().add(createdAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allCustomers.forEach(customer -> {
            Map<String, Object> customerObj = new HashMap<>();
            // Forces int to be string to be converted back to int
            customerObj.put("id", String.valueOf(customer.getCustomerId()));
            customerObj.put("name", customer.getName());
            customerObj.put("email", customer.getEmail());
            customerObj.put("postcode", customer.getPostcode());
            customerObj.put("address", customer.getAddress());
            items.add(customerObj);
        });

        // Add all the users to the table
        this.listOfCustomers.getItems().addAll(items);
    }

    @FXML
    private void deleteUser() {
        Customer customer = getCustomerFromSelection();

        this.customerService.delete(customer);


        populateTable();
    }


    @FXML
    private void editSelectedUser() {
        this.selectedCustomer = getCustomerFromSelection();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Edit User");

        // Populate fields
        inpUsername.setText(this.selectedCustomer.getName());
        inpEmail.setText(this.selectedCustomer.getEmail());
        inpPassword.setText(this.selectedCustomer.getPostcode());

    }

    @FXML
    private void createNewUser() {
        this.selectedCustomer = getCustomerFromSelection();
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create User");
    }

    private Customer getCustomerFromSelection() {
        TableView.TableViewSelectionModel selectionModel = this.listOfCustomers.getSelectionModel();
        HashMap<String, String> selected = (HashMap<String, String>) selectionModel.getSelectedItem();

        Customer customer = new Customer(parseIntOrNull(selected.get("id")),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                selected.get("address"),
                selected.get("postcode"),
                selected.get("email"),
                selected.get("name"));
        // This was the easiest way to get the id back to an int.
        return customer;
    }

    @FXML
    private void onSaveEvent() {
        if (this.editState) {
            Customer updatedCustomer = this.selectedCustomer;
            updatedCustomer.setEmail(inpEmail.getText());
            updatedCustomer.setName(inpUsername.getText());

            updatedCustomer.setUpdatedAt(DateFns.customDateFormat(DateFns.DateFormatOptions.Default));
            this.customerService.update(updatedCustomer);
        } else {
            String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);

//            this.customerService.create(newUser);
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
