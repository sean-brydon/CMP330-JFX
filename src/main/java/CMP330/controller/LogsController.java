package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.AuditLogService;
import CMP330.database.CustomerService;
import CMP330.database.InvoiceService;
import CMP330.gui.WindowManager;
import CMP330.model.AuditLog;
import CMP330.model.Customer;
import CMP330.model.Invoices;
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

public class LogsController extends LayoutController {

    @FXML
    TableView listOfLogs;

    @Inject
    AuditLogService auditLogService;
    private List<AuditLog> allLogs;


    @FXML
    @Override
    public void initialize() {
        // Get all Customers on load and populate list
        populateTable();

    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allLogs = this.auditLogService.getAllLogs();
        // Creat columns for users


        TableColumn<Map, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new MapValueFactory<>("action"));

        TableColumn<Map, String> actionByColumn = new TableColumn<>("By");
        actionByColumn.setCellValueFactory(new MapValueFactory<>("actionByUserId"));

        TableColumn<Map, String> actionAt = new TableColumn<>("At");
        actionAt.setCellValueFactory(new MapValueFactory<>("createdAt"));
        this.listOfLogs.getColumns().add(actionColumn);
        this.listOfLogs.getColumns().add(actionByColumn);
        this.listOfLogs.getColumns().add(actionAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allLogs.forEach(log -> {
            Map<String, Object> obj = new HashMap<>();
            // Forces int to be string to be converted back to int
            obj.put("action", log.getAction());
            obj.put("actionByUserId", log.getActionByUserId().getName());
            obj.put("createdAt", log.getCreatedAt());
            items.add(obj);
        });

        // Add all the users to the table
        this.listOfLogs.getItems().addAll(items);
    }


}
