package hu.ait.android.christmasapp.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    @PrimaryKey
    private  String itemID;

    private String itemName = "";


    public Item() {
    }

    public Item(String itemID, String itemName) {
        this.itemID = itemID;
        this.itemName = itemName;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
