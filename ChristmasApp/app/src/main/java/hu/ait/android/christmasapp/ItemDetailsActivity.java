package hu.ait.android.christmasapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hu.ait.android.christmasapp.data.Item;
import io.realm.Realm;

public class ItemDetailsActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_CITY";

    private Item itemToDisplay = null;
    private String itemName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        handleIntent();

        setupUI();
    }



    private void handleIntent() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_ID);
        itemToDisplay = getRealm().where(Item.class)
                .equalTo("itemID", itemID)
                .findFirst();
        itemName = itemToDisplay.getItemName();
    }

    private void setupUI() {

        TextView tvItemName = (TextView) findViewById(R.id.tvItemName);
        TextView tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        TextView tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        TextView tvItemCategory = (TextView) findViewById(R.id.tvItemCategory);

        tvItemName.setText(itemName);
        tvItemDescription.setText(itemToDisplay.getDescription());
        tvItemPrice.setText(itemToDisplay.getItemPrice());
        tvItemCategory.setText(itemToDisplay.getItemCategory());


    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }


}

