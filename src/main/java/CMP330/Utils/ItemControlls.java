package CMP330.Utils;

import java.util.List;

public class ItemControlls {

    // Type generics so this can be used in multiple places.
    public static <T> T NextItem(List<T> list, int currentPos) {
        if (currentPos > (list.size() - 1) || currentPos < 0) return null;



        // If it doesnt exist return the first element in the array
        return list.get(currentPos);
    }

    public static <T> T currentItem(List<T> list, int currentPos) {
        // Get the current item from the pointer in the list
        return list.get(currentPos);
    }

}
