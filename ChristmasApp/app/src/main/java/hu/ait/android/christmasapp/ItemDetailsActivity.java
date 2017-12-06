package hu.ait.android.christmasapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import hu.ait.android.christmasapp.data.Item;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ItemDetailsActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_CITY";

    private Item itemToDisplay = null;
    private String itemName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        final TextView tvData = (TextView) findViewById(R.id.tvData);

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
        tvItemName.setText(itemName);

    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }


}

