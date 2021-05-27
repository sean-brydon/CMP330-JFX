package CMP330.gui;

import com.gluonhq.ignite.guice.GuiceContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class GUI extends Application {
    // GuiceContext is what holds the DI
    private GuiceContext guiceContext;
    @Override
    public void start(Stage primaryStage) throws Exception {

        guiceContext = new GuiceContext(this, () -> Arrays.asList(new GUIConfig()));
        guiceContext.init();
        final WindowManager stageController = guiceContext.getInstance(WindowManager.class);
        stageController.setRoot(WindowManager.SCENES.USER_LOGIN_SCREEN);
    }

    public void run(String[] args) {
        launch(args);
    }
}
