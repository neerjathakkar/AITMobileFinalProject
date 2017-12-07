package hu.ait.android.christmasapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import hu.ait.android.christmasapp.data.Item;
import hu.ait.android.christmasapp.MainActivity;
import io.realm.Realm;

import android.content.Context;


public class AddItemActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    private EditText etItem;
    private EditText etItemDesc;
    private EditText etItemPrice;
    private EditText etItemCategory;
    private CheckBox cbPurchased;
    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setupUI();

        initCreate();
    }

    private void initCreate() {
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }


    private void setupUI() {

        etItem = (EditText) findViewById(R.id.etItemName);
        etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        etItemCategory = (EditText) findViewById(R.id.etItemCategory);
        cbPurchased = (CheckBox) findViewById(R.id.cbPurchased);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

    private void saveItem() {

        String category = etItemCategory.getText().toString();
        Set<String> categories = ChristmasListModel.getInstance().getCategories();

        if (!(categories.contains(category))){
            ChristmasListModel.getInstance().addToCategories(category);
        }

        Intent intentResult = new Intent();

        getRealm().beginTransaction();
        itemToEdit.setItemName(etItem.getText().toString());
        itemToEdit.setDescription(etItemDesc.getText().toString());
        itemToEdit.setPurchased(cbPurchased.isChecked());
        itemToEdit.setItemPrice(etItemPrice.getText().toString());
        itemToEdit.setItemCategory(etItemCategory.getText().toString());
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }
}


