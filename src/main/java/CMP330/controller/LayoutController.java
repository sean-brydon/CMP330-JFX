package CMP330.controller;


import CMP330.Utils.UserSingleton;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class LayoutController {

    // Main content
    @FXML
    public AnchorPane mainContent;
    @Inject
    private UserService userService;
    @Inject
    private WindowManager windowManager;

    @Inject
    private FXMLLoader fxmlLoader;

    // Labels
    public Label lblUser;
    public Button btnLogout;

    // Nav buttons
    public Button navDash;
    public Button navCompleted;
    public Button navProjects;
    public Button navTasks;
    public Button navCustomers;
    public Button navInvoices;
    public Button navUsers;
    public Button navLogs;


    @FXML
    public void initialize() throws IOException {
        // Get the current user
        User currentUser = UserSingleton.getInstance().getUser();
        lblUser.setText(currentUser.getName());
    }


    @FXML
    private void logoutUser()  {
        UserSingleton.getInstance().setUser(null);
        windowManager.setRoot(WindowManager.SCENES.USER_LOGIN_SCREEN);
//        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

    }


    // Navigation
    private void addStyles(Button button, Boolean underline, Color color) {
        button.setUnderline(underline);
        button.setTextFill(color);
    }

    private void removeStylesFromNav() {
        Button[] navigationButtons = {navDash, navCompleted, navTasks, navCustomers, navUsers, navLogs, navProjects, navInvoices};
        for (Button button : navigationButtons) {
            addStyles(button, false, Color.BLACK);
        }
    }

    @FXML
    private void navDash(){
        Stage stage = (Stage) navDash.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }

    @FXML
    private void navCompleted(){
        Stage stage = (Stage) navCompleted.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }
    @FXML
    private void navProjects(){
        Stage stage = (Stage) navProjects.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }
    @FXML
    private void navTasks(){
        Stage stage = (Stage) navTasks.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }

    @FXML
    private void navCustomers(){
        Stage stage = (Stage) navCustomers.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }
    @FXML
    private void navInvoices(){
        Stage stage = (Stage) navInvoices.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }

    @FXML
    private void navUsers(){
        Stage stage = (Stage) navUsers.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_MANAGEMENT_SCREEN);
    }
    @FXML
    private void navLogs(){
        Stage stage = (Stage) navLogs.getScene().getWindow();
        stage.close();
        windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);
    }
}