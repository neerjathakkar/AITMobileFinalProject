package hu.ait.android.christmasapp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by neerjathakkar on 12/7/17.
 */

// use singleton design pattern
    // just store the list of categories here



public class ChristmasListModel {
    // instance of myself in the class - singleton design pattern
    private static ChristmasListModel christmasListModel = null;

    // private constructor
    private ChristmasListModel() {
    }

    public static ChristmasListModel getInstance() {
        if (christmasListModel == null) {
            christmasListModel = new ChristmasListModel();
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
