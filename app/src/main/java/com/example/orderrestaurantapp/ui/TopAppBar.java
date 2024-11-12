package com.example.orderrestaurantapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.ui.fragments.OrderFragment;

public class TopAppBar {

    public static void setAppBarProperties(String refId, Context context, com.google.android.material.appbar.MaterialToolbar topAppBar) {

        topAppBar.setTitle("Orders für " + refId);
        topAppBar.getMenu().getItem(1).setIcon(R.drawable.ic_order_food__icon);

        topAppBar.getMenu().getItem(0).setVisible(false);
        topAppBar.getMenu().getItem(1).setVisible(true);
        topAppBar.getMenu().getItem(2).setVisible(false);
        if (MainActivity.actionMode != null) {
            MainActivity.actionMode.finish();
        }
        String finalRefId = refId;

        topAppBar
                .getMenu()
                .getItem(1)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                OrderFragment fragment = OrderFragment.newInstance();
                                Bundle bundle = new Bundle();
                                bundle.putString("TableID", finalRefId);
                                fragment.setArguments(bundle);
                                ((AppCompatActivity) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.containerView, fragment, null)
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            }
                        });


    }
}
