package CMP330;

import com.google.inject.Guice;
import com.google.inject.Injector;
import CMP330.gui.GUI;


public class Main {

    public static void main(String[] args) {
        // Setup DI for javafx using guice
        Injector injector = Guice.createInjector();
        GUI gui = injector.getInstance(GUI.class);

        // Run the start scene method on the GUI class
        try {
            gui.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
