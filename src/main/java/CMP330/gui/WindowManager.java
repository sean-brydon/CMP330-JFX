package CMP330.gui;

import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

public class WindowManager {

    @Inject
    private FXMLLoader fxmlLoader;

    private Scene scene;

    public enum SCENES {
        //TODO: REMOVE THIS ON EFINAL COMMIT

        // ---- SCREENS
        USER_SIGNUP_SCREEN("../controller/UserSignupTest.fxml"),
        USER_LOGIN_SCREEN("../controller/UserLogin.fxml"),
        PERSON_EDIT_SCENE("../controller/PersonEdit.fxml"),

        // ---- LAYOUT
        APP_LAYOUT("../controller/Layout.fxml"),
        // ---- Main Nav Screens
        USER_DASHBOARD_SCREEN("../controller/UserDashboard.fxml"),
        USER_MANAGEMENT_SCREEN("../controller/UserManagment.fxml"),
        CUSTOMER_MANAGEMENT_SCREEN("../controller/Customers.fxml"),
        PROJECT_MANAGEMENT_SCREEN("../controller/Projects.fxml");

        private String sceneName;

        SCENES(String scenePath) {
            this.sceneName = scenePath;
        }

        public String getSceneName() {
            return sceneName;
        }
    }

    public void setRoot(SCENES scene) {
        fxmlLoader.setRoot(null);
        fxmlLoader.setController(null);
        fxmlLoader.setLocation(getClass().getResource(scene.getSceneName()));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Wrong path: " + e.getMessage());
        }
        if(null == root) {
            throw new IllegalStateException("There was likely an error in the controller initialize() method.");
        }
        fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 750, 600));
        stage.show();
    }
}
