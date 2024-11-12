package com.example.orderrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;

import com.example.orderrestaurantapp.ui.fragments.TableManagerFragment;
import com.example.orderrestaurantapp.ui.fragments.UmsatzFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private static Context globalContext;
    public static ActionMode actionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalContext = getBaseContext();
        addListenersToBottomNavigationView();

    }

    private void addListenersToBottomNavigationView() {
        navigationView = findViewById(R.id.main_activity_bottom_navigation_view);
        navigationView.setOnItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();

                    if (itemId == R.id.table_dashboard) {
                        replaceFragment(TableManagerFragment.class);
                        return true;
                    } else if (itemId == R.id.finance) {
                        replaceFragment(UmsatzFragment.class);
                        return true;
                    } /**else if (itemId == R.id.menu_marvin) {
                        replaceFragment(MarvinFragment.class);
                        return true;
                    }
                    */
                    return super.onOptionsItemSelected(item);
                });
    }
    private void replaceFragment(Class<? extends Fragment> fragmentClass) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.containerView, fragmentClass, null)
                .commit();
    }
    public static Context getContext() {
        return globalContext;
    }
}