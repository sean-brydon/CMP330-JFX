package CMP330.controller;

import CMP330.Utils.UserSingleton;
import CMP330.Utils.Validator;
import CMP330.Utils.ValidatorOptions;
import CMP330.database.UserService;
import CMP330.gui.WindowManager;
import CMP330.model.User;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField inpUsername;
    public Label lblErrorMessage;
    public PasswordField inpPassword;
    public Button bntLogin;

    private int id;
    private String role;
    private int count;


    @Inject
    private UserService userService;

    @Inject
    private WindowManager windowManager;

    @FXML
    private User loginUser() throws Exception {
        System.out.println("Here");
        try {
            // Sanitise input;
            String username = Validator.checkString(inpUsername.getText(), new ValidatorOptions[]{ValidatorOptions.isNotEmpty});
            String password = Validator.checkString(inpPassword.getText(), new ValidatorOptions[]{ValidatorOptions.isNotEmpty});
            User user = userService.loginUser(username, password);
            // Set user to singleton instance
            storeUser(user);

            // Handle Scene
            windowManager.setRoot(WindowManager.SCENES.USER_DASHBOARD_SCREEN);

        } catch (Exception e) {
            lblErrorMessage.setText(e.getMessage());
            throw e;
        }
        Stage stage =(Stage) bntLogin.getScene().getWindow();
        stage.close();
        return null;
    }


    private void storeUser(User user) {
        UserSingleton userStore = UserSingleton.getInstance();
        userStore.setUser(user);
    }

    @FXML
    private void sendToRegister(ActionEvent event) {
        windowManager.setRoot(WindowManager.SCENES.USER_SIGNUP_SCREEN);
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
