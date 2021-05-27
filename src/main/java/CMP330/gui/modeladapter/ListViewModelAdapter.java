package CMP330.gui.modeladapter;

import javafx.scene.control.ListCell;
import CMP330.gui.GUIRepresentable;


public class ListViewModelAdapter<T extends GUIRepresentable> extends ListCell<T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getTitle());
        } else {
            setText(null);
        }
    }

}
