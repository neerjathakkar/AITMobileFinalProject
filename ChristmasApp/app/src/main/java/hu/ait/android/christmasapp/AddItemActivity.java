package hu.ait.android.christmasapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;

import hu.ait.android.christmasapp.data.Item;
import io.realm.Realm;


public class AddItemActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    public static final int REQUEST_SEARCH_RESULTS = 103;
    private static final String SEARCH_KEYWORD = "SEARCH_KEYWORD";
    private static final String SEARCH_DEPARTMENT = "SEARCH_DEPARTMENT";
    private EditText etItem;
    private EditText etItemDesc;
    private EditText etItemPrice;
    private EditText etItemCategory;
    private TextView tvAmazonDepartment;
    private Spinner spinnerAmazonDepartment;
    private Item itemToEdit = null;
    private LinearLayout layoutContent;

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

        setupAmazonSpinner();

        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etItem.getText().toString().isEmpty()) {
                    showSnackBarError("Please input at least one search keyword");
                }
                else { showSearchAmazonActivity(); }
            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    private void setupAmazonSpinner() {
        tvAmazonDepartment = (TextView) findViewById(R.id.tvAmazonDepartment);
        spinnerAmazonDepartment = (Spinner) findViewById(R.id.spinnerAmazonDepartment);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmazonDepartment.setAdapter(adapter);
    }

    private void showSnackBarError(String message) {

        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction("hide", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

    private void saveItem() {
        Log.e("search", "in saveItem()");

        String etItemText = etItem.getText().toString();
        if (TextUtils.isEmpty(etItemText)) {
            showSnackBarError("Please input an item name");
        }else{
            String category = etItemCategory.getText().toString();
            Set<String> categories = ChristmasListModel.getInstance().getCategories();

            if (!(categories.contains(category))){
                ChristmasListModel.getInstance().addToCategories(category);
            }

            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            itemToEdit.setItemName(etItem.getText().toString());
            itemToEdit.setDescription(etItemDesc.getText().toString());
            itemToEdit.setItemPrice(etItemPrice.getText().toString());
            itemToEdit.setItemCategory(etItemCategory.getText().toString());
            itemToEdit.setAmazonDepartment(spinnerAmazonDepartment.getSelectedItem().toString());
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            Log.e("search", "about to finish");
            finish();
        }

    }

    private void showSearchAmazonActivity() {
        Intent intentStart = new Intent(AddItemActivity.this, SearchAmazonActivity.class);
        intentStart.putExtra(SEARCH_KEYWORD, etItem.getText().toString());
        intentStart.putExtra(SEARCH_DEPARTMENT, spinnerAmazonDepartment.getSelectedItem().toString());
        Log.e("search crash", "intent created");
        startActivityForResult(intentStart, REQUEST_SEARCH_RESULTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_SEARCH_RESULTS) {
                    Log.e("search", "requested search results!");
                    String itemName = data.getStringExtra(SearchAmazonActivity.NAME_LIST);
                    String itemPrice = data.getStringExtra(SearchAmazonActivity.PRICE_LIST);
                    String itemDetailsUrl = data.getStringExtra(SearchAmazonActivity.URL_LIST);
                    etItem.setText(itemName);
                    etItemPrice.setText(itemPrice);
                    etItemDesc.setText(itemDetailsUrl);
                    saveItem();
                }

            case RESULT_CANCELED:
                break;
        }
    }


}


