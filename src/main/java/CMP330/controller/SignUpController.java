package CMP330.controller;


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

public class SignUpController {

    public TextField inpUsername;
    public TextField inpEmail;
    public Label lblErrorMessage;
    public PasswordField inpPassword;
    public Button signupBtn;

    @Inject
    private UserService userService;

    @Inject
    private WindowManager windowManager;

    public void initialize(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void signUpUser(ActionEvent event) throws IOException {
        // We don't need to check if this returns a value due to the fact error handling is handled in the createUser function
        User newUser = createNewUser();
        this.sendToLogin(event);
    }

    // Handle routing to login component
    @FXML
    public void sendToLogin(ActionEvent event) throws IOException {
        windowManager.setRoot(WindowManager.SCENES.USER_LOGIN_SCREEN);
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

    }

    /**
     * @return
     */
    private User createNewUser() {
        try {
            // Sanitise input;
            String username = Validator.checkString(inpUsername.getText(), new ValidatorOptions[]{ValidatorOptions.isNotEmpty});
            String password = Validator.checkString(inpPassword.getText(), new ValidatorOptions[]{ValidatorOptions.isNotEmpty, ValidatorOptions.isPassword});
            String email = Validator.checkString(inpEmail.getText(), new ValidatorOptions[]{ValidatorOptions.isNotEmpty, ValidatorOptions.isEmail});
            return userService.createUser(username, password, email);
        } catch (Exception e) {
            lblErrorMessage.setText(e.getMessage());
        }
        return null;
    }
}
