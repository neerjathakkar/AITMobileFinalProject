package hu.ait.android.christmasapp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by neerjathakkar on 12/7/17.
 */


public class ChristmasListModel {

    private static ChristmasListModel christmasListModel = null;

    private ChristmasListModel() {
    }

    public static ChristmasListModel getInstance() {
        if (christmasListModel == null) {
            christmasListModel = new ChristmasListModel();
            christmasListModel.addToCategories("All items");
        }

        return christmasListModel;
    }

    public Set<String> categories = new HashSet<String>();

    public Set<String> getCategories() {
        return categories;
    }

    public void addToCategories(String category){
        categories.add(category);
    }

}
