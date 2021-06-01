package CMP330.controller;

import CMP330.Utils.DateFns;
import CMP330.Utils.UserSingleton;
import CMP330.database.CustomerService;
import CMP330.database.InvoiceService;
import CMP330.gui.WindowManager;
import CMP330.model.Customer;
import CMP330.model.Invoices;
import CMP330.model.User;
import com.google.inject.Inject;
import com.j256.ormlite.stmt.query.In;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoicesController extends LayoutController {

    @FXML
    TableView listOfInvoices;
    @FXML
    Button btnDelCustomer;
    @FXML
    AnchorPane anchorForm;
    @FXML
    AnchorPane anchorTable;
    @FXML
    Label lblUserManagement;
    @FXML
    ComboBox inpCustomers;
    @FXML
    TextField inpAmountOwed;
    @FXML
    TextField inpAmountPaid;
    @FXML
    TextField inpSchedule;

    @Inject
    InvoiceService invoiceService;
    @Inject
    CustomerService customerService;
    @Inject
    WindowManager windowManager; // This could be removed if the LayoutController userService was set to protected however
    private List<Invoices> allInvoices;
    private User currentUser;

    private Boolean editState = false;
    private Invoices selectedInvoice;

    @FXML
    @Override
    public void initialize() {
        this.currentUser = UserSingleton.getInstance().getUser();

        // Get all Customers on load and populate list
        populateTable();

    }

    private void populateTable() {
        // This is moved here so we can Prefetch the data easily when we preform any CRUD actions on the user
        this.allInvoices = this.invoiceService.getAllInvoicess();
        // Creat columns for users
        TableColumn<Map, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));

        TableColumn<Map, String> customerColumn = new TableColumn<>("customer");
        customerColumn.setCellValueFactory(new MapValueFactory<>("Customer"));

        TableColumn<Map, String> amountOwedColumn = new TableColumn<>("amountOwed");
        amountOwedColumn.setCellValueFactory(new MapValueFactory<>("Amount Owed"));

        TableColumn<Map, String> amountPaidColumn = new TableColumn<>("amountPaid");
        amountPaidColumn.setCellValueFactory(new MapValueFactory<>("Amount Paid"));

        ;
        TableColumn<Map, String> createdAt = new TableColumn<>("createdAt");
        createdAt.setCellValueFactory(new MapValueFactory<>("createdAt"));
        this.listOfInvoices.getColumns().add(idColumn);
        this.listOfInvoices.getColumns().add(customerColumn);
        this.listOfInvoices.getColumns().add(amountPaidColumn);
        this.listOfInvoices.getColumns().add(amountOwedColumn);
        this.listOfInvoices.getColumns().add(createdAt);
        // Create an observable list to add customers to
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        this.allInvoices.forEach(invoice -> {
            Map<String, Object> customerObj = new HashMap<>();
            // Forces int to be string to be converted back to int
            customerObj.put("id", String.valueOf(invoice.getCustomerId()));
            customerObj.put("customer", invoice.getCustomerId().getName());
            customerObj.put("amountOwed", invoice.getAmountOwed());
            customerObj.put("amountPaid", invoice.getAmountPaid());
            customerObj.put("createdAt", invoice.getCreatedAt());
            items.add(customerObj);
        });

        // Add all the users to the table
        this.listOfInvoices.getItems().addAll(items);
    }

    @FXML
    private void deleteCustomer() {
        Invoices invoices = getInvoiceFromSelection();

        this.invoiceService.delete(invoices);


        windowManager.setRoot(WindowManager.SCENES.CUSTOMER_MANAGEMENT_SCREEN);

    }


    @FXML
    private void editSelectedCustomer() {
        this.selectedInvoice = getInvoiceFromSelection();
        this.editState = true;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Edit Customer");

        // Populate fields
        inpAmountOwed.setText(this.selectedInvoice.getAmountOwed().toString());
        inpAmountPaid.setText(this.selectedInvoice.getAmountPaid().toString());
        inpSchedule.setText("Monthly");

       inpCustomers.getItems().add(this.selectedInvoice.getCustomerId().getName());

    }

    @FXML
    private void createNewCustomer() {
        this.editState = false;
        this.anchorForm.setVisible(true);
        this.anchorTable.setVisible(false);
        lblUserManagement.setText("Create Customer");


        for (Customer customer : this.customerService.getAllCustomers()
        ) {
            inpCustomers.getItems().add(customer.getName());
        }
    }

    private Invoices getInvoiceFromSelection() {
        TableView.TableViewSelectionModel selectionModel = this.listOfInvoices.getSelectionModel();
        HashMap<String, String> selected = (HashMap<String, String>) selectionModel.getSelectedItem();

        Invoices invoice = new Invoices(parseIntOrNull(selected.get("id")),
                selected.get("createdAt"),
                DateFns.customDateFormat(DateFns.DateFormatOptions.Default),
                this.customerService.findOneByName(selected.get("customer")),
                Float.parseFloat(selected.get("amountOwed")),
                Float.parseFloat(selected.get("amountPaid")),
                inpSchedule.getText());
        // This was the easiest way to get the id back to an int.
        return invoice;
    }

    @FXML
    private void onSaveEvent() {
        String date = DateFns.customDateFormat(DateFns.DateFormatOptions.Default);
        if (this.editState) {
            this.invoiceService.update(new Invoices((this.selectedInvoice.getId()),
                    this.selectedInvoice.getCreatedAt(),
                    date,
                    this.customerService.findOneByName(inpCustomers.getValue().toString()),
                    Float.parseFloat(inpAmountOwed.getText()),
                    Float.parseFloat(inpAmountPaid.getText()),
                    inpSchedule.getText()
            ));
        } else {
            this.invoiceService.create(new Invoices(
                    date,
                    date,
                    this.customerService.findOneByName(inpCustomers.getValue().toString()),
                    Float.parseFloat(inpAmountOwed.getText()),
                    Float.parseFloat(inpAmountPaid.getText()),
                    inpSchedule.getText()
            ));
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
