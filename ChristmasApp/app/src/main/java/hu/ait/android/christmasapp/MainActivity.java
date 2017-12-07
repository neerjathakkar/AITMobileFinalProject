package hu.ait.android.christmasapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.christmasapp.adapter.ItemsAdapter;
import hu.ait.android.christmasapp.data.Item;
import hu.ait.android.christmasapp.touch.ItemsListTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_ITEM_DETAILS = 102;
    public static final String KEY_ID = "KEY_ID";
    private ItemsAdapter itemsAdapter;
    private CoordinatorLayout layoutContent;
    private DrawerLayout drawerLayout;
    private int itemToDisplayPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        RealmResults<Item> allItems = getRealm().where(Item.class).findAll();
        Item itemsArray[] = new Item[allItems.size()];
        List<Item> itemsResult = new ArrayList<Item>(Arrays.asList(allItems.toArray(itemsArray)));

        itemsAdapter = new ItemsAdapter(itemsResult, this);
        RecyclerView recyclerViewPlaces = (RecyclerView) findViewById(
                R.id.recyclerViewPlaces);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlaces.setAdapter(itemsAdapter);

        ItemsListTouchHelperCallback touchHelperCallback = new ItemsListTouchHelperCallback(
                itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewPlaces);

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemActivity();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_add:
                                showAddItemActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                showSnackBarMessage("about text");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_help:
                                showSnackBarMessage("help text");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }

                        return false;
                    }
                });

        setUpToolBar();
    }
    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void showAddItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                AddItemActivity.class);

        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    public void showItemDetailsActivity(String itemID, int position) {
        Intent intentStart = new Intent(MainActivity.this, ItemDetailsActivity.class);

        itemToDisplayPosition = position;

        intentStart.putExtra(KEY_ID, itemID);

        startActivityForResult(intentStart, REQUEST_ITEM_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String itemID  = data.getStringExtra(
                        AddItemActivity.KEY_ITEM);

                Item item  = getRealm().where(Item.class)
                        .equalTo("itemID", itemID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_ITEM) {
                    itemsAdapter.addItem(item);
                    showSnackBarMessage("item added");
                }
                break;
            case RESULT_CANCELED:
                showSnackBarMessage("cancelled");
                break;
        }
    }

    public void deleteItem(Item item) {
        getRealm().beginTransaction();
        item.deleteFromRealm();
        getRealm().commitTransaction();
    }


    private void showSnackBarMessage(String message) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddItemActivity();
                return true;
            case R.id.access_list_1:

        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
